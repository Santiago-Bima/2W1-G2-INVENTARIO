package ar.edu.utn.frc.tup.lc.iv.dtos.response;

import ar.edu.utn.frc.tup.lc.iv.Generated;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Representa una respuesta para mostrar ell stock total reservado.
 */
@Data
@AllArgsConstructor
@Generated
public class ReservaResumenResponse {
    /**
     * id.
     */
    private Long id;
    /**
     * stockReservado.
     */
    @JsonProperty("reservedStock")
    private List<DetalleResumenReserva> stockReservado;
}
