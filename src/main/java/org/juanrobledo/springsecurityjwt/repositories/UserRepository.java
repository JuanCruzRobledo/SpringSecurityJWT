package org.juanrobledo.springsecurityjwt.repositories;

import org.juanrobledo.springsecurityjwt.models.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
}
