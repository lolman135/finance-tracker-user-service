package tracker.userservice.infrastructure.mapper.jpa

import tracker.userservice.domain.user.User
import tracker.userservice.persistence.entity.UserEntity

interface UserJpaMapper : EntityDomainMapper<UserEntity, User> {
}