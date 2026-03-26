package tracker.userservice.application.usecase.role

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import tracker.userservice.domain.role.Role
import tracker.userservice.domain.role.RoleRepository
import java.util.UUID
import kotlin.test.assertEquals

class FindAllRolesUseCaseTest {

    private lateinit var roleRepository: RoleRepository
    private lateinit var useCase: FindAllRolesUseCase

    @BeforeEach
    fun setup() {
        roleRepository = mock()
        useCase = FindAllRolesUseCase(roleRepository)
    }

    @Test
    fun `execute should return all roles`() {
        // Arrange
        val role1 = Role(UUID.randomUUID(), "ROLE_USER")
        val role2 = Role(UUID.randomUUID(), "ROLE_ADMIN")
        val roles = listOf(role1, role2)

        whenever(roleRepository.findAll()).thenReturn(roles)

        // Act
        val result = useCase.execute(Unit)

        // Assert
        assertEquals(2, result.size)
        assertEquals("ROLE_USER", result[0].name)
        assertEquals("ROLE_ADMIN", result[1].name)
    }

    @Test
    fun execute_should_return_empty_list_when_no_roles() {
        // Arrange
        whenever(roleRepository.findAll()).thenReturn(emptyList())

        // Act
        val result = useCase.execute(Unit)

        // Assert
        assertEquals(0, result.size)
    }
}

