package tracker.userservice.web.controller

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tracker.userservice.application.usecase.auth.LoginUseCase
import tracker.userservice.application.usecase.auth.RegisterUseCase
import tracker.userservice.infrastructure.dto.user.JwtDtoOutbound
import tracker.userservice.infrastructure.dto.user.UserLoginDtoInbound
import tracker.userservice.infrastructure.dto.user.UserRegisterDtoInbound
import tracker.userservice.infrastructure.mapper.web.UserDtoDomainMapper

@RestController
@RequestMapping("/api/v1/auth")
@Validated
class AuthController(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val userDtoDomainMapper: UserDtoDomainMapper
) {

    @PostMapping("/login")
    fun login(@Valid @RequestBody inbound: UserLoginDtoInbound): ResponseEntity<JwtDtoOutbound>{
        val token = loginUseCase.execute(userDtoDomainMapper.toLoginCommand(inbound))
        return ResponseEntity.ok(JwtDtoOutbound(type = "Bearer", token = token))
    }

    @PostMapping("/register")
    fun register(@Valid @RequestBody inbound: UserRegisterDtoInbound): ResponseEntity<JwtDtoOutbound>{
        val token = registerUseCase.execute(userDtoDomainMapper.toRegisterCommand(inbound))
        return ResponseEntity.ok(JwtDtoOutbound(type = "Bearer", token = token))
    }
}