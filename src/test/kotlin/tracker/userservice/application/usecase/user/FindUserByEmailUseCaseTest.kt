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

class FindUserByEmailUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var useCase: FIndUserByEmailUseCase

    @BeforeEach
    fun setup() {
        userRepository = mock()
        useCase = FIndUserByEmailUseCase(userRepository)
    }

    @Test
    fun `execute should return user when found by email`() {
        // Arrange
        val email = "john@example.com"
        val user = User(
            id = UUID.randomUUID(),
            firstName = "John",
            lastName = "Doe",
            email = email,
            phoneNumber = "1234567890",
            passwordHash = "hash",
            createdAt = LocalDate.now(),
            roles = setOf(Role(UUID.randomUUID(), "ROLE_USER"))
        )
        whenever(userRepository.findByEmail(email)).thenReturn(user)

        // Act
        val result = useCase.execute(email)

        // Assert
        assertEquals(email, result.email)
        assertEquals("John", result.firstName)
    }

    @Test
    fun execute_should_throw_exception_when_user_not_found_by_email() {
        // Arrange
        val email = "notfound@example.com"
        whenever(userRepository.findByEmail(email)).thenReturn(null)

        // Act & Assert
        assertThrows<UserNotFoundException> {
            useCase.execute(email)
        }
    }
}

