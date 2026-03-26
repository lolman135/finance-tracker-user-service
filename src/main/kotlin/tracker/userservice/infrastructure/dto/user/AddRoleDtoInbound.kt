package tracker.userservice.infrastructure.dto.user

import jakarta.validation.constraints.NotBlank

data class AddRoleDtoInbound(
    @field:NotBlank(message = "Role name cannot be blank")
    val roleName: String
)

