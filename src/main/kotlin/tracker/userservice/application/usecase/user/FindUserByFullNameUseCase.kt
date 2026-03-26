package tracker.userservice.application.usecase.user

import org.springframework.stereotype.Service
import tracker.userservice.application.exception.user.UserNotFoundException
import tracker.userservice.application.usecase.UseCase
import tracker.userservice.application.usecase.user.command.FindUserByFullNameCommand
import tracker.userservice.domain.user.User
import tracker.userservice.domain.user.UserRepository

@Service
class FindUserByFullNameUseCase(private val userRepository: UserRepository)
    : UseCase<FindUserByFullNameCommand, List<User>> {

    override fun execute(inboundCommand: FindUserByFullNameCommand) =
        userRepository.findByFullName(inboundCommand.firstName, inboundCommand.lastName)
}