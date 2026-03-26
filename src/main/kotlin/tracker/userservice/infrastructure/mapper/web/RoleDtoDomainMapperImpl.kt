package tracker.userservice.infrastructure.mapper.web

import RoleDtoOutbound
import org.springframework.stereotype.Component
import tracker.userservice.application.usecase.role.comands.CreateRoleCommand
import tracker.userservice.domain.PageResponse
import tracker.userservice.domain.role.Role
import tracker.userservice.infrastructure.dto.PageableDtoOutbound
import tracker.userservice.infrastructure.dto.PaginationMetadata
import tracker.userservice.infrastructure.dto.role.RoleDtoInbound


@Component
class RoleDtoDomainMapperImpl : RoleDtoDomainMapper{
    override fun toDto(domain: Role) = RoleDtoOutbound(id = domain.id, name = domain.name)
    override fun toCommand(inboundDto: RoleDtoInbound) = CreateRoleCommand(name = inboundDto.name)

    override fun toPageableDto(pageResponse: PageResponse<Role>): PageableDtoOutbound<RoleDtoOutbound> {
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