package tracker.userservice.application.usecase.auth

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import tracker.userservice.application.JwtProvider
import tracker.userservice.application.exception.InvalidCredentialsException
import tracker.userservice.application.usecase.UseCase
import tracker.userservice.application.usecase.auth.command.LoginUserCommand
import tracker.userservice.application.usecase.user.FIndUserByEmailUseCase

@Service
class LoginUseCase(
    private val fIndUserByEmailUseCase: FIndUserByEmailUseCase,
    private val authenticationManager: AuthenticationManager,
    private val jwtProvider: JwtProvider
) : UseCase<LoginUserCommand,  String>{

    override fun execute(inboundCommand: LoginUserCommand): String {
        try {
            val authenticate = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(inboundCommand.email, inboundCommand.password)
            )

            SecurityContextHolder.getContext().authentication = authenticate
            val authorizedUser = fIndUserByEmailUseCase.execute(inboundCommand.email)
            return jwtProvider.generateToken(authorizedUser.id)
        } catch (ex: Exception) {
            throw InvalidCredentialsException("Invalid email or password")
        }
    }
}