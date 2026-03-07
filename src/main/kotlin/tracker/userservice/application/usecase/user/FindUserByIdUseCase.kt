package tracker.userservice.application.usecase.user

import tracker.userservice.application.exception.user.UserNotFoundException
import tracker.userservice.application.usecase.UseCase
import tracker.userservice.domain.user.User
import tracker.userservice.domain.user.UserRepository
import java.util.UUID

// TODO: Uncomment after adding implementation of repository
//@Service
class FindUserByIdUseCase(private val userRepository: UserRepository) : UseCase<UUID, User> {
    override fun execute(id: UUID) = userRepository.findById(id) ?: throw UserNotFoundException()
}