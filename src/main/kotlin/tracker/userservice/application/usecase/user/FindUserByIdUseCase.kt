package tracker.userservice.application.usecase.user

import org.springframework.stereotype.Service
import tracker.userservice.application.exception.user.UserNotFoundException
import tracker.userservice.application.usecase.UseCase
import tracker.userservice.domain.user.User
import tracker.userservice.domain.user.UserRepository
import java.util.UUID

@Service
class FindUserByIdUseCase(private val userRepository: UserRepository) : UseCase<UUID, User> {
    override fun execute(id: UUID) = userRepository.findById(id) ?: throw UserNotFoundException()
}