package com.campos.gestionparcelas.model.entity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Entidad que representa un tipo de cultivo.
 * 
 * Ejemplos: trigo, cebada, girasol, etc.
 */
@Entity
@Table(name = "cultivos")
public class Cultivo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cultivo_id")
    private Long cultivoId;
    
    /** Nombre del cultivo (ej: "Trigo", "Cebada") */
    private String cultivo;
    
    /** Hectáreas dedicadas a este cultivo */
    private BigDecimal hectareas;
    
    /** Porcentaje sobre el total de superficie */
    private BigDecimal porcentaje;

    public Cultivo() {}

    public Long getCultivoId() { return cultivoId; }
    public void setCultivoId(Long cultivoId) { this.cultivoId = cultivoId; }
    
    public String getCultivo() { return cultivo; }
    public void setCultivo(String cultivo) { this.cultivo = cultivo; }
    
    public BigDecimal getHectareas() { return hectareas; }
    public void setHectareas(BigDecimal hectareas) { this.hectareas = hectareas; }
    
    public BigDecimal getPorcentaje() { return porcentaje; }
    public void setPorcentaje(BigDecimal porcentaje) { this.porcentaje = porcentaje; }
}
