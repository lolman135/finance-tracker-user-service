package tracker.userservice.application.exception.role

import tracker.userservice.application.exception.DomainNotFoundException

class RoleNotFoundException() : DomainNotFoundException("This role not found") {
}