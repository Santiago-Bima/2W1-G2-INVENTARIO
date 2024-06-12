package ar.edu.utn.frc.tup.lc.iv.dtos.response;

import ar.edu.utn.frc.tup.lc.iv.Generated;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa un detalle de una reserva.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Generated
public class DetalleReservaDto {
    /**
     * Lote desde el cual se reserva.
     */
    @JsonProperty("batch")
    private LoteResponse lote;

    /**
     * Cantidad reservada.
     */
    @JsonProperty("quantity")
    private double cantidad;

}
