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

class FindUserByPhoneUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var useCase: FindUserByPhoneUseCase

    @BeforeEach
    fun setup() {
        userRepository = mock()
        useCase = FindUserByPhoneUseCase(userRepository)
    }

    @Test
    fun `execute should return user when found by phone number`() {
        // Arrange
        val phoneNumber = "1234567890"
        val user = User(
            id = UUID.randomUUID(),
            firstName = "John",
            lastName = "Doe",
            email = "john@example.com",
            phoneNumber = phoneNumber,
            passwordHash = "hash",
            createdAt = LocalDate.now(),
            roles = setOf(Role(UUID.randomUUID(), "ROLE_USER"))
        )
        whenever(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(user)

        // Act
        val result = useCase.execute(phoneNumber)

        // Assert
        assertEquals(phoneNumber, result.phoneNumber)
        assertEquals("John", result.firstName)
    }

    @Test
    fun execute_should_throw_exception_when_user_not_found_by_phone_number() {
        // Arrange
        val phoneNumber = "9999999999"
        whenever(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(null)

        // Act & Assert
        assertThrows<UserNotFoundException> {
            useCase.execute(phoneNumber)
        }
    }
}

