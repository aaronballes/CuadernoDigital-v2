package com.campos.gestionparcelas.model.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * Clase de identificación compuesta para la entidad Detalle.
 * 
 * Dado que Detalle tiene una clave primaria formada por tres campos
 * (ejercicio_id, cultivo_id, parcela_id), se utiliza esta clase
 * para representar la identidad compuesta.
 */
public class DetalleId implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String ejercicioId;
    private Long cultivoId;
    private Long parcelaId;

    public DetalleId() {}

    public DetalleId(String ejercicioId, Long cultivoId, Long parcelaId) {
        this.ejercicioId = ejercicioId;
        this.cultivoId = cultivoId;
        this.parcelaId = parcelaId;
    }

    public String getEjercicioId() { return ejercicioId; }
    public void setEjercicioId(String ejercicioId) { this.ejercicioId = ejercicioId; }
    
    public Long getCultivoId() { return cultivoId; }
    public void setCultivoId(Long cultivoId) { this.cultivoId = cultivoId; }
    
    public Long getParcelaId() { return parcelaId; }
    public void setParcelaId(Long parcelaId) { this.parcelaId = parcelaId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetalleId that = (DetalleId) o;
        return Objects.equals(ejercicioId, that.ejercicioId) &&
               Objects.equals(cultivoId, that.cultivoId) &&
               Objects.equals(parcelaId, that.parcelaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ejercicioId, cultivoId, parcelaId);
    }
}
