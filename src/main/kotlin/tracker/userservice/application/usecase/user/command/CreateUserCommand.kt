package tracker.userservice.application.usecase.user.command

data class CreateUserCommand(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val email: String,
    val password: String
)