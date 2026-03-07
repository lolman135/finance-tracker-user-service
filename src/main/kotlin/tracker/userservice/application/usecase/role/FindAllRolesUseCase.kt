package tracker.userservice.application.usecase.role

import tracker.userservice.application.usecase.UseCase
import tracker.userservice.application.usecase.role.comands.FindAllRolesPageableCommand
import tracker.userservice.domain.PageRequest
import tracker.userservice.domain.PageResponse
import tracker.userservice.domain.role.Role
import tracker.userservice.domain.role.RoleRepository

// TODO: Uncomment after adding implementation of repository
//@Service
class FindAllRolesUseCase(private val roleRepository: RoleRepository)
    : UseCase<FindAllRolesPageableCommand, PageResponse<Role>>{

    override fun execute(inboundCommand: FindAllRolesPageableCommand): PageResponse<Role> =
        roleRepository.findAllByPages(PageRequest(inboundCommand.pageAmount, inboundCommand.size))

}