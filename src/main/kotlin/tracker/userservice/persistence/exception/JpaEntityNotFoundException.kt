package labs.catmarket.repository.exception

open class JpaEntityNotFoundException(open val name: String?) : IllegalArgumentException() {
    override val message: String
        get() = "$name entity not found"
}