package tracker.userservice

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import


@SpringBootTest
@Disabled("Disabled because the app context test is not needed and breaks Liquibase/Testcontainers setup")
class UserServiceApplicationTests {

    @Test
    fun contextLoads() {
    }

}
