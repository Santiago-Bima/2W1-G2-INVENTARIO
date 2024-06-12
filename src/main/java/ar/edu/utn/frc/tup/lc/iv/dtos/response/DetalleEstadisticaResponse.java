package ar.edu.utn.frc.tup.lc.iv.dtos.response;

import ar.edu.utn.frc.tup.lc.iv.Generated;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa la respuesta del detalle de una estadística.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Generated
public class DetalleEstadisticaResponse {
    /**
     * Nombre o identificador del elemento medido.
     */
    private String name;

    /**
     * Cantidad de controles realizados sobre el elemento.
     */
    private double controls;

    /**
     * Cantidad total de unidades relacionadas al elemento.
     */
    private double units;

    /**
     * Porcentaje calculado en base a los
     * controles realizados y las unidades relacionadas.
     */
    private BigDecimal percent;
}
