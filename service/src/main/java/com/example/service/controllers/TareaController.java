package com.example.service.controllers;

import com.example.service.models.dtos.TareaDto;
import com.example.service.services.AppService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tarea")
public class TareaController {
    @Autowired
    private AppService appService;


    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(appService.findAll());
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody TareaDto tareaDto) {
        return ResponseEntity.status(201).body(appService.save(tareaDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> upcateById(@Valid @RequestBody TareaDto tareaDto, @PathVariable Long id) {
        return ResponseEntity.ok(appService.updateTareaById(id, tareaDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBuIOd(@PathVariable Long id) {
        appService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
