package tracker.userservice.application.usecase.role

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tracker.userservice.application.common.toRoleFormat
import tracker.userservice.application.exception.role.RoleNotFoundException
import tracker.userservice.application.usecase.UseCase
import tracker.userservice.application.usecase.role.comands.UpdateRoleCommand
import tracker.userservice.domain.role.Role
import tracker.userservice.domain.role.RoleRepository

@Service
class UpdateRoleByIdUseCase(private val roleRepository: RoleRepository) : UseCase<UpdateRoleCommand, Role> {

    @Transactional
    override fun execute(inboundCommand: UpdateRoleCommand): Role {
        val existedRole = roleRepository.findById(inboundCommand.id) ?: throw RoleNotFoundException()
        val updatedRole = existedRole.rename(inboundCommand.newName.toRoleFormat())
        return roleRepository.save(updatedRole)
    }
}