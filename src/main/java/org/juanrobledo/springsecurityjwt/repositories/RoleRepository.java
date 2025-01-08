package org.juanrobledo.springsecurityjwt.repositories;

import org.juanrobledo.springsecurityjwt.models.RoleEntity;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<RoleEntity, Long> {
}
