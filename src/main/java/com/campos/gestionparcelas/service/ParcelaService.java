package com.campos.gestionparcelas.service;

import com.campos.gestionparcelas.model.entity.Parcela;
import com.campos.gestionparcelas.model.repository.ParcelaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Service
public class ParcelaService {
    
    private final ParcelaRepository parcelaRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public ParcelaService(ParcelaRepository parcelaRepository) {
        this.parcelaRepository = parcelaRepository;
    }
    
    @Transactional(readOnly = true)
    public List<Parcela> listar() {
        return parcelaRepository.findAllOrderByPropietarioAndPoligonoAndParcela();
    }
    
    @Transactional(readOnly = true)
    public List<Parcela> listarPorPropietario(Long propietarioId) {
        return parcelaRepository.findByPropietarioIdOrderByPoligonoAscParcelaAsc(propietarioId);
    }
    
    @Transactional(readOnly = true)
    public Optional<Parcela> buscarPorId(Long id) {
        return parcelaRepository.findById(id);
    }
    
    @Transactional
    public Parcela guardar(Parcela parcela) {
        if (parcela.getParcelaId() == null) {
            Long maxId = parcelaRepository.findMaxParcelaId();
            parcela.setParcelaId(maxId == null ? 1L : maxId + 1);
        }
        return parcelaRepository.save(parcela);
    }
    
    @Transactional
    public void eliminar(Long id) {
        parcelaRepository.deleteById(id);
    }
}