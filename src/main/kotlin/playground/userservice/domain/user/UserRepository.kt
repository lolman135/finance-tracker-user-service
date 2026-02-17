package playground.userservice.domain.user

import playground.userservice.domain.BaseDomainRepository
import java.util.UUID

interface UserRepository : BaseDomainRepository<UUID, User> {

    fun findByFullName(firstName: String, lastName: String): User?
    fun findByEmail(email: String)
    fun findByPhoneNumber(phoneNumber: String)
}