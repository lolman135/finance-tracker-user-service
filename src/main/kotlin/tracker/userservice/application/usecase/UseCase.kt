package tracker.userservice.application.usecase

import tracker.userservice.domain.role.Role

interface UseCase<I, O> {
    fun execute(inboundCommand: I): O
}