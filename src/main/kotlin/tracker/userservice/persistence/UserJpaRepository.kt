package tracker.userservice.persistence

import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import tracker.userservice.persistence.entity.UserEntity
import java.util.Optional
import java.util.UUID

@Repository
interface UserJpaRepository : NaturalIdRepository<UserEntity, UUID>{

    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.roles WHERE u.email = :email")
    fun findUserEntityByEmail(email: String): Optional<UserEntity>

    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.roles WHERE u.phoneNumber = :phoneNumber")
    fun findUserEntityByPhoneNumber(phoneNumber: String): Optional<UserEntity>

    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.roles WHERE u.firstName = :firstName AND u.lastName = :lastName")
    fun findAllByFullName(firstName: String, lastName: String): List<UserEntity>

    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.roles")
    fun findAllWithRoles(): List<UserEntity>

    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.roles WHERE u.publicId = :publicId")
    fun findByPublicIdWithRoles(publicId: UUID): Optional<UserEntity>

    @Query(value = "SELECT u FROM UserEntity u LEFT JOIN FETCH u.roles WHERE u.id IN :ids",)
    fun findAllWithRolesByIds(ids: List<Long>): List<UserEntity>

    fun existsUserEntitiesByEmail(email: String): Boolean
    fun existsUserEntitiesByPhoneNumber(phoneNumber: String): Boolean
}