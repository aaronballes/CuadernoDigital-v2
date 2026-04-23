package com.campos.gestionparcelas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }
    
    @GetMapping("/home")
    public String home() {
        return "index";
    }
    
    @GetMapping("/parcelas")
    public String parcelas() {
        return "parcelas";
    }
    
    @GetMapping("/ejercicios")
    public String ejercicios() {
        return "ejercicios";
    }
    
    @GetMapping("/cultivos")
    public String cultivos() {
        return "cultivos";
    }
    
    @GetMapping("/detalles")
    public String detalles() {
        return "detalles";
    }
    
    @GetMapping("/propietarios")
    public String propietarios() {
        return "propietarios";
    }
    
    @GetMapping("/estadisticas")
    public String estadisticas() {
        return "estadisticas";
    }
}
