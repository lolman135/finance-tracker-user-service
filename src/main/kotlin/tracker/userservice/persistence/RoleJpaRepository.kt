package tracker.userservice.persistence

import org.springframework.stereotype.Repository
import tracker.userservice.persistence.entity.RoleEntity
import java.util.Optional
import java.util.UUID

@Repository
interface RoleJpaRepository : NaturalIdRepository<RoleEntity, UUID>{

    fun existsRoleEntityByName(name: String): Boolean
    fun findRoleEntityByName(name: String): Optional<RoleEntity>
    fun findAllByPublicIdIn(publicIds: List<UUID>): List<RoleEntity>
}