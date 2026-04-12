package com.campos.gestionparcelas.service;

import com.campos.gestionparcelas.model.entity.Cultivo;
import com.campos.gestionparcelas.model.repository.CultivoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CultivoService {
    
    private final CultivoRepository cultivoRepository;
    
    public CultivoService(CultivoRepository cultivoRepository) {
        this.cultivoRepository = cultivoRepository;
    }
    
    public List<Cultivo> listar() {
        return cultivoRepository.findAll();
    }
    
    public Optional<Cultivo> buscarPorId(Long id) {
        return cultivoRepository.findById(id);
    }
    
    @Transactional
    public Cultivo guardar(Cultivo cultivo) {
        return cultivoRepository.save(cultivo);
    }
    
    @Transactional
    public void eliminar(Long id) {
        cultivoRepository.deleteById(id);
    }
}
