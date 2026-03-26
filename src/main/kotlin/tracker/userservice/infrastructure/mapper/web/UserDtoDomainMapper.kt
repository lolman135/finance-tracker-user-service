package tracker.userservice.infrastructure.mapper.web

import tracker.userservice.application.usecase.auth.command.LoginUserCommand
import tracker.userservice.application.usecase.auth.command.RegisterUserCommand
import tracker.userservice.application.usecase.user.command.UpdateUserCommand
import tracker.userservice.domain.PageResponse
import tracker.userservice.domain.user.User
import tracker.userservice.infrastructure.dto.PageableDtoOutbound
import tracker.userservice.infrastructure.dto.user.UserDtoOutbound
import tracker.userservice.infrastructure.dto.user.UserLoginDtoInbound
import tracker.userservice.infrastructure.dto.user.UserRegisterDtoInbound
import tracker.userservice.infrastructure.dto.user.UserUpdateDtoInbound

interface UserDtoDomainMapper {
    fun toDto(domain: User): UserDtoOutbound
    fun toRegisterCommand(registerDto: UserRegisterDtoInbound): RegisterUserCommand
    fun toLoginCommand(loginDto: UserLoginDtoInbound): LoginUserCommand
    fun toUpdateCommand(updateDto: UserUpdateDtoInbound): UpdateUserCommand
    fun toPageableDto(pageResponse: PageResponse<User>): PageableDtoOutbound<UserDtoOutbound>
}