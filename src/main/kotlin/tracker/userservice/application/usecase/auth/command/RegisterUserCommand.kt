package tracker.userservice.application.usecase.auth.command

data class RegisterUserCommand(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val email: String,
    val password: String
)