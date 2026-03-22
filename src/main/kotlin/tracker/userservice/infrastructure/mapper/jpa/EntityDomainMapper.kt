package tracker.userservice.infrastructure.mapper.jpa

interface EntityDomainMapper<E, D> {

    fun toEntity(domain: D): E
    fun toDomain(entity: E): D
}