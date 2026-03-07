package tracker.userservice.application.exception

open class DomainNotFoundException(override val message: String) : RuntimeException(message)