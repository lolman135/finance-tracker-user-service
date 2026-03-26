package tracker.userservice

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.context.annotation.Import
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.transaction.annotation.Transactional
import org.testcontainers.postgresql.PostgreSQLContainer
import tracker.userservice.config.TestConfig
import tracker.userservice.utils.DbCleaner

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestConfig::class)
abstract class AbstractIT{

    @Autowired
    private lateinit var cleaner: DbCleaner

    companion object{
        @JvmStatic
        val postgres = PostgreSQLContainer("postgres:18-alpine").apply {
            withDatabaseName("testdb")
            withUsername("test")
            withPassword("test")
            start()
        }

        @JvmStatic
        @DynamicPropertySource
        fun registerProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url") {postgres.jdbcUrl}
            registry.add("spring.datasource.username") {postgres.username}
            registry.add("spring.datasource.password") {postgres.password}
            registry.add("spring.jpa.hibernate.ddl-auto") { "validate" }
            registry.add("spring.jpa.hibernate.ddl-auto") { "none" }
        }
    }

    @Transactional
    @BeforeEach
    fun cleanDB(){
        cleaner.clean()
    }
}