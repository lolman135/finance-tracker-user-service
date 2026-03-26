package tracker.userservice.infrastructure.dto.user

import RoleDtoOutbound
import java.time.LocalDate
import java.util.UUID

data class UserDtoOutbound(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val email: String,
    val createdAt: LocalDate,
    val roles: List<RoleDtoOutbound>
)