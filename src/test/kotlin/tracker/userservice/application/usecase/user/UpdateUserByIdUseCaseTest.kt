package tracker.userservice.application.usecase.user

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.security.crypto.password.PasswordEncoder
import tracker.userservice.application.exception.user.UserNotFoundException
import tracker.userservice.application.exception.user.UserWithEmailAlreadyExistsException
import tracker.userservice.application.exception.user.UserWithPhoneAlreadyExistsException
import tracker.userservice.application.usecase.user.command.UpdateUserCommand
import tracker.userservice.domain.role.Role
import tracker.userservice.domain.user.User
import tracker.userservice.domain.user.UserRepository
import java.time.LocalDate
import java.util.UUID
import kotlin.test.assertEquals

class UpdateUserByIdUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var passwordEncoder: PasswordEncoder
    private lateinit var useCase: UpdateUserByIdUseCase

    @BeforeEach
    fun setup() {
        userRepository = mock()
        passwordEncoder = mock()
        useCase = UpdateUserByIdUseCase(userRepository, passwordEncoder)
    }

    @Test
    fun `execute should update user successfully with all fields`() {
        // Arrange
        val userId = UUID.randomUUID()
        val existingUser = User(
            id = userId,
            firstName = "John",
            lastName = "Doe",
            email = "john@example.com",
            phoneNumber = "1234567890",
            passwordHash = "oldHash",
            createdAt = LocalDate.now(),
            roles = setOf(Role(UUID.randomUUID(), "ROLE_USER"))
        )
        val command = UpdateUserCommand(
            id = userId,
            firstName = "Jane",
            lastName = "Smith",
            email = "jane@example.com",
            phoneNumber = "0987654321",
            password = "newPassword"
        )
        val encodedPassword = "encodedNewPassword"

        whenever(userRepository.findById(userId)).thenReturn(existingUser)
        whenever(userRepository.findByEmail("jane@example.com")).thenReturn(null)
        whenever(userRepository.findByPhoneNumber("0987654321")).thenReturn(null)
        whenever(passwordEncoder.encode(command.password)).thenReturn(encodedPassword)
        val updatedUser = existingUser.copy(
            firstName = "Jane",
            lastName = "Smith",
            email = "jane@example.com",
            phoneNumber = "0987654321",
            passwordHash = encodedPassword
        )
        whenever(userRepository.save(org.mockito.kotlin.any())).thenReturn(updatedUser)

        // Act
        val result = useCase.execute(command)

        // Assert
        assertEquals("Jane", result.firstName)
        assertEquals("Smith", result.lastName)
        assertEquals("jane@example.com", result.email)
        assertEquals("0987654321", result.phoneNumber)
    }

    @Test
    fun `execute should update only provided fields`() {
        // Arrange
        val userId = UUID.randomUUID()
        val existingUser = User(
            id = userId,
            firstName = "John",
            lastName = "Doe",
            email = "john@example.com",
            phoneNumber = "1234567890",
            passwordHash = "hash",
            createdAt = LocalDate.now(),
            roles = setOf(Role(UUID.randomUUID(), "ROLE_USER"))
        )
        val command = UpdateUserCommand(
            id = userId,
            firstName = "Jane",
            lastName = null,
            email = null,
            phoneNumber = null,
            password = null
        )

        whenever(userRepository.findById(userId)).thenReturn(existingUser)
        val updatedUser = existingUser.copy(firstName = "Jane")
        whenever(userRepository.save(org.mockito.kotlin.any())).thenReturn(updatedUser)

        // Act
        val result = useCase.execute(command)

        // Assert
        assertEquals("Jane", result.firstName)
        assertEquals("Doe", result.lastName)
        assertEquals("john@example.com", result.email)
        assertEquals("1234567890", result.phoneNumber)
    }

    @Test
    fun `execute should throw exception when user not found`() {
        // Arrange
        val userId = UUID.randomUUID()
        val command = UpdateUserCommand(
            id = userId,
            firstName = "Jane",
            lastName = null,
            email = null,
            phoneNumber = null,
            password = null
        )
        whenever(userRepository.findById(userId)).thenReturn(null)

        // Act & Assert
        assertThrows<UserNotFoundException> {
            useCase.execute(command)
        }
    }

    @Test
    fun `execute should throw exception when new email already exists for different user`() {
        // Arrange
        val userId = UUID.randomUUID()
        val existingUser = User(
            id = userId,
            firstName = "John",
            lastName = "Doe",
            email = "john@example.com",
            phoneNumber = "1234567890",
            passwordHash = "hash",
            createdAt = LocalDate.now(),
            roles = setOf(Role(UUID.randomUUID(), "ROLE_USER"))
        )
        val otherUserId = UUID.randomUUID()
        val otherUser = User(
            id = otherUserId,
            firstName = "Other",
            lastName = "User",
            email = "taken@example.com",
            phoneNumber = "9999999999",
            passwordHash = "hash",
            createdAt = LocalDate.now(),
            roles = setOf()
        )
        val command = UpdateUserCommand(
            id = userId,
            firstName = null,
            lastName = null,
            email = "taken@example.com",
            phoneNumber = null,
            password = null
        )

        whenever(userRepository.findById(userId)).thenReturn(existingUser)
        whenever(userRepository.findByEmail("taken@example.com")).thenReturn(otherUser)

        // Act & Assert
        assertThrows<UserWithEmailAlreadyExistsException> {
            useCase.execute(command)
        }
    }

    @Test
    fun `execute should throw exception when new phone already exists for different user`() {
        // Arrange
        val userId = UUID.randomUUID()
        val existingUser = User(
            id = userId,
            firstName = "John",
            lastName = "Doe",
            email = "john@example.com",
            phoneNumber = "1234567890",
            passwordHash = "hash",
            createdAt = LocalDate.now(),
            roles = setOf(Role(UUID.randomUUID(), "ROLE_USER"))
        )
        val otherUserId = UUID.randomUUID()
        val otherUser = User(
            id = otherUserId,
            firstName = "Other",
            lastName = "User",
            email = "other@example.com",
            phoneNumber = "5555555555",
            passwordHash = "hash",
            createdAt = LocalDate.now(),
            roles = setOf()
        )
        val command = UpdateUserCommand(
            id = userId,
            firstName = null,
            lastName = null,
            email = null,
            phoneNumber = "5555555555",
            password = null
        )

        whenever(userRepository.findById(userId)).thenReturn(existingUser)
        whenever(userRepository.findByPhoneNumber("5555555555")).thenReturn(otherUser)

        // Act & Assert
        assertThrows<UserWithPhoneAlreadyExistsException> {
            useCase.execute(command)
        }
    }

    @Test
    fun execute_should_allow_updating_email_to_same_email() {
        // Arrange
        val userId = UUID.randomUUID()
        val existingUser = User(
            id = userId,
            firstName = "John",
            lastName = "Doe",
            email = "john@example.com",
            phoneNumber = "1234567890",
            passwordHash = "hash",
            createdAt = LocalDate.now(),
            roles = setOf(Role(UUID.randomUUID(), "ROLE_USER"))
        )
        val command = UpdateUserCommand(
            id = userId,
            firstName = null,
            lastName = null,
            email = "john@example.com",
            phoneNumber = null,
            password = null
        )

        whenever(userRepository.findById(userId)).thenReturn(existingUser)
        whenever(userRepository.save(org.mockito.kotlin.any())).thenReturn(existingUser)

        // Act
        val result = useCase.execute(command)

        // Assert
        assertEquals(existingUser.email, result.email)
    }
}

