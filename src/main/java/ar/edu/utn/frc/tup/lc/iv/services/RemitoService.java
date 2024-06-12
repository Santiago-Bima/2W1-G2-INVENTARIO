package ar.edu.utn.frc.tup.lc.iv.services;

import ar.edu.utn.frc.tup.lc.iv.dtos.request.CrearRemitoRequest;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.RemitoDto;
import java.util.List;

/**
 * Interfaz que define los servicios relacionados con remitos.
 */
public interface RemitoService {
    /**
     * Crea un nuevo remito a partir de una solicitud y lo
     * almacena en la base de datos.
     *
     * @param remito La solicitud para crear el remito.
     * @return El remito creado.
     */
    RemitoDto crearRemito(CrearRemitoRequest remito);

    /**
     * Obtiene una lista de todos los remitos almacenados en la base de datos.
     *
     * @return Una lista de remitos.
     */
    List<RemitoDto> listarRemitos();

    /**
     * Modifica un remito existente con la información proporcionada.
     *
     * @param remito El remito modificado.
     * @param id     El ID del remito a modificar.
     * @return El remito modificado.
     */
    RemitoDto modificarRemito(CrearRemitoRequest remito, Long id);
}
