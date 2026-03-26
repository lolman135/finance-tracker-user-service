package tracker.userservice.application.exception.user

import tracker.userservice.application.exception.DomainAlreadyExistsException

class UserWithNameAlreadyExistsException() : DomainAlreadyExistsException("User with name this already exists") {
}