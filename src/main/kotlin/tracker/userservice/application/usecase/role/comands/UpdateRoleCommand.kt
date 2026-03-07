package tracker.userservice.application.usecase.role.comands

import java.util.UUID

data class UpdateRoleCommand(
    val id: UUID,
    val newName: String
)