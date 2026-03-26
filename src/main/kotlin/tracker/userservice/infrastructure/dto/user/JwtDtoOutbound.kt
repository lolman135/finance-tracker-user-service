package tracker.userservice.infrastructure.dto.user

data class JwtDtoOutbound(
    val type: String,
    val token: String
)