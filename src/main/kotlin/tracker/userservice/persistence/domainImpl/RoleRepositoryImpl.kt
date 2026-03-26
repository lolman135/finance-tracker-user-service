package tracker.userservice.persistence.domainImpl

import org.springframework.stereotype.Repository
import tracker.userservice.domain.PageRequest
import tracker.userservice.domain.PageResponse
import tracker.userservice.domain.role.Role
import tracker.userservice.domain.role.RoleRepository
import tracker.userservice.infrastructure.mapper.jpa.RoleJpaMapper
import tracker.userservice.persistence.RoleJpaRepository
import java.util.UUID
import kotlin.jvm.optionals.getOrNull
import org.springframework.data.domain.PageRequest as SpringPageRequest

@Repository
class RoleRepositoryImpl(
    private val jpaRepository: RoleJpaRepository,
    private val mapper: RoleJpaMapper
) : RoleRepository{

    override fun findByName(name: String) =
        jpaRepository.findRoleEntityByName(name)
            .map { mapper.toDomain(it) }
            .getOrNull()

    override fun existsByName(name: String) = jpaRepository.existsRoleEntityByName(name)

    override fun save(domain: Role): Role {
        val existingEntity = jpaRepository.findByNaturalId(domain.id)
            .orElse(null)

        return if (existingEntity == null) {
            val newEntity = mapper.toEntity(domain)
            mapper.toDomain(jpaRepository.save(newEntity))
        } else {
            existingEntity.name = domain.name
            mapper.toDomain(existingEntity)
        }
    }

    override fun findAll() =
        jpaRepository.findAll()
            .map { mapper.toDomain(it) }

    override fun findById(id: UUID) =
        jpaRepository.findByNaturalId(id)
            .map { mapper.toDomain(it) }
            .getOrNull()

    override fun deleteById(id: UUID) {
        jpaRepository.deleteByNaturalId(id)
    }

    override fun findAllByPages(request: PageRequest): PageResponse<Role> {
        val pageRequest = SpringPageRequest.of(request.pageAmount,request.size)

        val pagedEntities = jpaRepository.findAll(pageRequest)

        return PageResponse(
            items = pagedEntities.content.map { mapper.toDomain(it) },
            total = pagedEntities.totalElements,
            page = pagedEntities.number,
            size = pagedEntities.size
        )
    }
}