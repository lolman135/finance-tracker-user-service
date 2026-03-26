package tracker.userservice.infrastructure.dto.user

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern

class UserRegisterDtoInbound(
    @field:Pattern(regexp = "^[a-zA-Zа-яА-ЯіІїЇʼєЄ\\d\\s]{2,}$", message = "Invalid name")
    val firstName: String,

    @field:Pattern(regexp = "^[a-zA-Zа-яА-ЯіІїЇʼєЄ\\d\\s]{2,}$", message = "Invalid name")
    val lastName: String,

    @field:Email(message = "Invalid email formant")
    val email: String,

    @field:Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number format")
    val phoneNumber: String,

    @field:Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$",
        message = "Minimum eight characters, at least one uppercase letter, one lowercase letter and one number"
    )
    val password: String
)