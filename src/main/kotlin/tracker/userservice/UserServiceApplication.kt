package tracker.userservice

import io.github.cdimascio.dotenv.Dotenv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class UserServiceApplication

fun main(args: Array<String>) {
    val dotenv = Dotenv.configure()
        .ignoreIfMissing()
        .load()

    dotenv.entries().forEach { entry ->
        System.setProperty(entry.key, entry.value)
    }
    runApplication<UserServiceApplication>(*args)

}
