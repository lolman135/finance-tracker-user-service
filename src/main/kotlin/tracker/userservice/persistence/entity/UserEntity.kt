package tracker.userservice.persistence.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import org.hibernate.annotations.NaturalId
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "users")
class UserEntity(

    @get:Id
    @get:GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    @get:SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq", allocationSize = 50)
    var id: Long?,

    @get:Column(name = "public_id")
    @get:NaturalId
    var publicId: UUID,

    @get:Column(name = "first_name")
    var firstName: String,

    @get:Column(name = "last_name")
    var lastName: String,

    @get:Column(name = "phone_number")
    var phoneNumber: String,

    var email: String,

    @get:Column(name = "password_hash")
    var passwordHash: String,

    @get:Column(name = "created_at")
    var createdAt: LocalDate,

    @get:ManyToMany
    @get:JoinTable(
        name = "user_role",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    var roles: MutableSet<RoleEntity>? = mutableSetOf(),
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserEntity

        return publicId == other.publicId
    }

    override fun hashCode(): Int {
        return publicId.hashCode()
    }
}