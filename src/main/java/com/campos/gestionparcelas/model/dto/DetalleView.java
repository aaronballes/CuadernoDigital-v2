package com.campos.gestionparcelas.model.dto;

import java.io.Serializable;

public class DetalleView implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long ejercicioId;
    private String ejercicio;
    private Long cultivoId;
    private String cultivo;
    private Long parcelaId;
    private String nombre;
    private String poligono;
    private String parcela;
    private String superficie;

    public DetalleView() {}

    public DetalleView(Long ejercicioId, String ejercicio, Long cultivoId, String cultivo,
                       Long parcelaId, String nombre, String poligono, String parcela, String superficie) {
        this.ejercicioId = ejercicioId;
        this.ejercicio = ejercicio;
        this.cultivoId = cultivoId;
        this.cultivo = cultivo;
        this.parcelaId = parcelaId;
        this.nombre = nombre;
        this.poligono = poligono;
        this.parcela = parcela;
        this.superficie = superficie;
    }

    public Long getEjercicioId() { return ejercicioId; }
    public void setEjercicioId(Long ejercicioId) { this.ejercicioId = ejercicioId; }

    public String getEjercicio() { return ejercicio; }
    public void setEjercicio(String ejercicio) { this.ejercicio = ejercicio; }

    public Long getCultivoId() { return cultivoId; }
    public void setCultivoId(Long cultivoId) { this.cultivoId = cultivoId; }

    public String getCultivo() { return cultivo; }
    public void setCultivo(String cultivo) { this.cultivo = cultivo; }

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
}
