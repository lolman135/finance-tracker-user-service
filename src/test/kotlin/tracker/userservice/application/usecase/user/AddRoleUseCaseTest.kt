package tracker.userservice.application.usecase.user

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import tracker.userservice.application.exception.role.RoleNotFoundException
import tracker.userservice.application.exception.user.UserNotFoundException
import tracker.userservice.application.usecase.user.command.AddRoleCommand
import tracker.userservice.domain.role.Role
import tracker.userservice.domain.role.RoleRepository
import tracker.userservice.domain.user.User
import tracker.userservice.domain.user.UserRepository
import java.time.LocalDate
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AddRoleUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var roleRepository: RoleRepository
    private lateinit var useCase: AddRoleUseCase

    @BeforeEach
    fun setup() {
        userRepository = mock()
        roleRepository = mock()
        useCase = AddRoleUseCase(userRepository, roleRepository)
    }

    @Test
    fun `execute should add role to user successfully`() {
        // Arrange
        val userId = UUID.randomUUID()
        val roleId = UUID.randomUUID()
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
        val newRole = Role(roleId, "ROLE_ADMIN")
        val command = AddRoleCommand(userId, "admin")
        val updatedUser = user.copy(roles = user.roles + newRole)

        whenever(userRepository.findById(userId)).thenReturn(user)
        whenever(roleRepository.findByName("ROLE_ADMIN")).thenReturn(newRole)
        whenever(userRepository.save(any())).thenReturn(updatedUser)

        // Act
        val result = useCase.execute(command)

        // Assert
        assertEquals(2, result.roles.size)
        assertTrue(result.roles.any { it.name == "ROLE_ADMIN" })
    }

    @Test
    fun `execute should throw exception when user not found`() {
        // Arrange
        val userId = UUID.randomUUID()
        val command = AddRoleCommand(userId, "ROLE_ADMIN")
        whenever(userRepository.findById(userId)).thenReturn(null)

        // Act & Assert
        assertThrows<UserNotFoundException> {
            useCase.execute(command)
        }
    }

    @Test
    fun execute_should_throw_exception_when_role_not_found() {
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
        val command = AddRoleCommand(userId, "ROLE_NONEXISTENT")
        whenever(userRepository.findById(userId)).thenReturn(user)
        whenever(roleRepository.findByName("ROLE_NONEXISTENT")).thenReturn(null)

        // Act & Assert
        assertThrows<RoleNotFoundException> {
            useCase.execute(command)
        }
    }
}

