package com.campos.gestionparcelas.controller;

import com.campos.gestionparcelas.model.entity.Detalle;
import com.campos.gestionparcelas.model.entity.DetalleId;
import com.campos.gestionparcelas.service.DetalleService;
import com.campos.gestionparcelas.model.repository.CultivoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/detalles")
@CrossOrigin(origins = "*")
public class DetalleController {
    
    private final DetalleService detalleService;
    private final CultivoRepository cultivoRepository;
    
    public DetalleController(DetalleService detalleService, CultivoRepository cultivoRepository) {
        this.detalleService = detalleService;
        this.cultivoRepository = cultivoRepository;
    }
    
    @GetMapping
    public List<Map<String, Object>> listar(@RequestParam(required = false) String ejercicio) {
        if (ejercicio != null && !ejercicio.isEmpty()) {
            return detalleService.listarConDetallesPorEjercicio(ejercicio);
        }
        return detalleService.obtenerMatriz();
    }
    
    @GetMapping("/cultivos")
    public List<?> cultivos() {
        return cultivoRepository.findAll();
    }
    
    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@RequestBody Detalle detalle) {
        Map<String, Object> respuesta = detalleService.guardar(detalle);
        return ResponseEntity.ok(respuesta);
    }
    
    @DeleteMapping
    public ResponseEntity<Void> eliminar(@RequestBody DetalleId id) {
        detalleService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
