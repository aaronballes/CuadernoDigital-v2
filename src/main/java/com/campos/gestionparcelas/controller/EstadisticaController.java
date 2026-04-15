package com.campos.gestionparcelas.controller;

import com.campos.gestionparcelas.service.EstadisticaService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/estadisticas")
@CrossOrigin(origins = "*")
public class EstadisticaController {
    
    private final EstadisticaService estadisticaService;
    
    public EstadisticaController(EstadisticaService estadisticaService) {
        this.estadisticaService = estadisticaService;
    }
    
    @GetMapping("/superficie-por-cultivo")
    public List<Map<String, Object>> getSuperficiePorCultivo() {
        return estadisticaService.getSuperficiePorCultivoPorEjercicio();
    }
}