package tracker.userservice.infrastructure.mapper.jpa

import org.springframework.stereotype.Component
import tracker.userservice.domain.role.Role
import tracker.userservice.persistence.entity.RoleEntity

@Component
class RoleJpaMapperImpl : RoleJpaMapper {

    override fun toEntity(domain: Role) = RoleEntity(publicId = domain.id, name = domain.name)
    override fun toDomain(entity: RoleEntity) = Role(id = entity.publicId, name = entity.name)

}