package tracker.userservice.application.usecase.user

import org.springframework.stereotype.Service
import tracker.userservice.application.usecase.UseCase
import tracker.userservice.domain.user.UserRepository
import java.util.UUID

@Service
class DeleteUserByIdUseCase(private val userRepository: UserRepository) : UseCase<UUID, Unit> {
    override fun execute(id: UUID) {
        userRepository.deleteById(id)
    }
}