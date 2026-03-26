package tracker.userservice.application.usecase.role

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import tracker.userservice.domain.role.RoleRepository
import java.util.UUID

class DeleteRoleByIdUseCaseTest {

    private lateinit var roleRepository: RoleRepository
    private lateinit var useCase: DeleteRoleByIdUseCase

    @BeforeEach
    fun setup() {
        roleRepository = mock()
        useCase = DeleteRoleByIdUseCase(roleRepository)
    }

    @Test
    fun execute_should_delete_role_by_id() {
        // Arrange
        val roleId = UUID.randomUUID()

        // Act
        useCase.execute(roleId)

        // Assert
        verify(roleRepository).deleteById(roleId)
    }
}

