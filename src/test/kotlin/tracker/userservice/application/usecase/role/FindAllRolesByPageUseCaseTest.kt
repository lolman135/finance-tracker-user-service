package tracker.userservice.application.usecase.role

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import tracker.userservice.application.usecase.role.comands.FindAllRolesPageableCommand
import tracker.userservice.domain.PageRequest
import tracker.userservice.domain.PageResponse
import tracker.userservice.domain.role.Role
import tracker.userservice.domain.role.RoleRepository
import java.util.UUID
import kotlin.test.assertEquals

class FindAllRolesByPageUseCaseTest {

    private lateinit var roleRepository: RoleRepository
    private lateinit var useCase: FindAllRolesByPageUseCase

    @BeforeEach
    fun setup() {
        roleRepository = mock()
        useCase = FindAllRolesByPageUseCase(roleRepository)
    }

    @Test
    fun `execute should return paginated roles`() {
        // Arrange
        val role1 = Role(UUID.randomUUID(), "ROLE_USER")
        val role2 = Role(UUID.randomUUID(), "ROLE_ADMIN")
        val command = FindAllRolesPageableCommand(pageAmount = 0, size = 10)
        val pageResponse = PageResponse(
            items = listOf(role1, role2),
            total = 2,
            page = 0,
            size = 10
        )

        whenever(roleRepository.findAllByPages(PageRequest(0, 10))).thenReturn(pageResponse)

        // Act
        val result = useCase.execute(command)

        // Assert
        assertEquals(2, result.items.size)
        assertEquals("ROLE_USER", result.items[0].name)
        assertEquals("ROLE_ADMIN", result.items[1].name)
        assertEquals(0, result.page)
        assertEquals(10, result.size)
        assertEquals(2, result.total)
    }

    @Test
    fun execute_should_return_empty_list_when_no_roles_on_page() {
        // Arrange
        val command = FindAllRolesPageableCommand(pageAmount = 1, size = 10)
        val pageResponse = PageResponse<Role>(
            items = emptyList(),
            total = 0,
            page = 1,
            size = 10
        )

        whenever(roleRepository.findAllByPages(PageRequest(1, 10))).thenReturn(pageResponse)

        // Act
        val result = useCase.execute(command)

        // Assert
        assertEquals(0, result.items.size)
        assertEquals(0, result.total)
    }
}

