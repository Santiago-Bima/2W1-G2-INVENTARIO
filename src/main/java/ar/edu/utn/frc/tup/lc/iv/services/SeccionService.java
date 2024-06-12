package ar.edu.utn.frc.tup.lc.iv.services;

import ar.edu.utn.frc.tup.lc.iv.dtos.request.CrearSeccionRequest;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.SeccionResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Service de AMBC de secciones de almacenamiento.
 */
@Service
public interface SeccionService {
    /**
     * Método para crear secciones de almacenamiento.
     *
     * @param seccionRequest datos de la seccion a crear
     * @return SeccionResponse
     */
    SeccionResponse crearSeccion(CrearSeccionRequest seccionRequest)
            throws Exception;
    /**
     * Método para listar secciones filtrando por zona de almacenamiento.
     *
     * @param idZona id de la zona a filtrar
     * @return List<SeccionResponse>
     */
    List<SeccionResponse> getSeccionesByZona(String idZona);
    /**
     * Método para eliminar secciones.
     *
     * @param id id de la seccion a eliminar
     * @return ResponseEntity
     */
    ResponseEntity deleteSeccion(Long id) throws Exception;
    /**
     * Método para obtener zonas de almacenamiento.
     *
     * @param seccion datos de la secion a actualizar
     * @param id id de la seccion a actualizar
     * @return SeccionResponse
     */
    SeccionResponse updateSeccion(CrearSeccionRequest seccion, Long id);
}
