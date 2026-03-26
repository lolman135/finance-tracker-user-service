package tracker.userservice.application.usecase.user

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import tracker.userservice.application.usecase.user.command.FindUserByFullNameCommand
import tracker.userservice.domain.role.Role
import tracker.userservice.domain.user.User
import tracker.userservice.domain.user.UserRepository
import java.time.LocalDate
import java.util.UUID
import kotlin.test.assertEquals

class FindUsersByFullNameUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var useCase: FindUserByFullNameUseCase

    @BeforeEach
    fun setup() {
        userRepository = mock()
        useCase = FindUserByFullNameUseCase(userRepository)
    }

    @Test
    fun `execute should return list of users when found by full name`() {
        // Arrange
        val firstName = "John"
        val lastName = "Doe"
        val command = FindUserByFullNameCommand(firstName, lastName)
        val users = listOf(
            User(
                id = UUID.randomUUID(),
                firstName = firstName,
                lastName = lastName,
                email = "john@example.com",
                phoneNumber = "1234567890",
                passwordHash = "hash",
                createdAt = LocalDate.now(),
                roles = setOf(Role(UUID.randomUUID(), "ROLE_USER"))
            )
        )
        whenever(userRepository.findByFullName(firstName, lastName)).thenReturn(users)

        // Act
        val result = useCase.execute(command)

        // Assert
        assertEquals(1, result.size)
        assertEquals(firstName, result.first().firstName)
        assertEquals(lastName, result.first().lastName)
    }

    @Test
    fun `execute should return empty list when no users found by full name`() {
        // Arrange
        val firstName = "Unknown"
        val lastName = "User"
        val command = FindUserByFullNameCommand(firstName, lastName)
        whenever(userRepository.findByFullName(firstName, lastName)).thenReturn(emptyList())

        // Act
        val result = useCase.execute(command)

        // Assert
        assertEquals(0, result.size)
    }
}