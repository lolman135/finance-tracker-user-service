package tracker.userservice.application.exception.user

import tracker.userservice.application.exception.DomainNotFoundException

class UserNotFoundException(override val message: String = "This user not found") : DomainNotFoundException(message) {
}