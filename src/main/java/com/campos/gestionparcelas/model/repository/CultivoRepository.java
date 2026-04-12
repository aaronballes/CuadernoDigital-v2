package com.campos.gestionparcelas.model.repository;

import com.campos.gestionparcelas.model.entity.Cultivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CultivoRepository extends JpaRepository<Cultivo, Long> {
}
