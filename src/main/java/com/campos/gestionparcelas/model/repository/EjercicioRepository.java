package com.campos.gestionparcelas.model.repository;

import com.campos.gestionparcelas.model.entity.Ejercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EjercicioRepository extends JpaRepository<Ejercicio, String> {
}
