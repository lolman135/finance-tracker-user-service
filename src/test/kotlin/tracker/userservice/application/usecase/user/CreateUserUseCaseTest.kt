package tracker.userservice.application.usecase.user

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.security.crypto.password.PasswordEncoder
import tracker.userservice.application.exception.user.UserWithEmailAlreadyExistsException
import tracker.userservice.application.exception.user.UserWithPhoneAlreadyExistsException
import tracker.userservice.application.usecase.role.GetDefaultRoleUseCase
import tracker.userservice.application.usecase.user.command.CreateUserCommand
import tracker.userservice.domain.role.Role
import tracker.userservice.domain.user.User
import tracker.userservice.domain.user.UserRepository
import java.time.LocalDate
import java.util.UUID
import kotlin.test.assertEquals

class CreateUserUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var passwordEncoder: PasswordEncoder
    private lateinit var getDefaultRoleUseCase: GetDefaultRoleUseCase
    private lateinit var useCase: CreateUserUseCase

    @BeforeEach
    fun setup() {
        userRepository = mock()
        passwordEncoder = mock()
        getDefaultRoleUseCase = mock()
        useCase = CreateUserUseCase(userRepository, passwordEncoder, getDefaultRoleUseCase)
    }

    @Test
    fun `execute should create user successfully`() {
        val command = CreateUserCommand(
            firstName = "John",
            lastName = "Doe",
            phoneNumber = "1234567890",
            email = "john@example.com",
            password = "password123"
        )
        val encodedPassword = "encodedPassword123"
        val defaultRole = Role(UUID.randomUUID(), "ROLE_USER")
        val createdUser = User(
            id = UUID.randomUUID(),
            firstName = command.firstName,
            lastName = command.lastName,
            email = command.email,
            phoneNumber = command.phoneNumber,
            passwordHash = encodedPassword,
            createdAt = LocalDate.now(),
            roles = setOf(defaultRole)
        )

        whenever(userRepository.existsByEmail(command.email)).thenReturn(false)
        whenever(userRepository.existsByPhoneNumber(command.phoneNumber)).thenReturn(false)
        whenever(passwordEncoder.encode(command.password)).thenReturn(encodedPassword)
        whenever(getDefaultRoleUseCase.execute(Unit)).thenReturn(defaultRole)
        whenever(userRepository.save(any())).thenReturn(createdUser)

        val result = useCase.execute(command)

        assertEquals(createdUser.id, result.id)
        assertEquals(createdUser.firstName, result.firstName)
        assertEquals(createdUser.email, result.email)
        assertEquals(createdUser.phoneNumber, result.phoneNumber)
        assertEquals(defaultRole, result.roles.first())
    }

    @Test
    fun `execute should throw exception when email already exists`() {
        val command = CreateUserCommand(
            firstName = "John",
            lastName = "Doe",
            phoneNumber = "1234567890",
            email = "existing@example.com",
            password = "password123"
        )
        whenever(userRepository.existsByEmail(command.email)).thenReturn(true)

        assertThrows<UserWithEmailAlreadyExistsException> {
            useCase.execute(command)
        }
    }

    @Test
    fun execute_should_throw_exception_when_phone_number_already_exists() {
        val command = CreateUserCommand(
            firstName = "John",
            lastName = "Doe",
            phoneNumber = "9999999999",
            email = "john@example.com",
            password = "password123"
        )
        whenever(userRepository.existsByEmail(command.email)).thenReturn(false)
        whenever(userRepository.existsByPhoneNumber(command.phoneNumber)).thenReturn(true)

        assertThrows<UserWithPhoneAlreadyExistsException> {
            useCase.execute(command)
        }
    }
}

