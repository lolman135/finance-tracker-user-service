package tracker.userservice.application.exception.user

import tracker.userservice.application.exception.DomainAlreadyExistsException

class UserWithPhoneAlreadyExistsException : DomainAlreadyExistsException("User with this phone number already exists")