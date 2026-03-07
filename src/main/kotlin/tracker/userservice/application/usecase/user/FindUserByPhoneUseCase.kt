package tracker.userservice.application.usecase.user

import tracker.userservice.application.exception.user.UserNotFoundException
import tracker.userservice.application.usecase.UseCase
import tracker.userservice.domain.user.User
import tracker.userservice.domain.user.UserRepository

// TODO: Uncomment after adding implementation of repository
//@Service
class FindUserByPhoneUseCase(private val userRepository: UserRepository) : UseCase<String, User> {
    override fun execute(phoneNumber: String) = userRepository.findByPhoneNumber(phoneNumber)
        ?: throw UserNotFoundException()
}