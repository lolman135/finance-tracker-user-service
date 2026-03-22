package tracker.userservice.infrastructure.mapper.jpa

import org.springframework.stereotype.Component
import tracker.userservice.domain.role.Role
import tracker.userservice.domain.user.User
import tracker.userservice.persistence.entity.RoleEntity
import tracker.userservice.persistence.entity.UserEntity

@Component
class UserJpaMapperImpl : UserJpaMapper {

    override fun toEntity(domain: User) = UserEntity(
            publicId = domain.id,
            firstName = domain.firstName,
            lastName = domain.lastName,
            phoneNumber = domain.phoneNumber,
            email = domain.email,
            createdAt = domain.createdAt,
            passwordHash = domain.passwordHash,
            roles = domain.roles.map { RoleEntity(publicId = it.id, name = it.name) }.toMutableSet()
        )


    override fun toDomain(entity: UserEntity) = User(
        id = entity.publicId,
        firstName = entity.firstName,
        lastName = entity.lastName,
        phoneNumber = entity.phoneNumber,
        email = entity.email,
        createdAt = entity.createdAt,
        passwordHash = entity.passwordHash,
        roles = entity.roles?.map { Role(id = it.publicId, name = it.name) }?.toMutableSet() ?: mutableSetOf()
    )
}