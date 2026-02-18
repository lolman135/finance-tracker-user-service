package tracker.userservice.domain.role

import tracker.userservice.domain.BaseDomainRepository
import java.util.UUID

interface RoleRepository : BaseDomainRepository<UUID, Role> {
    fun findByName(name: String): Role?
}