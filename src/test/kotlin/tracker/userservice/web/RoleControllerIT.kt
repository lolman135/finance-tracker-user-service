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
import java.util.UUID

@DisplayName("Role Controller Integration Test")
class RoleControllerIT @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper
) : AbstractIT() {

    // POST /api/v1/roles
    @Test
    @WithMockUser
    fun `create should return created when valid request`() {
        val inbound = RoleDtoInbound(name = "admin")

        mockMvc.post("/api/v1/roles") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(inbound)
        }.andExpect {
            status { isCreated() }
            header { exists("Location") }
            jsonPath("$.id") { exists() }
            jsonPath("$.name") { value("ROLE_ADMIN") }
        }
    }

    @Test
    @WithMockUser
    fun `create should return bad request when name is blank`() {
        val inbound = RoleDtoInbound(name = "   ")

        mockMvc.post("/api/v1/roles") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(inbound)
        }.andExpect {
            status { isBadRequest() }
            content { contentTypeCompatibleWith("application/problem+json") }
            jsonPath("$.status") { value(400) }
            jsonPath("$.detail") { value(containsString("Validation failed")) }
        }
    }

    @Test
    @WithMockUser
    fun `create should return bad request when body is missing`() {
        mockMvc.post("/api/v1/roles") {
            contentType = MediaType.APPLICATION_JSON
            content = "{}"
        }.andExpect {
            status { isBadRequest() }
        }
    }

    // GET /api/v1/roles
    @Test
    @WithMockUser
    fun `getAll should return ok and empty list when no roles`() {
        mockMvc.get("/api/v1/roles") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentTypeCompatibleWith("application/json") }
            jsonPath("$") { isArray() }
            jsonPath("$", hasSize<Any>(0)) {}
        }
    }

    @Test
    @WithMockUser
    fun `getAll should return ok and list when roles exist`() {
        createRole("user")
        createRole("admin")

        mockMvc.get("/api/v1/roles") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentTypeCompatibleWith("application/json") }
            jsonPath("$", hasSize<Any>(2)) {}
            jsonPath("$[0].id") { exists() }
            jsonPath("$[0].name") { exists() }
        }
    }

    // GET /api/v1/roles/page
    @Test
    @WithMockUser
    fun `getAllPageable should return ok with pagination metadata`() {
        createRole("ROLE_USER")
        createRole("ROLE_ADMIN")

        mockMvc.get("/api/v1/roles/page") {
            param("page", "0")
            param("size", "10")
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentTypeCompatibleWith("application/json") }
            jsonPath("$.items") { isArray() }
            jsonPath("$.items", hasSize<Any>(2)) {}
            jsonPath("$.metadata.totalElements") { value(2) }
            jsonPath("$.metadata.currentPage") { value(0) }
            jsonPath("$.metadata.pageSize") { value(10) }
        }
    }

    @Test
    @WithMockUser
    fun `getAllPageable should return correct page when second page requested`() {
        repeat(15) { createRole("ROLE_$it") }

        mockMvc.get("/api/v1/roles/page") {
            param("page", "1")
            param("size", "10")
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            jsonPath("$.items", hasSize<Any>(5)) {}
            jsonPath("$.metadata.currentPage") { value(1) }
            jsonPath("$.metadata.totalElements") { value(15) }
        }
    }

    // PUT /api/v1/roles/{id}
    @Test
    @WithMockUser
    fun `updateRole should return ok when valid request`() {
        val existingId = getExistingRoleId()
        val update = RoleDtoInbound(name = "moderator")

        mockMvc.put("/api/v1/roles/$existingId") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(update)
        }.andExpect {
            status { isOk() }
            content { contentTypeCompatibleWith("application/json") }
            jsonPath("$.id") { value(existingId.toString()) }
            jsonPath("$.name") { value("ROLE_MODERATOR") }
        }
    }

    @Test
    @WithMockUser
    fun `updateRole should return not found when role does not exist`() {
        val missingId = UUID.randomUUID()
        val update = RoleDtoInbound(name = "moderator")

        mockMvc.put("/api/v1/roles/$missingId") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(update)
        }.andExpect {
            status { isNotFound() }
            jsonPath("$.status") { value(404) }
        }
    }

    @Test
    @WithMockUser
    fun `updateRole should return bad request when name is blank`() {
        val existingId = getExistingRoleId()
        val update = RoleDtoInbound(name = "")

        mockMvc.put("/api/v1/roles/$existingId") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(update)
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.status") { value(400) }
            jsonPath("$.detail") { value(containsString("Validation failed")) }
        }
    }

    // DELETE /api/v1/roles/{id}
    @Test
    @WithMockUser
    fun `deleteRole should return no content when role exists`() {
        val existingId = getExistingRoleId()

        mockMvc.delete("/api/v1/roles/$existingId")
            .andExpect {
                status { isNoContent() }
            }

        mockMvc.get("/api/v1/roles") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            jsonPath("$", hasSize<Any>(0)) {}
        }
    }

    @Test
    @WithMockUser
    fun `deleteRole should return not found when role does not exist`() {
        val missingId = UUID.randomUUID()

        mockMvc.delete("/api/v1/roles/$missingId")
            .andExpect {
                status { isNoContent() }
            }
    }

    // Helpers
    private fun createRole(name: String): UUID {
        val result = mockMvc.post("/api/v1/roles") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(RoleDtoInbound(name = name))
        }.andReturn()

        val response = objectMapper.readTree(result.response.contentAsString)
        return UUID.fromString(response["id"].asText())
    }

    private fun getExistingRoleId(): UUID = createRole("ROLE_USER")
}
