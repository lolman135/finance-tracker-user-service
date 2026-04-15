package tracker.userservice.web

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import tracker.userservice.AbstractIT
import tracker.userservice.infrastructure.dto.role.RoleDtoInbound
import tracker.userservice.infrastructure.dto.user.UserLoginDtoInbound
import tracker.userservice.infrastructure.dto.user.UserRegisterDtoInbound

@DisplayName("Auth Controller Integration Test")
class AuthControllerIT @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper
) : AbstractIT() {

    // POST /api/v1/auth/register
    @Test
    fun `register should return token when request is valid`() {
        // Arrange
        createRole("user")
        val request = UserRegisterDtoInbound(
            firstName = "Alice",
            lastName = "Smith",
            email = "alice@example.com",
            phoneNumber = "+380990001122",
            password = "Password123"
        )

        // Act & Assert
        mockMvc.post("/api/v1/auth/register") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            jsonPath("$.type") { value("Bearer") }
            jsonPath("$.token") { exists() }
        }
    }

    @Test
    fun `register should return bad request when email is invalid`() {
        val request = mapOf(
            "firstName" to "Alice",
            "lastName" to "Smith",
            "email" to "invalid-email",
            "phoneNumber" to "+380990001122",
            "password" to "Password123"
        )

        mockMvc.post("/api/v1/auth/register") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isBadRequest() }
        }
    }

    // POST /api/v1/auth/login
    @Test
    fun `login should return token when credentials are correct`() {
        // Arrange: спочатку реєструємо користувача
        createRole("user")
        val registerRequest = UserRegisterDtoInbound(
            firstName = "Bob",
            lastName = "Jones",
            email = "bob@example.com",
            phoneNumber = "+380995554433",
            password = "SecurePassword1"
        )

        mockMvc.post("/api/v1/auth/register") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(registerRequest)
        }

        val loginRequest = UserLoginDtoInbound(
            email = "bob@example.com",
            password = "SecurePassword1"
        )

        // Act & Assert
        mockMvc.post("/api/v1/auth/login") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(loginRequest)
        }.andExpect {
            status { isOk() }
            jsonPath("$.type") { value("Bearer") }
            jsonPath("$.token") { exists() }
        }
    }

    @Test
    fun `login should return unauthorized when password is wrong`() {
        // Arrange
        createRole("user")
        registerUser("wrong_pass@example.com", "CorrectPassword1")

        val loginRequest = UserLoginDtoInbound(
            email = "wrong_pass@example.com",
            password = "WrongPassword2"
        )

        // Act & Assert
        mockMvc.post("/api/v1/auth/login") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(loginRequest)
        }.andExpect {
            status { isUnauthorized() } // Залежить від того, який статус кидає ваш ExceptionHandler для InvalidCredentialsException
        }
    }

    @Test
    fun `login should return unauthorized when user does not exist`() {
        val loginRequest = UserLoginDtoInbound(
            email = "non-existent@example.com",
            password = "SomePassword23"
        )

        mockMvc.post("/api/v1/auth/login") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(loginRequest)
        }.andExpect {
            status { isUnauthorized() }
        }
    }

    // Helpers
    private fun createRole(name: String) {
        mockMvc.post("/api/v1/roles") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(mapOf("name" to name))
        }
    }

    private fun registerUser(email: String, pass: String) {
        val request = UserRegisterDtoInbound(
            firstName = "Test",
            lastName = "User",
            email = email,
            phoneNumber = "+380000000000",
            password = pass
        )
        mockMvc.post("/api/v1/auth/register") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
    }
}