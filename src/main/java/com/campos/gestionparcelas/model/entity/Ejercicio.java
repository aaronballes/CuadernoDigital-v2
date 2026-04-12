package com.campos.gestionparcelas.model.entity;

import javax.persistence.*;

/**
 * Entidad que representa un ejercicio (campaña agrícola anual).
 * 
 * Cada ejercicio corresponde a un año natural de cultivo
 * y se utiliza para hacer seguimiento anual de las parcelas.
 */
@Entity
@Table(name = "ejercicios")
public class Ejercicio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ejercicio_id")
    private String ejercicioId;
    
    /** Nombre/año del ejercicio (ej: "2026", "2025") */
    private String ejercicio;

    public Ejercicio() {}

    public String getEjercicioId() { return ejercicioId; }
    public void setEjercicioId(String ejercicioId) { this.ejercicioId = ejercicioId; }
    
    public String getEjercicio() { return ejercicio; }
    public void setEjercicio(String ejercicio) { this.ejercicio = ejercicio; }
}
