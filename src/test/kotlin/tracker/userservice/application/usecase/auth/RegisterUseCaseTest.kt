package tracker.userservice.application.usecase.auth

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import tracker.userservice.application.JwtProvider
import tracker.userservice.application.usecase.auth.command.RegisterUserCommand
import tracker.userservice.application.usecase.user.CreateUserUseCase
import tracker.userservice.domain.user.User
import java.util.UUID

class RegisterUseCaseTest {

    private val jwtProvider: JwtProvider = mock()
    private val createUserUseCase: CreateUserUseCase = mock()
    private val useCase = RegisterUseCase(jwtProvider, createUserUseCase)

    @Test
    fun `execute should register user and return token`() {
        // Arrange
        val command = RegisterUserCommand(
            firstName = "John",
            lastName = "Doe",
            phoneNumber = "123456789",
            email = "john@example.com",
            password = "securePassword"
        )
        val userId = UUID.randomUUID()
        val registeredUser = mock<User> { on { id } doReturn userId }

        whenever(createUserUseCase.execute(any())).thenReturn(registeredUser)
        whenever(jwtProvider.generateToken(userId)).thenReturn("generated-jwt-token")

        // Act
        val result = useCase.execute(command)

        // Assert
        assertEquals("generated-jwt-token", result)
        verify(createUserUseCase).execute(argThat {
            this.email == command.email && this.firstName == command.firstName
        })
        verify(jwtProvider).generateToken(userId)
    }
}