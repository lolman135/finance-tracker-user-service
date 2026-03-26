package tracker.userservice.infrastructure.security

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import tracker.userservice.application.exception.user.UserNotFoundException
import tracker.userservice.domain.user.UserRepository
import java.util.UUID

@Service
class CustomUserDetailService(private val userRepository: UserRepository) : UserDetailsService{

    override fun loadUserByUsername(email: String): UserDetails {
        val user = (userRepository.findByEmail(email)
            ?: throw UserNotFoundException("User with this email not found"))

        return CustomUserDetails(user)
    }

    fun loadUserById(id: UUID): UserDetails {
        val user = (userRepository.findById(id)
            ?: throw UserNotFoundException())

        return CustomUserDetails(user)
    }
}