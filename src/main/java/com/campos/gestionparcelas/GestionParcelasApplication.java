package com.campos.gestionparcelas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada principal de la aplicación Gestión de Parcelas.
 * 
 * Esta aplicación es una API REST para gestionar parcelas agrícolas,
 * sus cultivos y ejercicios (campañas anuales).
 */
@SpringBootApplication
public class GestionParcelasApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(GestionParcelasApplication.class, args);
    }
}
