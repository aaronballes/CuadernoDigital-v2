package com.campos.gestionparcelas.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "propietarios")
public class Propietario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "propietario_seq")
    @SequenceGenerator(name = "propietario_seq", sequenceName = "PROPIETARIOS_SEQ", allocationSize = 1)
    @Column(name = "propietario_id")
    private Long propietarioId;
    
    @Column(name = "PROPIETARIO")
    private String propietario;

    public Propietario() {}

    public Long getPropietarioId() { return propietarioId; }
    public void setPropietarioId(Long propietarioId) { this.propietarioId = propietarioId; }
    
    public String getPropietario() { return propietario; }
    public void setPropietario(String propietario) { this.propietario = propietario; }
}