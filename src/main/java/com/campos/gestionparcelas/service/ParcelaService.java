package com.campos.gestionparcelas.service;

import com.campos.gestionparcelas.model.dto.ParcelaDTO;
import com.campos.gestionparcelas.model.entity.Parcela;
import com.campos.gestionparcelas.model.entity.Propietario;
import com.campos.gestionparcelas.model.repository.ParcelaRepository;
import com.campos.gestionparcelas.model.repository.PropietarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParcelaService {
    
    private final ParcelaRepository parcelaRepository;
    private final PropietarioRepository propietarioRepository;
    
    public ParcelaService(ParcelaRepository parcelaRepository, PropietarioRepository propietarioRepository) {
        this.parcelaRepository = parcelaRepository;
        this.propietarioRepository = propietarioRepository;
    }
    
    @Transactional(readOnly = true)
    public List<ParcelaDTO> listar() {
        return parcelaRepository.findAll().stream()
                .sorted(Comparator.comparing((Parcela p) -> p.getPropietario() != null && p.getPropietario().getPropietario() != null ? p.getPropietario().getPropietario() : "")
                        .thenComparing(Parcela::getPoligono)
                        .thenComparing(Parcela::getParcela))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    private ParcelaDTO toDTO(Parcela p) {
        ParcelaDTO dto = new ParcelaDTO();
        dto.setParcelaId(p.getParcelaId());
        dto.setNombre(p.getNombre());
        dto.setPoligono(p.getPoligono());
        dto.setParcela(p.getParcela());
        dto.setSuperficie(p.getSuperficie());
        if (p.getPropietario() != null) {
            dto.setPropietarioId(p.getPropietario().getPropietarioId());
            dto.setPropietarioNombre(p.getPropietario().getPropietario());
        }
        return dto;
    }
    
    @Transactional(readOnly = true)
    public Optional<Parcela> buscarPorId(Long id) {
        return parcelaRepository.findById(id);
    }
    
    @Transactional
    public Parcela guardar(Parcela parcela) {
        if (parcela.getPropietario() != null && parcela.getPropietario().getPropietarioId() != null) {
            Propietario propietario = propietarioRepository.findById(parcela.getPropietario().getPropietarioId()).orElse(null);
            parcela.setPropietario(propietario);
        }
        return parcelaRepository.save(parcela);
    }
    
    @Transactional
    public void eliminar(Long id) {
        parcelaRepository.deleteById(id);
    }
}
