package tracker.userservice.application.usecase.user.command

data class FindUserByFullNameCommand(
    val firstName: String,
    val lastName: String
)