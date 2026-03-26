package tracker.userservice.application.usecase.user

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tracker.userservice.application.exception.user.UserNotFoundException
import tracker.userservice.application.exception.user.UserWithEmailAlreadyExistsException
import tracker.userservice.application.exception.user.UserWithPhoneAlreadyExistsException
import tracker.userservice.application.usecase.UseCase
import tracker.userservice.application.usecase.user.command.UpdateUserCommand
import tracker.userservice.domain.user.User
import tracker.userservice.domain.user.UserRepository

@Service
class UpdateUserByIdUseCase(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UseCase<UpdateUserCommand, User> {

    @Transactional
    override fun execute(inboundCommand: UpdateUserCommand): User {
        val existedUser = userRepository.findById(inboundCommand.id!!) ?: throw UserNotFoundException()

        val newEmail = inboundCommand.email ?: existedUser.email
        val newPhoneNumber = inboundCommand.phoneNumber ?: existedUser.phoneNumber

        val isEmailChangedFlag = newEmail != existedUser.email
        val isPhoneChangedFlag = newPhoneNumber != existedUser.phoneNumber


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

        var updatedUser = existedUser
        updatedUser = inboundCommand.firstName?.let { updatedUser.changeFirstName(it) } ?: updatedUser
        updatedUser = inboundCommand.lastName?.let { updatedUser.changeLastName(it) } ?: updatedUser
        updatedUser = inboundCommand.email?.let { updatedUser.changeEmail(it) } ?: updatedUser
        updatedUser = inboundCommand.phoneNumber?.let { updatedUser.changePhoneNumber(it) } ?: updatedUser
        updatedUser = inboundCommand.password?.let { updatedUser.changePasswordHash(passwordEncoder.encode(it)!!) } ?: updatedUser

        return userRepository.save(updatedUser)
    }
}