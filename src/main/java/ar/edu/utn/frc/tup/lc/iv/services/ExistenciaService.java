package ar.edu.utn.frc.tup.lc.iv.services;

import ar.edu.utn.frc.tup.lc.iv.dtos.request.CrearExistenciaRequestDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.request.ModificarExistenciaRequestDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.ExistenciaBajaDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.ExistenciaDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.ExistenciaTotalResponse;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.ProductoCatalogoDto;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * ABMC de existencias.
 */
@Service
public interface ExistenciaService {
    /**
     * @return ProductoCatalogoDto[]
     * consultarCatalogo.
     */
    ProductoCatalogoDto[] consultarCatalogo();
    /**
     * crearExistencia.
     *
     * @param existenciaNueva datos de la existencia nueva
     * @return ExistenciaDto
     */
    ExistenciaDto crearExistencia(CrearExistenciaRequestDto existenciaNueva)
            throws Exception;
    /**
     * modificarExistencia.
     *
     * @param codigo codigo de la existencia a modificar
     * @param existenciaModificada datos de la existencia a modificar
     * @return ExistenciaDto
     */
    ExistenciaDto modificarExistencia(
            String codigo,
            ModificarExistenciaRequestDto existenciaModificada);
    /**
     * eliminarExistencia.
     *
     * @param codigo codigo de la existencia a eliminar
     * @return ExistenciaDto
     */
    ExistenciaDto eliminarExistencia(String codigo);
    /**
     * listar existencias con stock bajo.
     *
     * @return List<ExistenciaBajaDto>
     */
    List<ExistenciaBajaDto> getExistenciasBajas();
    /**
     * getTotalExistencia.
     *
     * @param codigo codigo de la existencia a buscar
     * @return ExistenciaTotalResponse
     */
    ExistenciaTotalResponse getTotalExistencia(String codigo);
    /**
     * @return List<ExistenciaTotalResponse>
     * getTotalExistencias.
     */
    List<ExistenciaTotalResponse> getTotalExistencias();
}
