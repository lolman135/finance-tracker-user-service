package tracker.userservice.infrastructure.mapper.jpa

import tracker.userservice.domain.role.Role
import tracker.userservice.persistence.entity.RoleEntity

interface RoleJpaMapper : EntityDomainMapper<RoleEntity, Role>{
}