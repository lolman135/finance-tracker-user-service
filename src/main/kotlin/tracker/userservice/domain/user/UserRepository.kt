package tracker.userservice.domain.user

import tracker.userservice.domain.BaseDomainRepository
import java.util.UUID

interface UserRepository : BaseDomainRepository<UUID, User> {

    fun findByFullName(firstName: String, lastName: String): User?
    fun findByEmail(email: String): User?
    fun findByPhoneNumber(phoneNumber: String): User?
}