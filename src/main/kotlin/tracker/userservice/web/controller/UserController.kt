package tracker.userservice.web.controller

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
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
import tracker.userservice.application.common.toCreateCommand
import tracker.userservice.application.usecase.user.AddRoleUseCase
import tracker.userservice.application.usecase.user.CreateUserUseCase
import tracker.userservice.application.usecase.user.DeleteUserByIdUseCase
import tracker.userservice.application.usecase.user.FIndUserByEmailUseCase
import tracker.userservice.application.usecase.user.FindAllUsersUseCase
import tracker.userservice.application.usecase.user.FindUserByFullNameUseCase
import tracker.userservice.application.usecase.user.FindUserByIdUseCase
import tracker.userservice.application.usecase.user.FindUserByPhoneUseCase
import tracker.userservice.application.usecase.user.UpdateUserByIdUseCase
import tracker.userservice.application.usecase.user.command.AddRoleCommand
import tracker.userservice.application.usecase.user.command.FindAllUserPageableCommand
import tracker.userservice.application.usecase.user.command.FindUserByFullNameCommand
import tracker.userservice.infrastructure.dto.PageableDtoOutbound
import tracker.userservice.infrastructure.dto.user.AddRoleDtoInbound
import tracker.userservice.infrastructure.dto.user.UserDtoOutbound
import tracker.userservice.infrastructure.dto.user.UserRegisterDtoInbound
import tracker.userservice.infrastructure.dto.user.UserUpdateDtoInbound
import tracker.userservice.infrastructure.mapper.web.UserDtoDomainMapper
import java.net.URI
import java.util.UUID

@RestController
@RequestMapping("/api/v1/users")
@Validated
class UserController(
    private val createUserUseCase: CreateUserUseCase,
    private val findUserByIdUseCase: FindUserByIdUseCase,
    private val updateUserByIdUseCase: UpdateUserByIdUseCase,
    private val deleteUserByIdUseCase: DeleteUserByIdUseCase,
    private val findAllUsersUseCase: FindAllUsersUseCase,
    private val findUserByEmailUseCase: FIndUserByEmailUseCase,
    private val findUserByPhoneUseCase: FindUserByPhoneUseCase,
    private val findUserByFullNameUseCase: FindUserByFullNameUseCase,
    private val addRoleUseCase: AddRoleUseCase,
    private val userMapper: UserDtoDomainMapper
) {

    @PostMapping
    fun createUser(@Valid @RequestBody inbound: UserRegisterDtoInbound): ResponseEntity<UserDtoOutbound> {
        val command = userMapper.toRegisterCommand(inbound).toCreateCommand()
        val response = userMapper.toDto(createUserUseCase.execute(command))
        val location = URI.create("/api/v1/users")
        return ResponseEntity.created(location).body(response)
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: UUID): ResponseEntity<UserDtoOutbound> {
        val user = findUserByIdUseCase.execute(id)
        return ResponseEntity.ok(userMapper.toDto(user))
    }

    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable id: UUID,
        @Valid @RequestBody request: UserUpdateDtoInbound
    ): ResponseEntity<UserDtoOutbound> {
        val command = userMapper.toUpdateCommand(request).copy(id = id)
        val user = updateUserByIdUseCase.execute(command)
        return ResponseEntity.ok(userMapper.toDto(user))
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: UUID): ResponseEntity<Unit> {
        deleteUserByIdUseCase.execute(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping
    fun getAllUsers(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<PageableDtoOutbound<UserDtoOutbound>> {
        val command = FindAllUserPageableCommand(pageAmount = page, size = size)
        return ResponseEntity.ok(userMapper.toPageableDto(findAllUsersUseCase.execute(command)))
    }

    @GetMapping("/by-email")
    fun getUserByEmail(@RequestParam email: String): ResponseEntity<UserDtoOutbound> {
        val user = findUserByEmailUseCase.execute(email)
        return ResponseEntity.ok(userMapper.toDto(user))
    }

    @GetMapping("/by-phone")
    fun getUserByPhone(@RequestParam phone: String): ResponseEntity<UserDtoOutbound> {
        val user = findUserByPhoneUseCase.execute(phone)
        return ResponseEntity.ok(userMapper.toDto(user))
    }

    @GetMapping("/by-full-name")
    fun getUserByFullName(
        @RequestParam firstName: String,
        @RequestParam lastName: String
    ): ResponseEntity<List<UserDtoOutbound>> {
        val command = FindUserByFullNameCommand(firstName, lastName)
        return ResponseEntity.ok(findUserByFullNameUseCase.execute(command).map { userMapper.toDto(it) })
    }

    @PostMapping("/{userId}/roles")
    fun addRoleToUser(
        @PathVariable userId: UUID,
        @Valid @RequestBody request: AddRoleDtoInbound
    ): ResponseEntity<UserDtoOutbound> {
        val command = AddRoleCommand(userId = userId, roleName = request.roleName)
        val user = addRoleUseCase.execute(command)
        return ResponseEntity.ok(userMapper.toDto(user))
    }
}

