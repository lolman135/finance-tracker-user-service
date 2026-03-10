package tracker.userservice.application.usecase.user

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.transaction.annotation.Transactional
import tracker.userservice.application.exception.user.UserWithEmailAlreadyExistsException
import tracker.userservice.application.exception.user.UserWithNameAlreadyExistsException
import tracker.userservice.application.exception.user.UserWithPhoneAlreadyExistsException
import tracker.userservice.application.usecase.UseCase
import tracker.userservice.application.usecase.role.DeleteRoleByIdUseCase
import tracker.userservice.application.usecase.role.GetDefaultRoleUseCase
import tracker.userservice.application.usecase.user.command.CreateUserCommand
import tracker.userservice.domain.user.User
import tracker.userservice.domain.user.UserRepository
import java.time.LocalDate
import java.util.UUID

// TODO: Uncomment after adding implementation of repository
//@Service
class CreateUserUseCase(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val getDefaultRoleUseCase: GetDefaultRoleUseCase
) : UseCase<CreateUserCommand, User>{

//    @Transactional
    override fun execute(inboundCommand: CreateUserCommand): User {
        if (userRepository.existsByEmail(inboundCommand.email))
            throw UserWithEmailAlreadyExistsException()

        if (userRepository.existsByPhoneNumber(inboundCommand.phoneNumber))
            throw UserWithPhoneAlreadyExistsException()

        val createdUser = User(
            id = UUID.randomUUID(),
            firstName = inboundCommand.firstName,
            lastName = inboundCommand.lastName,
            email = inboundCommand.email,
            phoneNumber = inboundCommand.phoneNumber,
            passwordHash = passwordEncoder.encode(inboundCommand.password)!!,
            createdAt = LocalDate.now(),
            roles = setOf(getDefaultRoleUseCase.execute(Unit))
        )

        return userRepository.save(createdUser)
    }
}