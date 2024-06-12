package ar.edu.utn.frc.tup.lc.iv.services;

import ar.edu.utn.frc.tup.lc.iv.dtos.request.CrearControlStockRequest;
import ar.edu.utn.frc.tup.lc.iv.dtos.request.ModificarControlStockRequest;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.ControlStockResponse;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.EstadisticaResponse;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Interfaz que define las operaciones relacionadas con el control de stock.
 */
@Service
public interface
ControlStockService {

    /**
     * Crea un informe de control de stock.
     *
     * @param request La solicitud para crear un informe de control de stock.
     * @return El informe de control de stock creado.
     */
    ControlStockResponse crearControlStock(CrearControlStockRequest request);

    /**
     * Modifica un informe de control de stock existente.
     *
     * @param idControl El ID del informe de control de stock a modificar.
     * @param request    La solicitud para modificar.
     * @return El informe de control de stock modificado.
     */
    ControlStockResponse modificarControlStock(Long idControl,
                         ModificarControlStockRequest request);

    /**
     * Elimina un informe de control de stock existente.
     *
     * @param idControl El ID del informe de control de stock a eliminar.
     * @return El informe de control de stock eliminado.
     */
    ControlStockResponse eliminarControlStock(Long idControl);

    /**
     * Lista los informes de control de stock del último mes.
     *
     * @return Una lista de informes de control de stock del último mes.
     */
    List<ControlStockResponse> listarControlesDelUltimoMes();

    /**
     * Lista los informes de control de stock.
     *
     * @return Una lista de informes de control de stock.
     */
    ControlStockResponse[] getControlesDeStock();


    /**
     * Lista los informes de existencias totales dañadas.
     *
     * @param tipo tipo de estadistica a obtener
     * @return Una lista de informes de control de stock.
     */
    EstadisticaResponse getEstadisticas(String tipo);

}
