package tracker.userservice.application.usecase.role

import org.springframework.stereotype.Service
import tracker.userservice.application.usecase.UseCase
import tracker.userservice.domain.role.RoleRepository
import java.util.UUID

@Service
class DeleteRoleByIdUseCase(private val roleRepository: RoleRepository) : UseCase<UUID, Unit>{

    override fun execute(id: UUID) {
        roleRepository.deleteById(id)
    }
}