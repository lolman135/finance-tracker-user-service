package tracker.userservice.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean
import java.io.Serializable
import java.util.Optional

@NoRepositoryBean
interface NaturalIdRepository<T: Any, NID : Serializable> : JpaRepository<T, NID> {
    fun findByNaturalId(naturalId: NID): Optional<T>
    fun deleteByNaturalId(naturalId: NID)
    fun existsByNaturalId(naturalId: NID): Boolean
}