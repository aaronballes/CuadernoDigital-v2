package com.campos.gestionparcelas.service;

import com.campos.gestionparcelas.model.entity.Parcela;
import com.campos.gestionparcelas.model.repository.ParcelaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ParcelaService {
    
    private final ParcelaRepository parcelaRepository;
    
    public ParcelaService(ParcelaRepository parcelaRepository) {
        this.parcelaRepository = parcelaRepository;
    }
    
    public List<Parcela> listar() {
        return parcelaRepository.findAllOrderByPropietarioAndPoligonoAndParcela();
    }
    
    public Optional<Parcela> buscarPorId(Long id) {
        return parcelaRepository.findById(id);
    }
    
    @Transactional
    public Parcela guardar(Parcela parcela) {
        return parcelaRepository.save(parcela);
    }
    
    @Transactional
    public void eliminar(Long id) {
        parcelaRepository.deleteById(id);
    }
}
