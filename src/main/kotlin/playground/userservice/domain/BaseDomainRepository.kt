package playground.userservice.domain

interface BaseDomainRepository<ID, T> {

    fun save(domain: T): T
    fun findAll(): List<T>
    fun findById(id: ID): T?
    fun deleteById(id: ID)
}