package tracker.userservice.web.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import tracker.userservice.AbstractIT
import tracker.userservice.infrastructure.dto.role.RoleDtoInbound
import tracker.userservice.infrastructure.dto.user.UserRegisterDtoInbound
import tracker.userservice.infrastructure.dto.user.UserUpdateDtoInbound
import java.util.UUID

@DisplayName("User Controller Integration Test")
class UserControllerIT @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper
) : AbstractIT() {

    // POST /api/v1/users
    @Test
    @WithMockUser
    fun `createUser should return created when valid request`() {
        createRole("user")
        val request = validRegisterRequest()

        mockMvc.post("/api/v1/users") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isCreated() }
            jsonPath("$.id") { exists() }
            jsonPath("$.firstName") { value("John") }
            jsonPath("$.lastName") { value("Doe") }
            jsonPath("$.email") { value("john.doe@example.com") }
            jsonPath("$.phoneNumber") { value("+380991234567") }
            jsonPath("$.roles") { isArray() }
        }
    }

    @Test
    @WithMockUser
    fun `createUser should return bad request when email invalid`() {
        val request = validRegisterRequest().copy(email = "not-an-email")  // UserRegisterDtoInbound не data class, придётся руками

        mockMvc.post("/api/v1/users") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                  "firstName": "John",
                  "lastName": "Doe",
                  "email": "not-an-email",
                  "phoneNumber": "+380991234567",
                  "password": "Password1"
                }
            """.trimIndent()
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.status") { value(400) }
            jsonPath("$.detail") { value(containsString("Validation failed")) }
        }
    }

    @Test
    @WithMockUser
    fun `createUser should return bad request when password too weak`() {
        mockMvc.post("/api/v1/users") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                  "firstName": "John",
                  "lastName": "Doe",
                  "email": "john.doe@example.com",
                  "phoneNumber": "+380991234567",
                  "password": "weak"
                }
            """.trimIndent()
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.status") { value(400) }
        }
    }

    @Test
    @WithMockUser
    fun `createUser should return conflict when email already exists`() {
        createRole("user")

        val request = objectMapper.writeValueAsString(validRegisterRequest())

        mockMvc.post("/api/v1/users") {
            contentType = MediaType.APPLICATION_JSON
            content = request
        }

        mockMvc.post("/api/v1/users") {
            contentType = MediaType.APPLICATION_JSON
            content = request
        }.andExpect {
            status { isConflict() }
        }
    }

    // GET /api/v1/users/{id}
    @Test
    @WithMockUser
    fun `getUserById should return ok when user exists`() {
        val existingId = getExistingUserId()

        mockMvc.get("/api/v1/users/$existingId") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            jsonPath("$.id") { value(existingId.toString()) }
            jsonPath("$.firstName") { value("John") }
        }
    }

    @Test
    @WithMockUser
    fun `getUserById should return not found when user missing`() {
        mockMvc.get("/api/v1/users/${UUID.randomUUID()}") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isNotFound() }
            jsonPath("$.status") { value(404) }
        }
    }

    // PUT /api/v1/users/{id}
    @Test
    @WithMockUser
    fun `updateUser should return ok when valid partial update`() {
        val existingId = getExistingUserId()
        val update = UserUpdateDtoInbound(firstName = "Jane")

        mockMvc.put("/api/v1/users/$existingId") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(update)
        }.andExpect {
            status { isOk() }
            jsonPath("$.id") { value(existingId.toString()) }
            jsonPath("$.firstName") { value("Jane") }
            jsonPath("$.lastName") { value("Doe") }
        }
    }

    @Test
    @WithMockUser
    fun `updateUser should return not found when user missing`() {
        val update = UserUpdateDtoInbound(firstName = "Jane")

        mockMvc.put("/api/v1/users/${UUID.randomUUID()}") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(update)
        }.andExpect {
            status { isNotFound() }
        }
    }

    @Test
    @WithMockUser
    fun `updateUser should return bad request when email invalid`() {
        val existingId = getExistingUserId()
        val update = UserUpdateDtoInbound(email = "not-valid")

        mockMvc.put("/api/v1/users/$existingId") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(update)
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.status") { value(400) }
        }
    }

    // DELETE /api/v1/users/{id}
    @Test
    @WithMockUser
    fun `deleteUser should return no content when user exists`() {
        val existingId = getExistingUserId()

        mockMvc.delete("/api/v1/users/$existingId")
            .andExpect {
                status { isNoContent() }
            }

        mockMvc.get("/api/v1/users/$existingId")
            .andExpect {
                status { isNotFound() }
            }
    }

    @Test
    @WithMockUser
    fun `deleteUser should return not found when user missing`() {
        mockMvc.delete("/api/v1/users/${UUID.randomUUID()}")
            .andExpect {
                status { isNoContent() }
            }
    }

    // GET /api/v1/users
    @Test
    @WithMockUser
    fun `getAllUsers should return ok with pagination metadata`() {
        createUser("john.doe@example.com", "+380991234567")
        createUser("jane.doe@example.com", "+380991234568")

        mockMvc.get("/api/v1/users") {
            param("page", "0")
            param("size", "10")
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            jsonPath("$.items") { isArray() }
            jsonPath("$.items", hasSize<Any>(2)) {}
            jsonPath("$.metadata.totalElements") { value(2) }
            jsonPath("$.metadata.currentPage") { value(0) }
            jsonPath("$.metadata.pageSize") { value(10) }
        }
    }

    // GET /api/v1/users/by-email
    @Test
    @WithMockUser
    fun `getUserByEmail should return ok when user exists`() {
        getExistingUserId()

        mockMvc.get("/api/v1/users/by-email") {
            param("email", "john.doe@example.com")
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            jsonPath("$.email") { value("john.doe@example.com") }
        }
    }

    @Test
    @WithMockUser
    fun `getUserByEmail should return not found when user missing`() {
        mockMvc.get("/api/v1/users/by-email") {
            param("email", "missing@example.com")
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isNotFound() }
        }
    }

    // GET /api/v1/users/by-phone
    @Test
    @WithMockUser
    fun `getUserByPhone should return ok when user exists`() {
        getExistingUserId()

        mockMvc.get("/api/v1/users/by-phone") {
            param("phone", "+380991234567")
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            jsonPath("$.phoneNumber") { value("+380991234567") }
        }
    }

    @Test
    @WithMockUser
    fun `getUserByPhone should return not found when user missing`() {
        mockMvc.get("/api/v1/users/by-phone") {
            param("phone", "+380000000000")
        }.andExpect {
            status { isNotFound() }
        }
    }

    // GET /api/v1/users/by-full-name
    @Test
    @WithMockUser
    fun `getUserByFullName should return list when users found`() {
        getExistingUserId()

        mockMvc.get("/api/v1/users/by-full-name") {
            param("firstName", "John")
            param("lastName", "Doe")
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            jsonPath("$") { isArray() }
            jsonPath("$", hasSize<Any>(1)) {}
            jsonPath("$[0].firstName") { value("John") }
            jsonPath("$[0].lastName") { value("Doe") }
        }
    }

    @Test
    @WithMockUser
    fun `getUserByFullName should return empty list when no users found`() {
        mockMvc.get("/api/v1/users/by-full-name") {
            param("firstName", "Unknown")
            param("lastName", "Person")
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            jsonPath("$", hasSize<Any>(0)) {}
        }
    }

    // POST /api/v1/users/{userId}/roles
    @Test
    @WithMockUser
    fun `addRoleToUser should return ok when role and user exist`() {
        val userId = getExistingUserId()
        createRole("admin")

        mockMvc.post("/api/v1/users/$userId/roles") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(mapOf("roleName" to "admin"))
        }.andExpect {
            status { isOk() }
            jsonPath("$.id") { value(userId.toString()) }
            jsonPath("$.roles[?(@.name == 'ROLE_ADMIN')]") { exists() }
        }
    }

    @Test
    @WithMockUser
    fun `addRoleToUser should return not found when user missing`() {
        createRole("admin")

        mockMvc.post("/api/v1/users/${UUID.randomUUID()}/roles") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(mapOf("roleName" to "ROLE_ADMIN"))
        }.andExpect {
            status { isNotFound() }
        }
    }

    @Test
    @WithMockUser
    fun `addRoleToUser should return bad request when roleName is blank`() {
        val userId = getExistingUserId()

        mockMvc.post("/api/v1/users/$userId/roles") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(mapOf("roleName" to ""))
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.status") { value(400) }
        }
    }

    // Helpers
    private fun validRegisterRequest() = UserRegisterDtoInbound(
        firstName = "John",
        lastName = "Doe",
        email = "john.doe@example.com",
        phoneNumber = "+380991234567",
        password = "Password1"
    )

    private fun createUser(
        email: String,
        phoneNumber: String
    ): UUID {
        createRole("user")
        val inbound = UserRegisterDtoInbound(
            firstName = "John",
            lastName = "Doe",
            email = email,
            phoneNumber = phoneNumber,
            password = "Password1"
        )

        val result = mockMvc.post("/api/v1/users") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(inbound)
        }.andReturn()

        val response = objectMapper.readTree(result.response.contentAsString)
        return UUID.fromString(response["id"].asText())
    }

    private fun getExistingUserId(): UUID = createUser("john.doe@example.com", "+380991234567")

    private fun createRole(name: String) {
        mockMvc.post("/api/v1/roles") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(RoleDtoInbound(name = name))
        }
    }
}