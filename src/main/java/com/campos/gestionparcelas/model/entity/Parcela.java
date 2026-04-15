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
    @Column(name = "parcela_id")
    private Long parcelaId;
    
    private String nombre;
    private String poligono;
    private String parcela;
    
    @Column(name = "superficie")
    private BigDecimal superficie;
    
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
