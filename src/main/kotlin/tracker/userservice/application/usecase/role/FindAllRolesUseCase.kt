package tracker.userservice.application.usecase.role

import org.springframework.stereotype.Service
import tracker.userservice.application.usecase.UseCase
import tracker.userservice.domain.role.Role
import tracker.userservice.domain.role.RoleRepository

@Service
class FindAllRolesUseCase(private val roleRepository: RoleRepository) : UseCase<Unit, List<Role>>{
    override fun execute(inboundCommand: Unit): List<Role> {
        return roleRepository.findAll()
    }
}