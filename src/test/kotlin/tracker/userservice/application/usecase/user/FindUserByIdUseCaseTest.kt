package tracker.userservice.application.usecase.user

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import tracker.userservice.application.exception.user.UserNotFoundException
import tracker.userservice.domain.role.Role
import tracker.userservice.domain.user.User
import tracker.userservice.domain.user.UserRepository
import java.time.LocalDate
import java.util.UUID
import kotlin.test.assertEquals

class FindUserByIdUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var useCase: FindUserByIdUseCase

    @BeforeEach
    fun setup() {
        userRepository = mock()
        useCase = FindUserByIdUseCase(userRepository)
    }

    @Test
    fun `execute should return user when found`() {
        // Arrange
        val userId = UUID.randomUUID()
        val user = User(
            id = userId,
            firstName = "John",
            lastName = "Doe",
            email = "john@example.com",
            phoneNumber = "1234567890",
            passwordHash = "hash",
            createdAt = LocalDate.now(),
            roles = setOf(Role(UUID.randomUUID(), "ROLE_USER"))
        )
        whenever(userRepository.findById(userId)).thenReturn(user)

        // Act
        val result = useCase.execute(userId)

        // Assert
        assertEquals(userId, result.id)
        assertEquals("John", result.firstName)
        assertEquals("Doe", result.lastName)
    }

    @Test
    fun execute_should_throw_exception_when_user_not_found() {
        // Arrange
        val userId = UUID.randomUUID()
        whenever(userRepository.findById(userId)).thenReturn(null)

        // Act & Assert
        assertThrows<UserNotFoundException> {
            useCase.execute(userId)
        }
    }
}

