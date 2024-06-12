package ar.edu.utn.frc.tup.lc.iv.dtos.request;

import ar.edu.utn.frc.tup.lc.iv.Generated;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa una solicitud para modificar una existencia.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Generated
public class ModificarExistenciaRequestDto {
    /**
     * StockMinimo.
     */
    @Schema(title = "minim stock",
            example = "1",
            description = "Min Stock of the product in catalog"
    )
    @NotNull(message = "Min Stock  can't be null")
    @JsonProperty("minimunStock")
    private double stockMinimo;
}
