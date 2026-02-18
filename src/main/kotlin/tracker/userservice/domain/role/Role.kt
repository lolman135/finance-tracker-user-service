package tracker.userservice.domain.role

import java.util.UUID

data class Role(
    val id: UUID,
    val name: String
) {
    fun rename(newName: String): Role{
        require(newName.isNotBlank()){"Name cannot be empty"}
        return copy(name = newName)
    }
}