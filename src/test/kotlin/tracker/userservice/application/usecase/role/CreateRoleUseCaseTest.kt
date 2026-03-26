package tracker.userservice.application.usecase.role

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import tracker.userservice.application.common.toRoleFormat
import tracker.userservice.application.exception.role.RoleAlreadyExistsException
import tracker.userservice.application.usecase.role.comands.CreateRoleCommand
import tracker.userservice.domain.role.Role
import tracker.userservice.domain.role.RoleRepository
import java.util.UUID
import kotlin.test.assertEquals

class CreateRoleUseCaseTest {

    private lateinit var roleRepository: RoleRepository
    private lateinit var useCase: CreateRoleUseCase

    @BeforeEach
    fun setup() {
        roleRepository = mock()
        useCase = CreateRoleUseCase(roleRepository)
    }

    @Test
    fun `execute should create role successfully`() {
        // Arrange
        val command = CreateRoleCommand("admin")
        val formattedName = "admin".toRoleFormat()
        val createdRole = Role(UUID.randomUUID(), formattedName)

        whenever(roleRepository.existsByName(formattedName)).thenReturn(false)
        whenever(roleRepository.save(any())).thenReturn(createdRole)

        // Act
        val result = useCase.execute(command)

        // Assert
        assertEquals(formattedName, result.name)
    }

    @Test
    fun execute_should_throw_exception_when_role_already_exists() {
        // Arrange
        val command = CreateRoleCommand("admin")
        val formattedName = "admin".toRoleFormat()

        whenever(roleRepository.existsByName(formattedName)).thenReturn(true)

        assertThrows<RoleAlreadyExistsException> {
            useCase.execute(command)
        }
    }
}

