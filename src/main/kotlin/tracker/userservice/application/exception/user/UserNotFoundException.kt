package tracker.userservice.application.exception.user

import tracker.userservice.application.exception.DomainNotFoundException

class UserNotFoundException() : DomainNotFoundException("This user not found") {
}