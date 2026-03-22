package tracker.userservice.persistence.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import org.hibernate.annotations.NaturalId
import java.util.UUID

@Entity
@Table(name = "roles")
class RoleEntity(
    @get:Id
    @get:GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_id_seq")
    @get:SequenceGenerator(name = "role_id_seq", sequenceName = "role_id_seq", allocationSize = 50)
    var id: Long? = null,

    @get:Column(name = "public_id")
    @get:NaturalId
    var publicId: UUID,

    @get:Column(name = "name")
    var name: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RoleEntity

        return publicId == other.publicId
    }

    override fun hashCode(): Int {
        return publicId.hashCode() ?: 0
    }
}