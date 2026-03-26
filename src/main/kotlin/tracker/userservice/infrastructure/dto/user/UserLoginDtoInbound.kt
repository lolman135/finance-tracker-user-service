package tracker.userservice.infrastructure.dto.user

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern

data class UserLoginDtoInbound(
    @field:Email(message = "Invalid email formant")
    val email: String,

    @field:Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$",
        message = "Minimum eight characters, at least one uppercase letter, one lowercase letter and one number"
    )
    val password: String
)