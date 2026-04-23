package com.campos.gestionparcelas.model.dto;

import java.math.BigDecimal;

public class ParcelaDTO {
    
    private Long parcelaId;
    private String nombre;
    private String poligono;
    private String parcela;
    private BigDecimal superficie;
    private Long propietarioId;
    private String propietarioNombre;

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
    
    public String getPropietarioNombre() { return propietarioNombre; }
    public void setPropietarioNombre(String propietarioNombre) { this.propietarioNombre = propietarioNombre; }
}