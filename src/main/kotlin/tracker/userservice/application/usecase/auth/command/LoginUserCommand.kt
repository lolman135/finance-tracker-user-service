package tracker.userservice.application.usecase.auth.command

data class LoginUserCommand(
    val email: String,
    val password: String
)