package tracker.userservice.application.usecase.auth

import org.springframework.stereotype.Service
import tracker.userservice.application.JwtProvider
import tracker.userservice.application.common.toCreateCommand
import tracker.userservice.application.usecase.UseCase
import tracker.userservice.application.usecase.auth.command.RegisterUserCommand
import tracker.userservice.application.usecase.user.CreateUserUseCase

@Service
class RegisterUseCase(
    private val jwtProvider: JwtProvider,
    private val createUserUseCase: CreateUserUseCase
) : UseCase<RegisterUserCommand, String> {

    override fun execute(inboundCommand: RegisterUserCommand): String {
        val registeredUser = createUserUseCase.execute(inboundCommand.toCreateCommand())
        return jwtProvider.generateToken(registeredUser.id)
    }
}