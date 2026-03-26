package tracker.userservice.application.exception.role

import tracker.userservice.application.exception.DomainNotFoundException

class RoleNotFoundException(override val message: String = "This role not found") : DomainNotFoundException(message) {
}