package tracker.userservice.application.usecase.user

import org.springframework.stereotype.Service
import tracker.userservice.application.exception.user.UserNotFoundException
import tracker.userservice.application.usecase.UseCase
import tracker.userservice.domain.user.User
import tracker.userservice.domain.user.UserRepository

@Service
class FIndUserByEmailUseCase(private val userRepository: UserRepository) : UseCase<String, User>{

    override fun execute(email: String) = userRepository.findByEmail(email) ?: throw UserNotFoundException()
}