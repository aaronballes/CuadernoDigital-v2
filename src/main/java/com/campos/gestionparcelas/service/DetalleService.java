package com.campos.gestionparcelas.service;

import com.campos.gestionparcelas.model.entity.Detalle;
import com.campos.gestionparcelas.model.entity.DetalleId;
import com.campos.gestionparcelas.model.repository.DetalleRepository;
import com.campos.gestionparcelas.model.repository.EjercicioRepository;
import com.campos.gestionparcelas.model.repository.ParcelaRepository;
import com.campos.gestionparcelas.model.repository.CultivoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

/**
 * Servicio para gestionar los detalles (relación parcela-ejercicio-cultivo).
 * 
 * Proporciona métodos para:
 * - Obtener la matriz completa de parcelas × ejercicios
 * - Filtrar por ejercicio específico
 * - Guardar/asignar cultivos a parcelas
 * - Eliminar asignaciones
 * - Detectar monocultivo (3+ ejercicios consecutivos con el mismo cultivo)
 */
@Service
public class DetalleService {
    
    private final DetalleRepository detalleRepository;
    private final EjercicioRepository ejercicioRepository;
    private final ParcelaRepository parcelaRepository;
    private final CultivoRepository cultivoRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public DetalleService(DetalleRepository detalleRepository, 
                          EjercicioRepository ejercicioRepository,
                          ParcelaRepository parcelaRepository,
                          CultivoRepository cultivoRepository) {
        this.detalleRepository = detalleRepository;
        this.ejercicioRepository = ejercicioRepository;
        this.parcelaRepository = parcelaRepository;
        this.cultivoRepository = cultivoRepository;
    }
    
    /**
     * Obtiene la matriz completa de parcelas × ejercicios con sus cultivos asignados.
     * 
     * Utiliza CROSS JOIN para generar todas las combinaciones parcela-ejercicio,
     * y LEFT JOIN con la tabla detalles para obtener el cultivo asignado (si existe).
     * 
     * @return Lista de mapas con datos de parcela, ejercicio y cultivo
     */
    public List<Map<String, Object>> obtenerMatriz() {
        List<Map<String, Object>> result = new ArrayList<>();
        
        // Consulta principal: matriz parcela × ejercicio con cultivo
        String sql = "SELECT P.PARCELA_ID, P.NOMBRE, P.POLIGONO, P.PARCELA, P.SUPERFICIE, " +
                     "NVL(PO.PROPIETARIO_ID, 0) as PROPIETARIO_ID, NVL(PO.PROPIETARIO, '') as PROPIETARIO, " +
                     "E.EJERCICIO_ID, E.EJERCICIO, NVL(C.CULTIVO_ID, 0) as CULTIVO_ID, NVL(C.CULTIVO, '') as CULTIVO " +
                     "FROM PARCELAS P " +
                     "LEFT JOIN PROPIETARIOS PO ON P.PROPIETARIO_ID = PO.PROPIETARIO_ID " +
                     "CROSS JOIN EJERCICIOS E " +
                     "LEFT JOIN DETALLES D ON D.PARCELA_ID = P.PARCELA_ID AND D.EJERCICIO_ID = E.EJERCICIO_ID " +
                     "LEFT JOIN CULTIVOS C ON C.CULTIVO_ID = D.CULTIVO_ID " +
                     "ORDER BY PO.PROPIETARIO, E.EJERCICIO DESC, P.NOMBRE";
        
        @SuppressWarnings("unchecked")
        List<Object[]> rows = entityManager.createNativeQuery(sql).getResultList();
        
        // Obtener parcelas con detección de monocultivo
        Map<Long, List<String>> cultivosPorParcela = new LinkedHashMap<>();
        Map<Long, Boolean> parcelasAlerta = new HashMap<>();
        
        String cultivosSql = "SELECT PARCELA_ID, NVL(CULTIVO_ID, 0) FROM DETALLES ORDER BY PARCELA_ID, EJERCICIO_ID DESC";
        List<?> cultivosRaw = entityManager.createNativeQuery(cultivosSql).getResultList();
        
        for (Object row : cultivosRaw) {
            if (row instanceof Object[]) {
                Object[] arr = (Object[]) row;
                Long parcelaId = ((Number) arr[0]).longValue();
                String cultivoId = String.valueOf(arr[1]);
                cultivosPorParcela.computeIfAbsent(parcelaId, k -> new ArrayList<>()).add(cultivoId);
            }
        }
        
        // Calcular alertas de monocultivo (3+ ejercicios consecutivos)
        for (Map.Entry<Long, List<String>> entry : cultivosPorParcela.entrySet()) {
            List<String> cultivos = entry.getValue();
            parcelasAlerta.put(entry.getKey(), tieneRepeticionConsecutiva(cultivos, 3));
        }
        
        // Construir resultado con marca de alerta
        for (Object[] row : rows) {
            Map<String, Object> item = new HashMap<>();
            Long parcelaId = ((Number) row[0]).longValue();
            item.put("parcelaId", parcelaId);
            item.put("nombre", row[1]);
            item.put("poligono", row[2]);
            item.put("parcela", row[3]);
            item.put("superficie", row[4]);
            item.put("propietarioId", ((Number) row[5]).longValue());
            item.put("propietario", row[6]);
            item.put("ejercicioId", String.valueOf(row[7]));
            item.put("ejercicio", row[8]);
            item.put("cultivoId", ((Number) row[9]).longValue());
            item.put("cultivo", row[10]);
            item.put("alertaRepeticion", parcelasAlerta.getOrDefault(parcelaId, false));
            result.add(item);
        }
        
        return result;
    }
    
    /**
     * Obtiene la matriz filtrada por un ejercicio específico.
     * 
     * @param ejercicioId ID del ejercicio a filtrar
     * @return Lista de mapas con datos del ejercicio seleccionado
     */
    public List<Map<String, Object>> listarConDetallesPorEjercicio(String ejercicioId) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        String sql = "SELECT P.PARCELA_ID, P.NOMBRE, P.POLIGONO, P.PARCELA, P.SUPERFICIE, " +
                     "NVL(PO.PROPIETARIO_ID, 0) as PROPIETARIO_ID, NVL(PO.PROPIETARIO, '') as PROPIETARIO, " +
                     "E.EJERCICIO_ID, E.EJERCICIO, NVL(C.CULTIVO_ID, 0) as CULTIVO_ID, NVL(C.CULTIVO, '') as CULTIVO " +
                     "FROM PARCELAS P " +
                     "LEFT JOIN PROPIETARIOS PO ON P.PROPIETARIO_ID = PO.PROPIETARIO_ID " +
                     "CROSS JOIN EJERCICIOS E " +
                     "LEFT JOIN DETALLES D ON D.PARCELA_ID = P.PARCELA_ID AND D.EJERCICIO_ID = E.EJERCICIO_ID " +
                     "LEFT JOIN CULTIVOS C ON C.CULTIVO_ID = D.CULTIVO_ID " +
                     "WHERE E.EJERCICIO_ID = '" + ejercicioId + "' " +
                     "ORDER BY PO.PROPIETARIO, P.NOMBRE";
        
        @SuppressWarnings("unchecked")
        List<Object[]> rows = entityManager.createNativeQuery(sql).getResultList();
        
        // Reutilizar lógica de detección de monocultivo
        Map<Long, List<String>> cultivosPorParcela = new LinkedHashMap<>();
        Map<Long, Boolean> parcelasAlerta = new HashMap<>();
        
        String cultivosSql = "SELECT PARCELA_ID, NVL(CULTIVO_ID, 0) FROM DETALLES ORDER BY PARCELA_ID, EJERCICIO_ID DESC";
        List<?> cultivosRaw = entityManager.createNativeQuery(cultivosSql).getResultList();
        
        for (Object row : cultivosRaw) {
            if (row instanceof Object[]) {
                Object[] arr = (Object[]) row;
                Long parcelaId = ((Number) arr[0]).longValue();
                String cultivoId = String.valueOf(arr[1]);
                cultivosPorParcela.computeIfAbsent(parcelaId, k -> new ArrayList<>()).add(cultivoId);
            }
        }
        
        for (Map.Entry<Long, List<String>> entry : cultivosPorParcela.entrySet()) {
            List<String> cultivos = entry.getValue();
            parcelasAlerta.put(entry.getKey(), tieneRepeticionConsecutiva(cultivos, 3));
        }
        
        for (Object[] row : rows) {
            Map<String, Object> item = new HashMap<>();
            Long parcelaId = ((Number) row[0]).longValue();
            item.put("parcelaId", parcelaId);
            item.put("nombre", row[1]);
            item.put("poligono", row[2]);
            item.put("parcela", row[3]);
            item.put("superficie", row[4]);
            item.put("ejercicioId", String.valueOf(row[5]));
            item.put("ejercicio", row[6]);
            item.put("cultivoId", ((Number) row[7]).longValue());
            item.put("cultivo", row[8]);
            item.put("alertaRepeticion", parcelasAlerta.getOrDefault(parcelaId, false));
            result.add(item);
        }
        
        return result;
    }
    
    /**
     * Guarda o actualiza la asignación de un cultivo a una parcela-ejercicio.
     * 
     * Utiliza DELETE + INSERT en lugar de UPDATE para manejar correctamente
     * la clave primaria compuesta.
     * 
     * @param detalle Datos del detalle a guardar
     * @return Mapa con información de alerta si se detecta monocultivo
     */
    @Transactional
    public Map<String, Object> guardar(Detalle detalle) {
        Map<String, Object> respuesta = new HashMap<>();
        Long parcelaId = detalle.getParcelaId();
        String ejercicioId = detalle.getEjercicioId();
        Long cultivoId = detalle.getCultivoId();
        
        // Si cultivo es 0 o null, eliminar el registro existente
        if (cultivoId == null || cultivoId == 0) {
            String deleteSql = "DELETE FROM DETALLES WHERE PARCELA_ID = " + parcelaId + " AND EJERCICIO_ID = '" + ejercicioId + "'";
            entityManager.createNativeQuery(deleteSql).executeUpdate();
        } else {
            // Eliminar registro existente (si hay) y crear nuevo
            String deleteSql = "DELETE FROM DETALLES WHERE PARCELA_ID = " + parcelaId + " AND EJERCICIO_ID = '" + ejercicioId + "'";
            entityManager.createNativeQuery(deleteSql).executeUpdate();
            
            String insertSql = "INSERT INTO DETALLES (PARCELA_ID, EJERCICIO_ID, CULTIVO_ID) VALUES (" + parcelaId + ", '" + ejercicioId + "', " + cultivoId + ")";
            entityManager.createNativeQuery(insertSql).executeUpdate();
        }
        
        // Verificar si hay monocultivo después de guardar
        String cultivosSql = "SELECT NVL(CULTIVO_ID, 0) FROM DETALLES WHERE PARCELA_ID = " + parcelaId + " ORDER BY EJERCICIO_ID DESC";
        List<?> cultivosRaw = entityManager.createNativeQuery(cultivosSql).getResultList();
        
        List<String> cultivos = new ArrayList<>();
        for (Object row : cultivosRaw) {
            if (row instanceof Object[]) {
                cultivos.add(String.valueOf(((Object[]) row)[0]));
            } else {
                cultivos.add(String.valueOf(row));
            }
        }
        
        boolean alerta = tieneRepeticionConsecutiva(cultivos, 3);
        respuesta.put("alerta", alerta);
        respuesta.put("mensaje", alerta ? "Esta parcela tiene 3 ejercicios consecutivos con el mismo cultivo. Posible monocultivo." : null);
        
        return respuesta;
    }
    
    /**
     * Elimina un detalle específico de la base de datos.
     * 
     * @param id Clave compuesta del detalle a eliminar
     */
    @Transactional
    public void eliminar(DetalleId id) {
        String sql = "DELETE FROM DETALLES WHERE PARCELA_ID = " + id.getParcelaId() + 
                     " AND EJERCICIO_ID = '" + id.getEjercicioId() + "'";
        entityManager.createNativeQuery(sql).executeUpdate();
    }
    
    private boolean tieneRepeticionConsecutiva(List<String> cultivos, int minimo) {
        if (cultivos.size() < minimo) return false;
        
        for (int i = 0; i <= cultivos.size() - minimo; i++) {
            String cultivo = cultivos.get(i);
            if (cultivo == null || cultivo.equals("0") || cultivo.isEmpty()) continue;
            
            boolean todosIguales = true;
            for (int j = i; j < i + minimo; j++) {
                if (!cultivos.get(j).equals(cultivo)) {
                    todosIguales = false;
                    break;
                }
            }
            if (todosIguales) return true;
        }
        return false;
    }
}
