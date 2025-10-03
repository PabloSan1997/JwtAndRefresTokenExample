package com.example.service.repositories;

import com.example.service.models.entities.Logins;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface LoginRespository extends CrudRepository<Logins, UUID> {
}
