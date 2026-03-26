package tracker.userservice.application.usecase.role

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tracker.userservice.application.common.toRoleFormat
import tracker.userservice.application.exception.role.RoleAlreadyExistsException
import tracker.userservice.application.usecase.UseCase
import tracker.userservice.application.usecase.role.comands.CreateRoleCommand
import tracker.userservice.domain.role.Role
import tracker.userservice.domain.role.RoleRepository
import java.util.UUID

@Service
class CreateRoleUseCase(private val roleRepository: RoleRepository) : UseCase<CreateRoleCommand, Role> {

    @Transactional
    override fun execute(inboundCommand: CreateRoleCommand): Role {
        if (roleRepository.existsByName(inboundCommand.name.toRoleFormat()))
            throw RoleAlreadyExistsException()

        val newRole = Role(id = UUID.randomUUID(), name = inboundCommand.name.toRoleFormat())
        return roleRepository.save(newRole)
    }
}