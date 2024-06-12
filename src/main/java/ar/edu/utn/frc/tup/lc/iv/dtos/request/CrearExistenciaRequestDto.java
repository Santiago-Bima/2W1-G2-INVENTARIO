package ar.edu.utn.frc.tup.lc.iv.dtos.request;

import ar.edu.utn.frc.tup.lc.iv.Generated;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa una solicitud para registrar una nueva existencia.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Generated
public class CrearExistenciaRequestDto {
    /**
     * productCode.
     */
    @Schema(title = "Existence Code",
            example = "1",
            description = "Code of the product in catalog"
    )
    @NotNull(message = "Code can't be null")
    @NotBlank(message = "Id can't be blank")
    @JsonProperty("code")
    private String codigoProducto;
    /**
     * nombre.
     */
    @Schema(title = "Existence name",
            example = "Bolts",
            description = "Name of the product in catalog"
    )
    @NotNull(message = "Name can't be null")
    @NotBlank(message = "Name can't be blank")
    @JsonProperty("name")
    private String nombre;
    /**
     * stockMinimo.
     */
    @Schema(title = "Min stock",
            example = "1",
            description = "Min Stock of the product in catalog"
    )
    @NotNull(message = "Min Stock  can't be null")
//    @NotBlank(message = "Min Stock  can't be blank")
    @JsonProperty("minimunStock")
    private double stockMinimo;
}
