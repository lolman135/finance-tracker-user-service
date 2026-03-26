package tracker.userservice.persistence.domainImpl

import org.springframework.stereotype.Repository
import tracker.userservice.domain.PageRequest
import tracker.userservice.domain.PageResponse
import tracker.userservice.domain.user.User
import tracker.userservice.domain.user.UserRepository
import tracker.userservice.infrastructure.mapper.jpa.UserJpaMapper
import tracker.userservice.persistence.UserJpaRepository
import java.util.UUID
import kotlin.jvm.optionals.getOrNull
import org.springframework.data.domain.PageRequest as SpringPageRequest

@Repository
class UserRepositoryImpl(
    private val jpaRepository: UserJpaRepository,
    private val mapper: UserJpaMapper
) : UserRepository {

    override fun findByFullName(firstName: String, lastName: String): User? =
        jpaRepository.findByFullName(firstName, lastName)
            .map { mapper.toDomain(it) }
            .getOrNull()

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
        val entity = mapper.toEntity(domain)
        val savedEntity = jpaRepository.save(entity)
        return mapper.toDomain(savedEntity)
    }

    override fun findAll(): List<User> =
        jpaRepository.findAll()
            .map { mapper.toDomain(it) }

    override fun findById(id: UUID): User? =
        jpaRepository.findByNaturalId(id)
            .map { mapper.toDomain(it) }
            .getOrNull()

    override fun deleteById(id: UUID) {
        jpaRepository.deleteByNaturalId(id)
    }

    override fun findAllByPages(request: PageRequest): PageResponse<User> {
        val pageRequest = SpringPageRequest.of(request.pageAmount, request.size)
        val pagedEntities = jpaRepository.findAll(pageRequest)

        return PageResponse(
            items = pagedEntities.content.map { mapper.toDomain(it) },
            total = pagedEntities.totalElements,
            page = pagedEntities.number,
            size = pagedEntities.size
        )
    }
}