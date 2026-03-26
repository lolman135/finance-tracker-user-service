package tracker.userservice.persistence.domainImpl

import org.springframework.stereotype.Repository
import tracker.userservice.domain.PageRequest
import tracker.userservice.domain.PageResponse
import tracker.userservice.domain.user.User
import tracker.userservice.domain.user.UserRepository
import tracker.userservice.infrastructure.mapper.jpa.UserJpaMapper
import tracker.userservice.persistence.RoleJpaRepository
import tracker.userservice.persistence.UserJpaRepository
import java.util.UUID
import kotlin.jvm.optionals.getOrNull
import org.springframework.data.domain.PageRequest as SpringPageRequest

@Repository
class UserRepositoryImpl(
    private val jpaRepository: UserJpaRepository,
    private val roleJpaRepository: RoleJpaRepository,
    private val mapper: UserJpaMapper
) : UserRepository {

    override fun findByFullName(firstName: String, lastName: String): List<User> =
        jpaRepository.findAllByFullName(firstName, lastName)
            .map { mapper.toDomain(it) }

    override fun findByEmail(email: String): User? =
        jpaRepository.findUserEntityByEmail(email)
            .map { mapper.toDomain(it) }
            .getOrNull()

    override fun findByPhoneNumber(phoneNumber: String): User? =
        jpaRepository.findUserEntityByPhoneNumber(phoneNumber)
            .map { mapper.toDomain(it) }
            .getOrNull()

    override fun existsByEmail(email: String): Boolean = jpaRepository.existsUserEntitiesByEmail(email)

    override fun existsByPhoneNumber(phoneNumber: String): Boolean =
        jpaRepository.existsUserEntitiesByPhoneNumber(phoneNumber)

    override fun save(domain: User): User {
        val existingEntity = jpaRepository.findByNaturalId(domain.id)
            .getOrNull()

        return if (existingEntity == null) {
            val newEntity = mapper.toEntity(domain)
            mapper.toDomain(jpaRepository.save(newEntity))
        } else {
            existingEntity.apply {
                firstName = domain.firstName
                lastName = domain.lastName
                email = domain.email
                phoneNumber = domain.phoneNumber
                passwordHash = domain.passwordHash

                val roleIds = domain.roles.map { it.id }
                val managedRoles = roleJpaRepository.findAllByPublicIdIn(roleIds)

                roles?.clear()
                roles?.addAll(managedRoles)
            }
            mapper.toDomain(existingEntity)
        }
    }

    override fun findAll(): List<User> =
        jpaRepository.findAllWithRoles()
            .map { mapper.toDomain(it) }

    override fun findById(id: UUID): User? =
        jpaRepository.findByPublicIdWithRoles(id)
            .map { mapper.toDomain(it) }
            .getOrNull()

    override fun deleteById(id: UUID) {
        jpaRepository.deleteByNaturalId(id)
    }

    override fun findAllByPages(request: PageRequest): PageResponse<User> {
        val pageRequest = SpringPageRequest.of(request.pageAmount, request.size)
        val pagedEntities = jpaRepository.findAll(pageRequest)

        val ids = pagedEntities.content.map { it.id!! }
        val entitiesWithRoles = jpaRepository.findAllWithRolesByIds(ids)
            .associateBy { it.id }

        return PageResponse(
            items = pagedEntities.content
                .map { entitiesWithRoles[it.id]!! }
                .map { mapper.toDomain(it) },
            total = pagedEntities.totalElements,
            page = pagedEntities.number,
            size = pagedEntities.size
        )
    }
}