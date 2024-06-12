package ar.edu.utn.frc.tup.lc.iv.dtos.response;

import ar.edu.utn.frc.tup.lc.iv.Generated;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa una petición para hacer una reserva.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Generated
public class ReservaDto {
    /**
     * Identificador de reserva.
     */
    private Long id;

    /**
     * Detalles de reserva.
     */
    @JsonProperty("details")
    private List<DetalleReservaDto> detalles;
}
