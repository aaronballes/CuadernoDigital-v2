package com.campos.gestionparcelas.controller;

import com.campos.gestionparcelas.model.dto.ParcelaDTO;
import com.campos.gestionparcelas.model.entity.Parcela;
import com.campos.gestionparcelas.service.ParcelaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/parcelas")
@CrossOrigin(origins = "*")
public class ParcelaController {
    
    private final ParcelaService parcelaService;
    
    public ParcelaController(ParcelaService parcelaService) {
        this.parcelaService = parcelaService;
    }
    
    @GetMapping
    public List<ParcelaDTO> listar() {
        return parcelaService.listar();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Parcela> buscarPorId(@PathVariable Long id) {
        return parcelaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public Parcela crear(@RequestBody Parcela parcela) {
        return parcelaService.guardar(parcela);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Parcela> actualizar(@PathVariable Long id, @RequestBody Parcela parcela) {
        return parcelaService.buscarPorId(id)
                .map(existente -> {
                    parcela.setParcelaId(id);
                    return ResponseEntity.ok(parcelaService.guardar(parcela));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        parcelaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
