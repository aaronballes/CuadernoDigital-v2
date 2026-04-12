package com.campos.gestionparcelas.model.repository;

import com.campos.gestionparcelas.model.entity.Parcela;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ParcelaRepository extends JpaRepository<Parcela, Long> {
    
    @Query("SELECT p FROM Parcela p LEFT JOIN p.propietario ORDER BY p.poligono, p.parcela")
    List<Parcela> findAllOrderByPropietarioAndPoligonoAndParcela();
}
