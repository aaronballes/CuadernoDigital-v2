package com.campos.gestionparcelas.service;

import com.campos.gestionparcelas.model.entity.Ejercicio;
import com.campos.gestionparcelas.model.repository.EjercicioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class EjercicioService {
    
    private final EjercicioRepository ejercicioRepository;
    
    public EjercicioService(EjercicioRepository ejercicioRepository) {
        this.ejercicioRepository = ejercicioRepository;
    }
    
    public List<Ejercicio> listar() {
        return ejercicioRepository.findAll();
    }
    
    public Optional<Ejercicio> buscarPorId(String id) {
        return ejercicioRepository.findById(id);
    }
    
    @Transactional
    public Ejercicio guardar(Ejercicio ejercicio) {
        return ejercicioRepository.save(ejercicio);
    }
    
    @Transactional
    public void eliminar(String id) {
        ejercicioRepository.deleteById(id);
    }
}
