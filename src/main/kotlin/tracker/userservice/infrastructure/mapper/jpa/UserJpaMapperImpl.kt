package tracker.userservice.infrastructure.mapper.jpa

import org.springframework.stereotype.Component
import tracker.userservice.domain.role.Role
import tracker.userservice.domain.user.User
import tracker.userservice.persistence.RoleJpaRepository
import tracker.userservice.persistence.entity.RoleEntity
import tracker.userservice.persistence.entity.UserEntity
import tracker.userservice.persistence.exception.RoleJpaNotFoundException

@Component
class UserJpaMapperImpl(
    private val roleMapper: RoleJpaMapper,
    private val roleJpaRepository: RoleJpaRepository
) : UserJpaMapper {

    override fun toEntity(domain: User): UserEntity {

        val roleIds = domain.roles.map { it.id }
        val existingRoles = roleJpaRepository
            .findAllByPublicIdIn(roleIds)
            .associateBy { it.publicId }

        return UserEntity(
            publicId = domain.id,
            firstName = domain.firstName,
            lastName = domain.lastName,
            phoneNumber = domain.phoneNumber,
            email = domain.email,
            createdAt = domain.createdAt,
            passwordHash = domain.passwordHash,
            roles = domain.roles.map { role ->
                existingRoles[role.id]
                    ?: throw RoleJpaNotFoundException()
            }.toMutableSet()
        )
    }

    override fun toDomain(entity: UserEntity): User = User(
        id = entity.publicId,
        firstName = entity.firstName,
        lastName = entity.lastName,
        phoneNumber = entity.phoneNumber,
        email = entity.email,
        createdAt = entity.createdAt,
        passwordHash = entity.passwordHash,
        roles = entity.roles?.map { roleMapper.toDomain(it) }?.toSet() ?: emptySet()
    )
}