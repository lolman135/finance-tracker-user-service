package tracker.userservice.application.usecase.user.command

import java.util.UUID

data class AddRoleCommand(
    val userId: UUID,
    val roleName: String
)