package ar.edu.utn.frc.tup.lc.iv.dtos.request;

import ar.edu.utn.frc.tup.lc.iv.Generated;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa una solicitud para crear un detalle de reserva.
 */
@Data
@Generated
@AllArgsConstructor
@NoArgsConstructor
public class DetalleCrearReservaRequest {
    /**
     * codigoExistencia.
     */
    @JsonProperty("existenceCode")
    private String codigoExistencia;

    /**
     * cantidad.
     */
    @JsonProperty("quantity")
    private double cantidad;
}
