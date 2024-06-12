package ar.edu.utn.frc.tup.lc.iv.dtos.request;

import ar.edu.utn.frc.tup.lc.iv.Generated;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa una solicitud para crear un informe de control de stock.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Generated
public class CrearControlStockRequest {
    /**
     * Referencia al lote asociado.
     */
    @JsonProperty("batchId")
    private Long loteId;

    /**
     * Descripción del control de stock.
     */
    @JsonProperty("description")
    private String descripcion;

    /**
     * Cantidad de productos inspeccionados en el lote.
     */
    @JsonProperty("inspectedQuantity")
    private double cantidadInspeccionada;

    /**
     * Cantidad de productos dañados en el lote.
     */
    @JsonProperty("damagedQuantity")
    private double cantidadDaniada;

    /**
     * Indica si el lote está vencido o no.
     */
    @JsonProperty("batchIsExpired")
    private boolean loteVencido;

    /**
     * Indica la fecha de creación del control de stock.
     */
    @JsonProperty("date")
    private LocalDateTime fecha;
}
