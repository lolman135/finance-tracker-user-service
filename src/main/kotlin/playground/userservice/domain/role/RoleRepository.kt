package playground.userservice.domain.role

import playground.userservice.domain.BaseDomainRepository
import java.util.UUID

interface RoleRepository : BaseDomainRepository<UUID, Role> {
    fun findByName(name: String): Role?
}