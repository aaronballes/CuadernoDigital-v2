package com.campos.gestionparcelas.controller;

import com.campos.gestionparcelas.model.entity.Cultivo;
import com.campos.gestionparcelas.service.CultivoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cultivos")
@CrossOrigin(origins = "*")
public class CultivoController {
    
    private final CultivoService cultivoService;
    
    public CultivoController(CultivoService cultivoService) {
        this.cultivoService = cultivoService;
    }
    
    @GetMapping
    public List<Cultivo> listar() {
        return cultivoService.listar();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Cultivo> buscarPorId(@PathVariable Long id) {
        return cultivoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public Cultivo crear(@RequestBody Cultivo cultivo) {
        return cultivoService.guardar(cultivo);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Cultivo> actualizar(@PathVariable Long id, @RequestBody Cultivo cultivo) {
        return cultivoService.buscarPorId(id)
                .map(existente -> {
                    cultivo.setCultivoId(id);
                    return ResponseEntity.ok(cultivoService.guardar(cultivo));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        cultivoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
