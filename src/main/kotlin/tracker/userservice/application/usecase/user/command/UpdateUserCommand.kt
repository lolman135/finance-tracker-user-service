package tracker.userservice.application.usecase.user.command

import java.util.UUID

data class UpdateUserCommand(
    val id: UUID,
    val firstName: String?,
    val lastName: String?,
    val phoneNumber: String?,
    val email: String?,
    val password: String?
)
