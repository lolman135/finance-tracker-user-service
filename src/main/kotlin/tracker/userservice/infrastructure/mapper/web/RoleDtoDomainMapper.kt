package tracker.userservice.infrastructure.mapper.web

import RoleDtoOutbound
import tracker.userservice.application.usecase.role.comands.CreateRoleCommand
import tracker.userservice.domain.PageResponse
import tracker.userservice.domain.role.Role
import tracker.userservice.infrastructure.dto.PageableDtoOutbound
import tracker.userservice.infrastructure.dto.role.RoleDtoInbound

interface RoleDtoDomainMapper {
    fun toDto(domain: Role): RoleDtoOutbound
    fun toCommand(inboundDto: RoleDtoInbound): CreateRoleCommand
    fun toPageableDto(pageResponse: PageResponse<Role>): PageableDtoOutbound<RoleDtoOutbound>
}