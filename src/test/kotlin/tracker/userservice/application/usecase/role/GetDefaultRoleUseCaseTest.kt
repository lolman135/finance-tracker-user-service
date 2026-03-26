package tracker.userservice.application.usecase.role

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import tracker.userservice.application.exception.role.RoleNotFoundException
import tracker.userservice.domain.role.Role
import tracker.userservice.domain.role.RoleRepository
import java.util.UUID
import kotlin.test.assertEquals

class GetDefaultRoleUseCaseTest {

    private lateinit var roleRepository: RoleRepository
    private lateinit var useCase: GetDefaultRoleUseCase

    @BeforeEach
    fun setup() {
        roleRepository = mock()
        useCase = GetDefaultRoleUseCase(roleRepository)
    }

    @Test
    fun `execute should return default role when found`() {
        // Arrange
        val defaultRole = Role(UUID.randomUUID(), "ROLE_USER")

        whenever(roleRepository.findByName("ROLE_USER")).thenReturn(defaultRole)

        // Act
        val result = useCase.execute(Unit)

        // Assert
        assertEquals("ROLE_USER", result.name)
    }

    @Test
    fun execute_should_throw_exception_when_default_role_not_found() {
        // Arrange
        whenever(roleRepository.findByName("ROLE_USER")).thenReturn(null)

        // Act & Assert
        assertThrows<RoleNotFoundException> {
            useCase.execute(Unit)
        }
    }
}

