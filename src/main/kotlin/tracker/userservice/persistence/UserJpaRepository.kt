package tracker.userservice.persistence

import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import tracker.userservice.persistence.entity.UserEntity
import java.util.Optional
import java.util.UUID

@Repository
interface UserJpaRepository : NaturalIdRepository<UserEntity, UUID>{

    @Query("SELECT u FROM UserEntity u WHERE u.firstName = :firstName AND u.lastName = :lastName")
    fun findByFullName(firstName: String, lastName: String): Optional<UserEntity>
    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.roles WHERE u.email = :email")
    fun findUserEntityByEmail(email: String): Optional<UserEntity>
    fun findUserEntityByPhoneNumber(email: String): Optional<UserEntity>

    fun existsUserEntitiesByEmail(email: String): Boolean
    fun existsUserEntitiesByPhoneNumber(phoneNumber: String): Boolean
}