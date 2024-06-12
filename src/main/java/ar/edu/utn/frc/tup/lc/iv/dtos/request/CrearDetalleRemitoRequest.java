package ar.edu.utn.frc.tup.lc.iv.dtos.request;

import ar.edu.utn.frc.tup.lc.iv.Generated;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa una solicitud para crear un detalle de remito.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "CreateShipmentDetailsRequest")
@Generated
public class CrearDetalleRemitoRequest {
    /**
     * Cantidad del producto en el detalle del remito.
     */
    @Schema(
            title = "Quantity",
            example = "5.0",
            description = "Product quantity in the detail."
    )
    @NotNull
    @JsonProperty("quantity")
    private double cantidad;

    /**
     * Nombre del producto en el detalle del remito.
     */
    @Schema(
            title = "Product Name",
            example = "Sample Product",
            description = "Product name in the detail."
    )
    @NotNull
    @NotBlank
    @JsonProperty("productName")
    private String nombreProducto;

    /**
     * Descripción adicional del producto en el detalle del remito.
     */
    @Schema(
            title = "Detail Description",
            example = "Sample detail description",
            description = "Additional product description in detail."
    )
    @JsonProperty("detail")
    private String detalle;
}
