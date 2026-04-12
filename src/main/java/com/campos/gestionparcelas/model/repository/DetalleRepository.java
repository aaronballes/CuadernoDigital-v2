package com.campos.gestionparcelas.model.repository;

import com.campos.gestionparcelas.model.entity.Detalle;
import com.campos.gestionparcelas.model.entity.DetalleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DetalleRepository extends JpaRepository<Detalle, DetalleId> {
    List<Detalle> findByParcelaId(Long parcelaId);
    List<Detalle> findByEjercicioId(Long ejercicioId);
}
