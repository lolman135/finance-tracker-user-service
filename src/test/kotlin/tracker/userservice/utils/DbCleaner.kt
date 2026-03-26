package tracker.userservice.utils

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class DbCleaner {
    @PersistenceContext
    private lateinit var em: EntityManager

    @Transactional
    fun clean() {
        em.createNativeQuery("""
            TRUNCATE TABLE 
                users,
                roles,
                user_role
            RESTART IDENTITY CASCADE
        """.trimIndent()).executeUpdate()
    }
}