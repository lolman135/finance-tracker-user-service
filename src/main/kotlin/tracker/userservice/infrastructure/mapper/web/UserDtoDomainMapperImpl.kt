package tracker.userservice.infrastructure.mapper.web

import org.springframework.stereotype.Component
import tracker.userservice.application.usecase.auth.command.LoginUserCommand
import tracker.userservice.application.usecase.auth.command.RegisterUserCommand
import tracker.userservice.application.usecase.user.command.UpdateUserCommand
import tracker.userservice.domain.PageResponse
import tracker.userservice.domain.role.Role
import tracker.userservice.domain.user.User
import tracker.userservice.infrastructure.dto.PageableDtoOutbound
import tracker.userservice.infrastructure.dto.PaginationMetadata
import tracker.userservice.infrastructure.dto.user.UserDtoOutbound
import tracker.userservice.infrastructure.dto.user.UserLoginDtoInbound
import tracker.userservice.infrastructure.dto.user.UserRegisterDtoInbound
import tracker.userservice.infrastructure.dto.user.UserUpdateDtoInbound

@Component
class UserDtoDomainMapperImpl(private val roleMapper: RoleDtoDomainMapperImpl) : UserDtoDomainMapper{
    override fun toDto(domain: User): UserDtoOutbound {
        return UserDtoOutbound(
            id = domain.id,
            firstName = domain.firstName,
            lastName = domain.lastName,
            phoneNumber = domain.phoneNumber,
            email = domain.email,
            createdAt = domain.createdAt,
            roles = domain.roles.map { roleMapper.toDto(it) }
        )
    }

    override fun toRegisterCommand(registerDto: UserRegisterDtoInbound): RegisterUserCommand {
        return RegisterUserCommand(
            firstName = registerDto.firstName,
            lastName = registerDto.lastName,
            phoneNumber = registerDto.phoneNumber,
            email = registerDto.email,
            password = registerDto.password
        )
    }

    override fun toLoginCommand(loginDto: UserLoginDtoInbound): LoginUserCommand {
        return LoginUserCommand(
            email = loginDto.email,
            password = loginDto.password
        )
    }

    override fun toUpdateCommand(updateDto: UserUpdateDtoInbound): UpdateUserCommand {
        return UpdateUserCommand(
            firstName = updateDto.firstName,
            lastName = updateDto.lastName,
            phoneNumber = updateDto.phoneNumber,
            email = updateDto.email,
            password = updateDto.password
        )
    }

    override fun toPageableDto(pageResponse: PageResponse<User>): PageableDtoOutbound<UserDtoOutbound> {
        return PageableDtoOutbound(
            items = pageResponse.items.map { toDto(it) },
            metadata = PaginationMetadata(
                totalElements = pageResponse.total,
                pageSize = pageResponse.size,
                currentPage = pageResponse.page
            )
        )
    }
}