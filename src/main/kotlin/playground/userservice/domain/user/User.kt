package playground.userservice.domain.user

import playground.userservice.domain.role.Role
import java.time.LocalDate
import java.util.UUID

data class User(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val email: String,
    val passwordHash: String,
    val createdAt: LocalDate,
    val roles: Set<Role>
) {
    fun changeFirstName(newFirstName: String): User {
        require(newFirstName.isNotBlank()){"First name cannot be empty"}
        return copy(firstName = newFirstName)
    }

    fun changeLastName(newLastName: String): User {
        require(newLastName.isNotBlank()){"Last name cannot be empty"}
        return copy(firstName = newLastName)
    }

    fun changePhoneNumber(newPhoneNumber: String): User {
        require(newPhoneNumber.isNotBlank()){"Phone number cannot be empty"}
        return copy(firstName = newPhoneNumber)
    }

    fun changeEmail(email: String): User {
        require(email.isNotBlank()){"Email cannot be empty"}
        return copy(firstName = email)
    }

    fun changePasswordHash(newPasswordHash: String): User{
        require(passwordHash.isNotBlank()){"Password hash cannot be empty"}
        return copy(passwordHash = newPasswordHash)
    }

    fun addRole(newRole: Role): User{
        return copy(roles = roles + newRole)
    }
}