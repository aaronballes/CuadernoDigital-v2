package com.campos.gestionparcelas.controller;

import com.campos.gestionparcelas.model.entity.Ejercicio;
import com.campos.gestionparcelas.service.EjercicioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ejercicios")
@CrossOrigin(origins = "*")
public class EjercicioController {
    
    private final EjercicioService ejercicioService;
    
    public EjercicioController(EjercicioService ejercicioService) {
        this.ejercicioService = ejercicioService;
    }
    
    @GetMapping
    public List<Ejercicio> listar() {
        return ejercicioService.listar();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Ejercicio> buscarPorId(@PathVariable String id) {
        return ejercicioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public Ejercicio crear(@RequestBody Ejercicio ejercicio) {
        return ejercicioService.guardar(ejercicio);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Ejercicio> actualizar(@PathVariable String id, @RequestBody Ejercicio ejercicio) {
        return ejercicioService.buscarPorId(id)
                .map(existente -> {
                    ejercicio.setEjercicioId(id);
                    return ResponseEntity.ok(ejercicioService.guardar(ejercicio));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        ejercicioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
