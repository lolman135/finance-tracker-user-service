package tracker.userservice.application.usecase.user

import tracker.userservice.application.usecase.UseCase
import tracker.userservice.domain.user.UserRepository
import java.util.UUID

// TODO: Uncomment after adding implementation of repository
//@Service
class DeleteUserByIdUseCase(private val userRepository: UserRepository) : UseCase<UUID, Unit> {
    override fun execute(id: UUID) {
        userRepository.deleteById(id)
    }
}