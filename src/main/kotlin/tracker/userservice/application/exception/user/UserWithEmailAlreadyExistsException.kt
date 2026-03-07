package tracker.userservice.application.exception.user

import tracker.userservice.application.exception.DomainAlreadyExistsException

class UserWithEmailAlreadyExistsException : DomainAlreadyExistsException("User with this email already exists")