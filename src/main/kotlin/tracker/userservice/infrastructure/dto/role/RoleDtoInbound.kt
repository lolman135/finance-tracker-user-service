package tracker.userservice.infrastructure.dto.role

import jakarta.validation.constraints.NotBlank

data class RoleDtoInbound(
    @field:NotBlank(message = "Name field cannot be empty")
    val name: String
)