package tracker.userservice.application.usecase.auth

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import tracker.userservice.application.JwtProvider
import tracker.userservice.application.exception.InvalidCredentialsException
import tracker.userservice.application.usecase.auth.command.LoginUserCommand
import tracker.userservice.application.usecase.user.FIndUserByEmailUseCase
import tracker.userservice.domain.user.User
import java.util.UUID

class LoginUseCaseTest {

    private val fIndUserByEmailUseCase: FIndUserByEmailUseCase = mock()
    private val authenticationManager: AuthenticationManager = mock()
    private val jwtProvider: JwtProvider = mock()
    private val useCase = LoginUseCase(fIndUserByEmailUseCase, authenticationManager, jwtProvider)

    @Test
    fun `execute should return token when credentials are valid`() {
        // Arrange
        val command = LoginUserCommand("test@example.com", "password")
        val userId = UUID.randomUUID()
        val mockUser = mock<User> { on { id } doReturn userId }
        val mockAuth = mock<Authentication>()

        whenever(authenticationManager.authenticate(any())).thenReturn(mockAuth)
        whenever(fIndUserByEmailUseCase.execute(command.email)).thenReturn(mockUser)
        whenever(jwtProvider.generateToken(userId)).thenReturn("mock-jwt-token")

        // Act
        val result = useCase.execute(command)

        // Assert
        assertEquals("mock-jwt-token", result)
        verify(authenticationManager).authenticate(any())
        verify(jwtProvider).generateToken(userId)
    }

    @Test
    fun `execute should throw InvalidCredentialsException when authentication fails`() {
        // Arrange
        val command = LoginUserCommand("wrong@example.com", "wrong")
        whenever(authenticationManager.authenticate(any())).thenThrow(BadCredentialsException("Failed"))

        // Act & Assert
        assertThrows<InvalidCredentialsException> {
            useCase.execute(command)
        }
    }
}