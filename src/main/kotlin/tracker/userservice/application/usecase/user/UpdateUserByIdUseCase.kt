package tracker.userservice.application.usecase.user

import org.springframework.security.crypto.password.PasswordEncoder
import tracker.userservice.application.exception.user.UserNotFoundException
import tracker.userservice.application.exception.user.UserWithEmailAlreadyExistsException
import tracker.userservice.application.exception.user.UserWithNameAlreadyExistsException
import tracker.userservice.application.exception.user.UserWithPhoneAlreadyExistsException
import tracker.userservice.application.usecase.UseCase
import tracker.userservice.application.usecase.user.command.UpdateUserCommand
import tracker.userservice.domain.user.User
import tracker.userservice.domain.user.UserRepository

// TODO: Uncomment after adding implementation of repository
//@Service
class UpdateUserByIdUseCase(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UseCase<UpdateUserCommand, User> {

//    @Transactional
    override fun execute(inboundCommand: UpdateUserCommand): User {
        val existedUser = userRepository.findById(inboundCommand.id) ?: throw UserNotFoundException()

        val newFirstName = inboundCommand.firstName ?: existedUser.firstName
        val newLastName = inboundCommand.lastName ?: existedUser.lastName
        val newEmail = inboundCommand.email ?: existedUser.email
        val newPhoneNumber = inboundCommand.phoneNumber ?: existedUser.phoneNumber

        val isNameChangedFlag = newFirstName != existedUser.firstName || newLastName != existedUser.lastName
        val isEmailChangedFlag = newEmail != existedUser.email
        val isPhoneChangedFlag = newPhoneNumber != existedUser.phoneNumber

        if (isNameChangedFlag) {
            val existingUserByName = userRepository.findByFullName(newFirstName, newLastName)
            if (existingUserByName != null && existingUserByName.id != existedUser.id) {
                throw UserWithNameAlreadyExistsException()
            }
        }

        if (isEmailChangedFlag) {
            val existingUserByEmail = userRepository.findByEmail(newEmail)
            if (existingUserByEmail != null && existingUserByEmail.id != existedUser.id) {
                throw UserWithEmailAlreadyExistsException()
            }
        }

        if (isPhoneChangedFlag) {
            val existingUserByPhone = userRepository.findByPhoneNumber(newPhoneNumber)
            if (existingUserByPhone != null && existingUserByPhone.id != existedUser.id) {
                throw UserWithPhoneAlreadyExistsException()
            }
        }

        val updatedUser = existedUser
        inboundCommand.firstName?.let{ updatedUser.changeFirstName(it)}
        inboundCommand.lastName?.let { updatedUser.changeLastName(it) }
        inboundCommand.email?.let { updatedUser.changeEmail(it) }
        inboundCommand.phoneNumber?.let{ updatedUser.changePhoneNumber(it)}
        inboundCommand.password?.let { updatedUser.changePasswordHash(passwordEncoder.encode(it)!!) }

        return userRepository.save(updatedUser)
    }
}