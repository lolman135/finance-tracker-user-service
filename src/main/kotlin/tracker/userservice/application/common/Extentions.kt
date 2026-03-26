package tracker.userservice.application.common

import tracker.userservice.application.usecase.auth.command.RegisterUserCommand
import tracker.userservice.application.usecase.user.command.CreateUserCommand

//Strings
fun String.toRoleFormat() = "ROLE_" + this.uppercase()

//Commands
fun RegisterUserCommand.toCreateCommand() = CreateUserCommand(
    firstName = this.firstName,
    lastName = this.lastName,
    email = this.email,
    phoneNumber = this.phoneNumber,
    password = this.password
)