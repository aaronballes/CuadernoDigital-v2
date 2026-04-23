package com.campos.gestionparcelas.controller;

import com.campos.gestionparcelas.model.entity.Propietario;
import com.campos.gestionparcelas.service.PropietarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/propietarios")
@CrossOrigin(origins = "*")
public class PropietarioController {
    
    private final PropietarioService propietarioService;
    
    public PropietarioController(PropietarioService propietarioService) {
        this.propietarioService = propietarioService;
    }
    
    @GetMapping
    public List<Propietario> listar() {
        return propietarioService.listar();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Propietario> buscarPorId(@PathVariable Long id) {
        return propietarioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public Propietario crear(@RequestBody Propietario propietario) {
        return propietarioService.guardar(propietario);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Propietario> actualizar(@PathVariable Long id, @RequestBody Propietario propietario) {
        return propietarioService.buscarPorId(id)
                .map(existente -> {
                    propietario.setPropietarioId(id);
                    return ResponseEntity.ok(propietarioService.guardar(propietario));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        propietarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}