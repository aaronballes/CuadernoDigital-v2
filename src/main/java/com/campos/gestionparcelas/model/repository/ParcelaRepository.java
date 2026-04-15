package com.campos.gestionparcelas.model.repository;

import com.campos.gestionparcelas.model.entity.Parcela;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ParcelaRepository extends JpaRepository<Parcela, Long> {
    
    @Query("SELECT p FROM Parcela p ORDER BY p.propietarioId, p.poligono, p.parcela")
    List<Parcela> findAllOrderByPropietarioAndPoligonoAndParcela();
    
    List<Parcela> findByPropietarioIdOrderByPoligonoAscParcelaAsc(Long propietarioId);
    
    @Query("SELECT MAX(p.parcelaId) FROM Parcela p")
    Long findMaxParcelaId();
}
