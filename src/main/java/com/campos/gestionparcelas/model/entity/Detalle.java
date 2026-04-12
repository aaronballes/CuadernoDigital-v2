package com.campos.gestionparcelas.model.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entidad que representa la relación entre parcelas, ejercicios y cultivos.
 * 
 * Esta tabla actúa como tabla de hechos del sistema, vinculando:
 * - Una parcela específica
 * - Un ejercicio (año) específico
 * - Un cultivo específico
 * 
 * La clave primaria está formada por las tres claves foráneas.
 * Esto permite conocer en cada ejercicio qué cultivo se plantó en cada parcela.
 */
@Entity
@Table(name = "detalles")
@IdClass(DetalleId.class)
public class Detalle implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "ejercicio_id")
    private String ejercicioId;
    
    @Id
    @Column(name = "cultivo_id")
    private Long cultivoId;
    
    @Id
    @Column(name = "parcela_id")
    private Long parcelaId;
    
    /** Nombre alternativo para la parcela (opcional) */
    private String nombre;
    
    /** Polígono alternativo (opcional) */
    private String poligono;
    
    /** Número de parcela alternativo (opcional) */
    private String parcela;
    
    /** Superficie alternativa (opcional) */
    private String superficie;
    
    /** Nombre del cultivo (para redundancia/opcional) */
    private String cultivo;

    public Detalle() {}

    public String getEjercicioId() { return ejercicioId; }
    public void setEjercicioId(String ejercicioId) { this.ejercicioId = ejercicioId; }
    
    public Long getCultivoId() { return cultivoId; }
    public void setCultivoId(Long cultivoId) { this.cultivoId = cultivoId; }
    
    public Long getParcelaId() { return parcelaId; }
    public void setParcelaId(Long parcelaId) { this.parcelaId = parcelaId; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getPoligono() { return poligono; }
    public void setPoligono(String poligono) { this.poligono = poligono; }
    
    public String getParcela() { return parcela; }
    public void setParcela(String parcela) { this.parcela = parcela; }
    
    public String getSuperficie() { return superficie; }
    public void setSuperficie(String superficie) { this.superficie = superficie; }
    
    public String getCultivo() { return cultivo; }
    public void setCultivo(String cultivo) { this.cultivo = cultivo; }
}
