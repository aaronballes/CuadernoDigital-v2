package com.campos.gestionparcelas.service;

import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class EstadisticaService {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public List<Map<String, Object>> getSuperficiePorCultivoPorEjercicio() {
        List<Map<String, Object>> result = new ArrayList<>();
        
        String sql = 
            "SELECT " +
            "PR.PROPIETARIO_ID, " +
            "PR.PROPIETARIO, " +
            "E.EJERCICIO_ID, " +
            "E.EJERCICIO, " +
            "C.CULTIVO_ID, " +
            "C.CULTIVO, " +
            "SUM(P.SUPERFICIE) as SUPERFICIE_TOTAL " +
            "FROM DETALLES D " +
            "JOIN EJERCICIOS E ON D.EJERCICIO_ID = E.EJERCICIO_ID " +
            "JOIN CULTIVOS C ON D.CULTIVO_ID = C.CULTIVO_ID " +
            "JOIN PARCELAS P ON D.PARCELA_ID = P.PARCELA_ID " +
            "LEFT JOIN PROPIETARIOS PR ON P.PROPIETARIO_ID = PR.PROPIETARIO_ID " +
            "GROUP BY PR.PROPIETARIO_ID, PR.PROPIETARIO, E.EJERCICIO_ID, E.EJERCICIO, C.CULTIVO_ID, C.CULTIVO " +
            "ORDER BY PR.PROPIETARIO_ID, E.EJERCICIO DESC, SUPERFICIE_TOTAL DESC";
        
        @SuppressWarnings("unchecked")
        List<Object[]> rows = entityManager.createNativeQuery(sql).getResultList();
        
        Map<String, Map<String, Map<Long, BigDecimal>>> propietarioEjercicioCultivo = new LinkedHashMap<>();
        Map<String, Map<String, BigDecimal>> propietarioEjercicioTotal = new LinkedHashMap<>();
        
        for (Object[] row : rows) {
            Long propietarioId = row[0] != null ? ((Number) row[0]).longValue() : null;
            String propietarioNombre = row[1] != null ? String.valueOf(row[1]) : "Sin propietario";
            String ejercicioId = String.valueOf(row[2]);
            String ejercicio = String.valueOf(row[3]);
            Long cultivoId = ((Number) row[4]).longValue();
            String cultivo = String.valueOf(row[5]);
            BigDecimal superficie = row[6] != null ? new BigDecimal(String.valueOf(row[6])) : BigDecimal.ZERO;
            
            String propKey = propietarioId != null ? propietarioId.toString() : "sin_propietario";
            
            propietarioEjercicioCultivo
                .computeIfAbsent(propKey, k -> new LinkedHashMap<>())
                .computeIfAbsent(ejercicioId, k -> new HashMap<>())
                .put(cultivoId, superficie);
            
            propietarioEjercicioTotal
                .computeIfAbsent(propKey, k -> new LinkedHashMap<>())
                .merge(ejercicioId, superficie, BigDecimal::add);
        }
        
        Map<String, String> ejercicioNombres = new HashMap<>();
        Map<String, String> cultivoNombres = new HashMap<>();
        for (Object[] row : rows) {
            String ejercicioId = String.valueOf(row[2]);
            String ejercicio = String.valueOf(row[3]);
            Long cultivoId = ((Number) row[4]).longValue();
            String cultivo = String.valueOf(row[5]);
            ejercicioNombres.put(ejercicioId, ejercicio);
            cultivoNombres.put(cultivoId.toString(), cultivo);
        }
        
        for (Map.Entry<String, Map<String, Map<Long, BigDecimal>>> propEntry : propietarioEjercicioCultivo.entrySet()) {
            String propKey = propEntry.getKey();
            Map<String, Map<Long, BigDecimal>> ejercicios = propEntry.getValue();
            
            String nombrePropietario = "Sin propietario";
            for (Object[] row : rows) {
                Long propId = row[0] != null ? ((Number) row[0]).longValue() : null;
                String pk = propId != null ? propId.toString() : "sin_propietario";
                if (pk.equals(propKey)) {
                    nombrePropietario = row[1] != null ? String.valueOf(row[1]) : "Sin propietario";
                    break;
                }
            }
            
            for (Map.Entry<String, Map<Long, BigDecimal>> ejerEntry : ejercicios.entrySet()) {
                String ejercicioId = ejerEntry.getKey();
                Map<Long, BigDecimal> cultivos = ejerEntry.getValue();
                BigDecimal total = propietarioEjercicioTotal.get(propKey).get(ejercicioId);
                
                for (Map.Entry<Long, BigDecimal> cultivoEntry : cultivos.entrySet()) {
                    Long cultivoId = cultivoEntry.getKey();
                    BigDecimal superficie = cultivoEntry.getValue();
                    BigDecimal porcentaje = total.compareTo(BigDecimal.ZERO) > 0 
                        ? superficie.multiply(BigDecimal.valueOf(100)).divide(total, 2, RoundingMode.HALF_UP) 
                        : BigDecimal.ZERO;
                    
                    Map<String, Object> item = new LinkedHashMap<>();
                    item.put("propietarioId", propKey.equals("sin_propietario") ? null : Long.parseLong(propKey));
                    item.put("propietario", nombrePropietario);
                    item.put("ejercicioId", ejercicioId);
                    item.put("ejercicio", ejercicioNombres.get(ejercicioId));
                    item.put("cultivoId", cultivoId);
                    item.put("cultivo", cultivoNombres.get(cultivoId.toString()));
                    item.put("superficie", superficie);
                    item.put("porcentaje", porcentaje);
                    item.put("total", total);
                    result.add(item);
                }
            }
        }
        
        return result;
    }
}