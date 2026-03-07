package tracker.userservice.application.usecase.role

import tracker.userservice.application.exception.role.RoleNotFoundException
import tracker.userservice.application.usecase.UseCase
import tracker.userservice.domain.role.Role
import tracker.userservice.domain.role.RoleRepository

// TODO: Uncomment after adding implementation of repository
//@Service
class GetDefaultRoleUseCase(private val roleRepository: RoleRepository) : UseCase<Unit, Role> {
    override fun execute(inboundCommand: Unit): Role =
        roleRepository.findByName("ROLE_USER") ?: throw RoleNotFoundException()
}