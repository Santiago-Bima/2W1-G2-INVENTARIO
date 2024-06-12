package ar.edu.utn.frc.tup.lc.iv.dtos.response;

import ar.edu.utn.frc.tup.lc.iv.Generated;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa un DTO (Data Transfer Object) para remitos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Generated
public class RemitoDto {
    /**
     * Identificador único del remito.
     */
    private long id;

    /**
     * Número de remito.
     */
    @JsonProperty("receiptNumber")
    private Long nroRemito;

    /**
     * Número de orden de compra asociado al remito.
     */
    @JsonProperty("purchaseOrderNumber")
    private int nroOrdenCompra;

    /**
     * Descripción asociada al remito.
     */
    @JsonProperty("observations")
    private String observaciones;

    /**
     * Fecha de llegada del remito.
     */
    @JsonProperty("arrivalDate")
    private LocalDateTime fechaLlegada;

    /**
     * Nombre del proveedor que emitió el remito.
     */
    @JsonProperty("supplierName")
    private String nombreProveedor;

    /**
     * Lista de detalles de remito asociados a este remito.
     */
    @JsonProperty("details")
    private List<DetalleRemitoDto> detalles;
}

