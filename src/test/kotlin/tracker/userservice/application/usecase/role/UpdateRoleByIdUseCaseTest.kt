package tracker.userservice.application.usecase.role

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import tracker.userservice.application.exception.role.RoleNotFoundException
import tracker.userservice.application.usecase.role.comands.UpdateRoleCommand
import tracker.userservice.domain.role.Role
import tracker.userservice.domain.role.RoleRepository
import java.util.UUID
import kotlin.test.assertEquals

class UpdateRoleByIdUseCaseTest {

    private lateinit var roleRepository: RoleRepository
    private lateinit var useCase: UpdateRoleByIdUseCase

    @BeforeEach
    fun setup() {
        roleRepository = mock()
        useCase = UpdateRoleByIdUseCase(roleRepository)
    }

    @Test
    fun `execute should update role successfully`() {
        // Arrange
        val roleId = UUID.randomUUID()
        val existingRole = Role(roleId, "ROLE_ADMIN")
        val command = UpdateRoleCommand(roleId, "ROLE_SUPER_ADMIN")
        val updatedRole = existingRole.rename("ROLE_SUPER_ADMIN")

        whenever(roleRepository.findById(roleId)).thenReturn(existingRole)
        whenever(roleRepository.save(org.mockito.kotlin.any())).thenReturn(updatedRole)

        // Act
        val result = useCase.execute(command)

        // Assert
        assertEquals("ROLE_SUPER_ADMIN", result.name)
    }

    @Test
    fun execute_should_throw_exception_when_role_not_found() {
        // Arrange
        val roleId = UUID.randomUUID()
        val command = UpdateRoleCommand(roleId, "ROLE_NEW")

        whenever(roleRepository.findById(roleId)).thenReturn(null)

        // Act & Assert
        assertThrows<RoleNotFoundException> {
            useCase.execute(command)
        }
    }
}

