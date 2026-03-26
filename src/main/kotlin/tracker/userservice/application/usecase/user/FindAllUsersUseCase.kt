package tracker.userservice.application.usecase.user

import org.springframework.stereotype.Service
import tracker.userservice.application.usecase.UseCase
import tracker.userservice.application.usecase.user.command.FindAllUserPageableCommand
import tracker.userservice.domain.PageRequest
import tracker.userservice.domain.PageResponse
import tracker.userservice.domain.user.User
import tracker.userservice.domain.user.UserRepository

@Service
class FindAllUsersUseCase(private val userRepository: UserRepository)
    : UseCase<FindAllUserPageableCommand, PageResponse<User>> {

    override fun execute(inboundCommand: FindAllUserPageableCommand) =
        userRepository.findAllByPages(PageRequest(inboundCommand.pageAmount, inboundCommand.size))
}