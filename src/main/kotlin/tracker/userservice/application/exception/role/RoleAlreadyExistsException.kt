package tracker.userservice.application.exception.role

import tracker.userservice.application.exception.DomainAlreadyExistsException

class RoleAlreadyExistsException() : DomainAlreadyExistsException("Role with this name already exists") {
}