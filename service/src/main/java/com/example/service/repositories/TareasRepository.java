package com.example.service.repositories;

import com.example.service.models.entities.Tareas;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TareasRepository extends CrudRepository<Tareas, Long> {
    @Query("select t from Tareas t where t.user.username = ?1 order by t.createdAt desc")
    List<Tareas> findByUsername(String username);

    @Query("select t from Tareas t where t.user.username = :username and t.id= :id")
    Optional<Tareas> findByIdAndUsername(@Param("username") String username, @Param("id") Long id);
}
