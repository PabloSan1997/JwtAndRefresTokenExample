package com.example.service.services;

import com.example.service.models.dtos.TareaDto;
import com.example.service.models.entities.Tareas;

import java.util.List;

public interface AppService {
    List<Tareas> findAll();
    Tareas updateTareaById(Long id, TareaDto tareaDto);
    Tareas save(TareaDto tareaDto);
    void deleteById(Long id);
}
