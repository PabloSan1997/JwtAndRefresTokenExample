package com.example.service.repositories;

import com.example.service.models.entities.RolesEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<RolesEntity, Long> {
    Optional<RolesEntity> findByName(String name);
}
