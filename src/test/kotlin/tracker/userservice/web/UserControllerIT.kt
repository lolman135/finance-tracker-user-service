}
    }
        }
            userRepository.save(user)
            )
                roles = setOf(role)
                createdAt = LocalDate.now(),
                passwordHash = "hashed_password",
                phoneNumber = "+1234567890$index",
                email = "user$index@example.com",
                lastName = "LastName$index",
                firstName = "User$index",
                id = UUID.randomUUID(),
            val user = User(
        repeat(count) { index ->
        val role = roleRepository.findAll().first()
    private fun createMultipleUsers(count: Int) {

    }
        return user.id
        userRepository.save(user)
        )
            roles = setOf(roleRepository.findAll().first())
            createdAt = LocalDate.now(),
            passwordHash = "hashed_password",
            phoneNumber = "+19876543210",
            email = "jane@example.com",
            lastName = "Smith",
            firstName = "Jane",
            id = UUID.randomUUID(),
        val user = User(
    private fun getExistingUserId(): UUID {

    }
        }
            status { isNotFound() }
        }.andExpect {
            content = objectMapper.writeValueAsString(addRoleRequest)
            contentType = MediaType.APPLICATION_JSON
        mockMvc.post("/api/v1/users/$userId/roles") {

        val addRoleRequest = AddRoleDtoInbound(roleName = "ROLE_NONEXISTENT")
        val userId = getExistingUserId()
    fun ensureNotFoundWhenAddingNonExistentRole() {
    @DisplayName("POST /api/v1/users/{userId}/roles - should return not found when role missing")
    @WithMockUser
    @Test

    }
        }
            status { isNotFound() }
        }.andExpect {
            content = objectMapper.writeValueAsString(addRoleRequest)
            contentType = MediaType.APPLICATION_JSON
        mockMvc.post("/api/v1/users/$missingId/roles") {

        val addRoleRequest = AddRoleDtoInbound(roleName = "ROLE_ADMIN")
        val missingId = UUID.randomUUID()
    fun ensureNotFoundWhenAddingRoleToNonExistentUser() {
    @DisplayName("POST /api/v1/users/{userId}/roles - should return not found when user missing")
    @WithMockUser
    @Test

    }
        }
            jsonPath("$.roles.length()") { value(2) }
            jsonPath("$.roles") { isArray() }
            status { isOk() }
        }.andExpect {
            content = objectMapper.writeValueAsString(addRoleRequest)
            contentType = MediaType.APPLICATION_JSON
        mockMvc.post("/api/v1/users/$userId/roles") {

        val addRoleRequest = AddRoleDtoInbound(roleName = "ROLE_ADMIN")

        roleRepository.save(adminRole)
        val adminRole = Role(id = UUID.randomUUID(), name = "ROLE_ADMIN")
        val userId = getExistingUserId()
    fun ensureRoleAddedToUserSuccessfully() {
    @DisplayName("POST /api/v1/users/{userId}/roles - should add role to user")
    @WithMockUser
    @Test

    }
        }
            status { isNotFound() }
        }.andExpect {
            accept = MediaType.APPLICATION_JSON
        mockMvc.get("/api/v1/users/$userId") {

            }
                status { isNoContent() }
            .andExpect {
        mockMvc.delete("/api/v1/users/$userId")

        val userId = getExistingUserId()
    fun ensureUserDeletedSuccessfully() {
    @DisplayName("DELETE /api/v1/users/{id} - should delete user successfully")
    @WithMockUser
    @Test

    }
        }
            status { isNotFound() }
        }.andExpect {
            content = objectMapper.writeValueAsString(updateRequest)
            contentType = MediaType.APPLICATION_JSON
        mockMvc.put("/api/v1/users/$missingId") {

        )
            password = null
            phoneNumber = null,
            email = null,
            lastName = null,
            firstName = "Updated",
        val updateRequest = UserUpdateDtoInbound(
        val missingId = UUID.randomUUID()
    fun ensureNotFoundWhenUpdatingNonExistentUser() {
    @DisplayName("PUT /api/v1/users/{id} - should return not found when user missing")
    @WithMockUser
    @Test

    }
        }
            status { isBadRequest() }
        }.andExpect {
            content = objectMapper.writeValueAsString(updateRequest)
            contentType = MediaType.APPLICATION_JSON
        mockMvc.put("/api/v1/users/$userId") {

        )
            password = null
            phoneNumber = null,
            email = "invalid-email",
            lastName = null,
            firstName = null,
        val updateRequest = UserUpdateDtoInbound(
        val userId = getExistingUserId()
    fun ensureBadRequestWhenUpdatingWithInvalidEmail() {
    @DisplayName("PUT /api/v1/users/{id} - should return bad request when email invalid")
    @WithMockUser
    @Test

    }
        }
            jsonPath("$.lastName") { value("Doe") }
            jsonPath("$.firstName") { value("Janet") }
            status { isOk() }
        }.andExpect {
            content = objectMapper.writeValueAsString(updateRequest)
            contentType = MediaType.APPLICATION_JSON
        mockMvc.put("/api/v1/users/$userId") {

        )
            password = null
            phoneNumber = null,
            email = null,
            lastName = "Doe",
            firstName = "Janet",
        val updateRequest = UserUpdateDtoInbound(
        val userId = getExistingUserId()
    fun ensureUserUpdatedSuccessfully() {
    @DisplayName("PUT /api/v1/users/{id} - should update user successfully")
    @WithMockUser
    @Test

    }
        }
            status { isNotFound() }
        }.andExpect {
            accept = MediaType.APPLICATION_JSON
        mockMvc.get("/api/v1/users/by-full-name?firstName=Unknown&lastName=Person") {
    fun ensureNotFoundForNonExistingFullName() {
    @DisplayName("GET /api/v1/users/by-full-name - should return not found for non-existing name")
    @WithMockUser
    @Test

    }
        }
            jsonPath("$.lastName") { value("Smith") }
            jsonPath("$.firstName") { value("Jane") }
            status { isOk() }
        }.andExpect {
            accept = MediaType.APPLICATION_JSON
        mockMvc.get("/api/v1/users/by-full-name?firstName=Jane&lastName=Smith") {

        getExistingUserId()
    fun ensureUserReturnedByFullName() {
    @DisplayName("GET /api/v1/users/by-full-name - should return user by full name")
    @WithMockUser
    @Test

    }
        }
            status { isNotFound() }
        }.andExpect {
            accept = MediaType.APPLICATION_JSON
        mockMvc.get("/api/v1/users/by-phone?phone=%2B15555555555") {
    fun ensureNotFoundForNonExistingPhone() {
    @DisplayName("GET /api/v1/users/by-phone - should return not found for non-existing phone")
    @WithMockUser
    @Test

    }
        }
            jsonPath("$.firstName") { value("Jane") }
            jsonPath("$.phoneNumber") { exists() }
            status { isOk() }
        }.andExpect {
            accept = MediaType.APPLICATION_JSON
        mockMvc.get("/api/v1/users/by-phone?phone=%2B19876543210") {

        getExistingUserId()
    fun ensureUserReturnedByPhone() {
    @DisplayName("GET /api/v1/users/by-phone - should return user by phone")
    @WithMockUser
    @Test

    }
        }
            status { isNotFound() }
        }.andExpect {
            accept = MediaType.APPLICATION_JSON
        mockMvc.get("/api/v1/users/by-email?email=nonexistent@example.com") {
    fun ensureNotFoundForNonExistingEmail() {
    @DisplayName("GET /api/v1/users/by-email - should return not found for non-existing email")
    @WithMockUser
    @Test

    }
        }
            jsonPath("$.firstName") { value("Jane") }
            jsonPath("$.email") { value("jane@example.com") }
            status { isOk() }
        }.andExpect {
            accept = MediaType.APPLICATION_JSON
        mockMvc.get("/api/v1/users/by-email?email=jane@example.com") {

        getExistingUserId()
    fun ensureUserReturnedByEmail() {
    @DisplayName("GET /api/v1/users/by-email - should return user by email")
    @WithMockUser
    @Test

    }
        }
            jsonPath("$.total") { value(3) }
            jsonPath("$.size") { value(10) }
            jsonPath("$.page") { value(0) }
            jsonPath("$.items.length()") { value(3) }
            jsonPath("$.items") { isArray() }
            status { isOk() }
        }.andExpect {
            accept = MediaType.APPLICATION_JSON
        mockMvc.get("/api/v1/users?page=0&size=10") {

        createMultipleUsers(3)
    fun ensurePaginatedUsersReturned() {
    @DisplayName("GET /api/v1/users - should return paginated users")
    @WithMockUser
    @Test

    }
        }
            status { isNotFound() }
        }.andExpect {
            accept = MediaType.APPLICATION_JSON
        mockMvc.get("/api/v1/users/$missingId") {

        val missingId = UUID.randomUUID()
    fun ensureNotFoundWhenUserMissing() {
    @DisplayName("GET /api/v1/users/{id} - should return not found when missing")
    @WithMockUser
    @Test

    }
        }
            jsonPath("$.email") { value("jane@example.com") }
            jsonPath("$.firstName") { value("Jane") }
            jsonPath("$.id") { exists() }
            status { isOk() }
        }.andExpect {
            accept = MediaType.APPLICATION_JSON
        mockMvc.get("/api/v1/users/$userId") {

        val userId = getExistingUserId()
    fun ensureUserReturnedWhenExists() {
    @DisplayName("GET /api/v1/users/{id} - should return user when exists")
    @WithMockUser
    @Test

    }
        }
            status { isBadRequest() }
        }.andExpect {
            content = objectMapper.writeValueAsString(inbound)
            contentType = MediaType.APPLICATION_JSON
        mockMvc.post("/api/v1/users") {

        )
            password = "weak"
            phoneNumber = "+12345678901",
            email = "john@example.com",
            lastName = "Doe",
            firstName = "John",
        val inbound = UserRegisterDtoInbound(
    fun ensureBadRequestWhenPasswordWeak() {
    @DisplayName("POST /api/v1/users - should return bad request when password weak")
    @WithMockUser
    @Test

    }
        }
            status { isBadRequest() }
        }.andExpect {
            content = objectMapper.writeValueAsString(inbound)
            contentType = MediaType.APPLICATION_JSON
        mockMvc.post("/api/v1/users") {

        )
            password = "SecurePass123"
            phoneNumber = "123",
            email = "john@example.com",
            lastName = "Doe",
            firstName = "John",
        val inbound = UserRegisterDtoInbound(
    fun ensureBadRequestWhenPhoneInvalid() {
    @DisplayName("POST /api/v1/users - should return bad request when phone invalid")
    @WithMockUser
    @Test

    }
        }
            status { isBadRequest() }
        }.andExpect {
            content = objectMapper.writeValueAsString(inbound)
            contentType = MediaType.APPLICATION_JSON
        mockMvc.post("/api/v1/users") {

        )
            password = "SecurePass123"
            phoneNumber = "+12345678901",
            email = "invalid-email",
            lastName = "Doe",
            firstName = "John",
        val inbound = UserRegisterDtoInbound(
    fun ensureBadRequestWhenEmailInvalid() {
    @DisplayName("POST /api/v1/users - should return bad request when email invalid")
    @WithMockUser
    @Test

    }
        }
            jsonPath("$.phoneNumber") { value("+12345678901") }
            jsonPath("$.email") { value("john@example.com") }
            jsonPath("$.lastName") { value("Doe") }
            jsonPath("$.firstName") { value("John") }
            jsonPath("$.id") { exists() }
            status { isCreated() }
        }.andExpect {
            content = objectMapper.writeValueAsString(inbound)
            contentType = MediaType.APPLICATION_JSON
        mockMvc.post("/api/v1/users") {

        )
            password = "SecurePass123"
            phoneNumber = "+12345678901",
            email = "john@example.com",
            lastName = "Doe",
            firstName = "John",
        val inbound = UserRegisterDtoInbound(
    fun ensureUserCreatedSuccessfully() {
    @DisplayName("POST /api/v1/users - should create user successfully")
    @WithMockUser
    @Test

    }
        roleRepository.save(role)
        val role = Role(id = UUID.randomUUID(), name = "ROLE_USER")
    private fun setupDefaultRole() {

    }
        setupDefaultRole()
        roleRepository.deleteAll()
        userRepository.deleteAll()
    fun setUp() {
    @BeforeEach

) : AbstractIT() {
    private val roleRepository: RoleRepository
    private val userRepository: UserRepository,
    private val objectMapper: ObjectMapper,
    private val mockMvc: MockMvc,
class UserControllerIT @Autowired constructor(
@DisplayName("User Controller Integration Test")

import java.util.UUID
import java.time.LocalDate
import tracker.userservice.infrastructure.dto.user.UserUpdateDtoInbound
import tracker.userservice.infrastructure.dto.user.UserRegisterDtoInbound
import tracker.userservice.infrastructure.dto.user.AddRoleDtoInbound
import tracker.userservice.domain.user.UserRepository
import tracker.userservice.domain.user.User
import tracker.userservice.domain.role.RoleRepository
import tracker.userservice.domain.role.Role
import tracker.userservice.AbstractIT
import org.springframework.test.web.servlet.put
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.MockMvc
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.http.MediaType
import org.springframework.beans.factory.annotation.Autowired
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.BeforeEach
import org.hamcrest.Matchers.containsString
import com.fasterxml.jackson.databind.ObjectMapper


