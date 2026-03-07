package tracker.userservice.application.exception

open class DomainAlreadyExistsException(override val message: String) : RuntimeException(message)