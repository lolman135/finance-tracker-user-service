package tracker.userservice.application.usecase.user

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import tracker.userservice.domain.user.UserRepository
import java.util.UUID

class DeleteUserByIdUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var useCase: DeleteUserByIdUseCase

    @BeforeEach
    fun setup() {
        userRepository = mock()
        useCase = DeleteUserByIdUseCase(userRepository)
    }

    @Test
    fun execute_should_delete_user_by_id() {
        // Arrange
        val userId = UUID.randomUUID()

        // Act
        useCase.execute(userId)

        // Assert
        verify(userRepository).deleteById(userId)
    }
}

