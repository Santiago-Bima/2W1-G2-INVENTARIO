package ar.edu.utn.frc.tup.lc.iv.dtos.response;

import ar.edu.utn.frc.tup.lc.iv.Generated;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Representa un detalle de una reserva.
 */
@Data
@AllArgsConstructor
@Generated
public class DetalleResumenReserva {
    /**
     * Existencia.
     */
    @JsonProperty("existence")
    private ExistenciaDto existencia;
    /**
     * cantidad.
     */
    @JsonProperty("quantity")
    private double cantidad;
}
