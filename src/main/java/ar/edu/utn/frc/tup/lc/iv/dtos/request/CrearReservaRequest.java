package ar.edu.utn.frc.tup.lc.iv.dtos.request;

import ar.edu.utn.frc.tup.lc.iv.Generated;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

/**
 * Representa una solicitud para crear una reserva.
 */
@Data
@Generated
public class CrearReservaRequest {
    /**
     * stockReservado.
     */
    @JsonProperty("details")
    private List<DetalleCrearReservaRequest> stockReservado;
}
