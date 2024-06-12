package ar.edu.utn.frc.tup.lc.iv.services;

import ar.edu.utn.frc.tup.lc.iv.dtos.request.CrearReservaRequest;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.ReservaDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.ReservaResumenResponse;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Service para ABMC de reservas.
 */
@Service
public interface ReservaService {
    /**
     * crearReserva.
     *
     * @param crearReservaRequest datos de la reserva a crear
     * @return ReservaDto
     */
    ReservaResumenResponse crearReserva(
            CrearReservaRequest crearReservaRequest);

    /**
     * consultarReserva.
     *
     * @param id id de la resrva a consultar
     * @return ReservaDto
     */
    ReservaDto consultarReserva(Long id);

    /**
     * listarReservas.
     *
     * @return List<ReservaDto>
     */
    List<ReservaResumenResponse> listarReservas();

    /**
     * modificarReservas.
     *
     * @param reservaId id de la reserva a modificar
     * @param crearReservaRequest datos de la reserva a modificar
     * @return ReservaResumenResponse
     */
    ReservaResumenResponse modificarReserva(
            CrearReservaRequest crearReservaRequest, Long reservaId);

    /**
     * eliminarReserva.
     *
     * @param reservaId id de la reserva a eliminar
     * @return ReservaResumenResponse
     */
    ReservaResumenResponse eliminarReserva(Long reservaId);

    /**
     * confirmarReserva.
     *
     * @param reservaId id de la reserva a confirmar
     * @return ReservaResumenResponse
     */
    ReservaResumenResponse confirmarReserva(Long reservaId);
}
