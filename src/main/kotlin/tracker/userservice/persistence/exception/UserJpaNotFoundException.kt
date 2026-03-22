package tracker.userservice.persistence.exception

import labs.catmarket.repository.exception.JpaEntityNotFoundException

class UserJpaNotFoundException() : JpaEntityNotFoundException("User") {
}