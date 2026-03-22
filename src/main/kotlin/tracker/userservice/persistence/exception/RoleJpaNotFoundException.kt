package tracker.userservice.persistence.exception

import labs.catmarket.repository.exception.JpaEntityNotFoundException

class RoleJpaNotFoundException() : JpaEntityNotFoundException("Role") {
}