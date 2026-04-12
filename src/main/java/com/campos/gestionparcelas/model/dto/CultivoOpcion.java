package com.campos.gestionparcelas.model.dto;

import java.io.Serializable;

public class CultivoOpcion {
    private Long cultivoId;
    private String cultivo;

    public CultivoOpcion() {}

    public CultivoOpcion(Long cultivoId, String cultivo) {
        this.cultivoId = cultivoId;
        this.cultivo = cultivo;
    }

    public Long getCultivoId() { return cultivoId; }
    public void setCultivoId(Long cultivoId) { this.cultivoId = cultivoId; }

    public String getCultivo() { return cultivo; }
    public void setCultivo(String cultivo) { this.cultivo = cultivo; }
}
