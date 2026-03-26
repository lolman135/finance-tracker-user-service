package tracker.userservice.application.usecase.user


import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tracker.userservice.application.common.toRoleFormat
import tracker.userservice.application.exception.role.RoleNotFoundException
import tracker.userservice.application.exception.user.UserNotFoundException
import tracker.userservice.application.usecase.UseCase
import tracker.userservice.application.usecase.user.command.AddRoleCommand
import tracker.userservice.domain.role.RoleRepository
import tracker.userservice.domain.user.User
import tracker.userservice.domain.user.UserRepository

@Service
class AddRoleUseCase(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository
) : UseCase<AddRoleCommand, User>{

    @Transactional
    override fun execute(inboundCommand: AddRoleCommand): User {
        val user = userRepository.findById(inboundCommand.userId) ?: throw UserNotFoundException()
        val roleToAdd = roleRepository.findByName(inboundCommand.roleName.toRoleFormat()) ?: throw RoleNotFoundException()
        val grantedUser = user.addRole(roleToAdd)
        return userRepository.save(grantedUser)
    }
}