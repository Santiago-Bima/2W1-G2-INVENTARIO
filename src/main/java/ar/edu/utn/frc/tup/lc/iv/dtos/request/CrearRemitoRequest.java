package ar.edu.utn.frc.tup.lc.iv.dtos.request;

import ar.edu.utn.frc.tup.lc.iv.Generated;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa una solicitud para crear un remito.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "CreateShipmentRequest")
@Generated
public class CrearRemitoRequest {
    /**
     * Número de remito, un identificador único asociado a un proveedor.
     */
    @Schema(
            title = "receipt number",
            example = "1",
            description = "Shipping number, a unique identifier "
                    + "associated with a supplier."
                    + " It can be repeated between suppliers, but a supplier "
                    + "cannot have repeated shipping numbers."
    )
    @NotNull
    @JsonProperty("receiptNumber")
    private Long nroRemito;

    /**
     * Fecha de llegada del remito.
     */
    @Schema(
            title = "receipt arrive date",
            example = "2023-10-26T01:13:32.512Z",
            description = "Shipment arrive date",
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    )
    @NotNull
    @JsonProperty("arribalDate")
    private LocalDateTime fechaLlegada;

    /**
     * Número de orden de compra asociado al remito.
     */
    @NotNull
    @JsonProperty("purchaseOrderNumber")
    private int nroOrdenCompra;

    /**
     * Descripción asociada al remito.
     */
    @Schema(
            title = "receipt Description",
            example = "some description",
            description = "Description that reflects details of the shipment"
    )
    @NotNull
    @NotBlank
    @JsonProperty("observations")
    private String observaciones;

    /**
     * Nombre del proveedor que emitió el remito.
     */
    @Schema(
            title = "Supplier name",
            example = "some name",
            description = "Name of the provider who issued the shipment."
    )
    @NotNull
    @NotBlank
    @JsonProperty("supplierName")
    private String nombreProveedor;

    /**
     * Lista de detalles de remito asociados a este remito.
     */
    @NotNull
    @JsonProperty("details")
    private List<CrearDetalleRemitoRequest> detalles;
}

