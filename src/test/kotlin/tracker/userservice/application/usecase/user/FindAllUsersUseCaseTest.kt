package tracker.userservice.application.usecase.user

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import tracker.userservice.application.usecase.user.command.FindAllUserPageableCommand
import tracker.userservice.domain.PageRequest
import tracker.userservice.domain.PageResponse
import tracker.userservice.domain.role.Role
import tracker.userservice.domain.user.User
import tracker.userservice.domain.user.UserRepository
import java.time.LocalDate
import java.util.UUID
import kotlin.test.assertEquals

class FindAllUsersUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var useCase: FindAllUsersUseCase

    @BeforeEach
    fun setup() {
        userRepository = mock()
        useCase = FindAllUsersUseCase(userRepository)
    }

    @Test
    fun ensurePaginatedUsersReturned() {
        val user1 = User(
            id = UUID.randomUUID(),
            firstName = "John",
            lastName = "Doe",
            email = "john@example.com",
            phoneNumber = "1234567890",
            passwordHash = "hash1",
            createdAt = LocalDate.now(),
            roles = setOf(Role(UUID.randomUUID(), "ROLE_USER"))
        )
        val user2 = User(
            id = UUID.randomUUID(),
            firstName = "Jane",
            lastName = "Smith",
            email = "jane@example.com",
            phoneNumber = "0987654321",
            passwordHash = "hash2",
            createdAt = LocalDate.now(),
            roles = setOf(Role(UUID.randomUUID(), "ROLE_USER"))
        )
        val command = FindAllUserPageableCommand(pageAmount = 0, size = 10)
        val pageResponse = PageResponse(
            items = listOf(user1, user2),
            page = 0,
            size = 10,
            total = 2
        )
        whenever(userRepository.findAllByPages(PageRequest(0, 10))).thenReturn(pageResponse)

        val result = useCase.execute(command)

        assertEquals(2, result.items.size)
        assertEquals("John", result.items[0].firstName)
        assertEquals("Jane", result.items[1].firstName)
        assertEquals(0, result.page)
        assertEquals(10, result.size)
        assertEquals(2, result.total)
    }

    @Test
    fun ensureEmptyListReturnedWhenNoUsers() {
        val command = FindAllUserPageableCommand(pageAmount = 0, size = 10)
        val pageResponse = PageResponse<User>(
            items = emptyList(),
            total = 0,
            page = 0,
            size = 10
        )
        whenever(userRepository.findAllByPages(PageRequest(0, 10))).thenReturn(pageResponse)

        val result = useCase.execute(command)

        assertEquals(0, result.items.size)
        assertEquals(0, result.total)
    }
}

