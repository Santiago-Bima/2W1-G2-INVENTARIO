package ar.edu.utn.frc.tup.lc.iv.dtos.response;

import ar.edu.utn.frc.tup.lc.iv.Generated;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa la respuesta de una estadística.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Generated
public class EstadisticaResponse {
    /**
     * Fecha de inicio de la temporada estadística.
     */
    private LocalDateTime seasonStart;

    /**
     * Fecha de fin de la temporada estadística.
     */
    private LocalDateTime seasonEnd;

    /**
     * Detalles de la estadística, representados
     * por un arreglo de objetos DetalleEstadisticaResponse.
     */
    private DetalleEstadisticaResponse[] items;
}

