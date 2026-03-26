package tracker.userservice.web.controller

import RoleDtoOutbound
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import tracker.userservice.application.usecase.role.CreateRoleUseCase
import tracker.userservice.application.usecase.role.DeleteRoleByIdUseCase
import tracker.userservice.application.usecase.role.FindAllRolesByPageUseCase
import tracker.userservice.application.usecase.role.FindAllRolesUseCase
import tracker.userservice.application.usecase.role.UpdateRoleByIdUseCase
import tracker.userservice.application.usecase.role.comands.FindAllRolesPageableCommand
import tracker.userservice.application.usecase.role.comands.UpdateRoleCommand
import tracker.userservice.infrastructure.dto.PageableDtoOutbound
import tracker.userservice.infrastructure.dto.role.RoleDtoInbound
import tracker.userservice.infrastructure.mapper.web.RoleDtoDomainMapper
import java.net.URI
import java.util.UUID

@RestController
@RequestMapping("/api/v1/roles")
@Validated
class RoleController(
    private val createRoleUseCase: CreateRoleUseCase,
    private val findAllRolesUseCase: FindAllRolesUseCase,
    private val updateRoleByIdUseCase: UpdateRoleByIdUseCase,
    private val deleteRoleByIdUseCase: DeleteRoleByIdUseCase,
    private val findAllRolesByPageUseCase: FindAllRolesByPageUseCase,
    private val roleMapper: RoleDtoDomainMapper
) {

    @PostMapping
    fun create(@Valid @RequestBody inbound: RoleDtoInbound): ResponseEntity<RoleDtoOutbound> {
        val command = roleMapper.toCommand(inbound)
        val response = roleMapper.toDto(createRoleUseCase.execute(command))
        val location = URI.create("/api/v1/roles")
        return ResponseEntity.created(location).body(response)
    }

    @GetMapping
    fun getAll() = ResponseEntity.ok(findAllRolesUseCase.execute(Unit).map { roleMapper.toDto(it) })

    @GetMapping("/page")
    fun getAllPageable(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<PageableDtoOutbound<RoleDtoOutbound>> {
        val command = FindAllRolesPageableCommand(pageAmount = page, size = size)
        return ResponseEntity.ok(roleMapper.toPageableDto(findAllRolesByPageUseCase.execute(command)))
    }

    @PutMapping("/{id}")
    fun updateRole(
        @PathVariable id: UUID,
        @Valid @RequestBody request: RoleDtoInbound
    ): ResponseEntity<RoleDtoOutbound> {
        val command = UpdateRoleCommand(id, request.name)
        val role = updateRoleByIdUseCase.execute(command)
        return ResponseEntity.ok(roleMapper.toDto(role))
    }

    @DeleteMapping("/{id}")
    fun deleteRole(@PathVariable id: UUID): ResponseEntity<Unit> {
        deleteRoleByIdUseCase.execute(id)
        return ResponseEntity.noContent().build()
    }

}