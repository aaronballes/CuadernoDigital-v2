package com.campos.gestionparcelas.service;

import com.campos.gestionparcelas.model.entity.Propietario;
import com.campos.gestionparcelas.model.repository.PropietarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class PropietarioService {
    
    private final PropietarioRepository propietarioRepository;
    
    public PropietarioService(PropietarioRepository propietarioRepository) {
        this.propietarioRepository = propietarioRepository;
    }
    
    public List<Propietario> listar() {
        return propietarioRepository.findAll();
    }
    
    public Optional<Propietario> buscarPorId(Long id) {
        return propietarioRepository.findById(id);
    }
    
    @Transactional
    public Propietario guardar(Propietario propietario) {
        return propietarioRepository.save(propietario);
    }
    
    @Transactional
    public void eliminar(Long id) {
        propietarioRepository.deleteById(id);
    }
}