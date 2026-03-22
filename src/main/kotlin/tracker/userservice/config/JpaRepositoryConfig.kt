package tracker.userservice.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import tracker.userservice.persistence.impl.NaturalIdRepositoryImpl

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(
    basePackages = ["labs.catmarket.repository"],
    repositoryBaseClass = NaturalIdRepositoryImpl::class)
class JpaRepositoryConfig