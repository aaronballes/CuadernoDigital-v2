package com.campos.gestionparcelas.model.entity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Entidad que representa una parcela agrícola.
 * 
 * Una parcela es una porción de terreno identificada por su nombre,
 * polígono, número de parcela y superficie.
 */
@Entity
@Table(name = "parcelas")
public class Parcela {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parcela_id")
    private Long parcelaId;
    
    /** Nombre descriptivo de la parcela */
    private String nombre;
    
    /** Número de polígono donde se ubica la parcela */
    private String poligono;
    
    /** Número identificador de la parcela dentro del polígono */
    private String parcela;
    
    /** Superficie de la parcela en hectáreas */
    @Column(name = "superficie")
    private BigDecimal superficie;
    
    /** Identificador del propietario de la parcela */
    @Column(name = "propietario_id")
    private Long propietarioId;

    public Parcela() {}

    public Long getParcelaId() { return parcelaId; }
    public void setParcelaId(Long parcelaId) { this.parcelaId = parcelaId; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getPoligono() { return poligono; }
    public void setPoligono(String poligono) { this.poligono = poligono; }
    
    public String getParcela() { return parcela; }
    public void setParcela(String parcela) { this.parcela = parcela; }
    
    public BigDecimal getSuperficie() { return superficie; }
    public void setSuperficie(BigDecimal superficie) { this.superficie = superficie; }
    
    public Long getPropietarioId() { return propietarioId; }
    public void setPropietarioId(Long propietarioId) { this.propietarioId = propietarioId; }
}
