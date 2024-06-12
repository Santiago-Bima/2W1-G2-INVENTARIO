package ar.edu.utn.frc.tup.lc.iv.dtos.request;

import ar.edu.utn.frc.tup.lc.iv.Generated;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa una solicitud para modificar un informe de control de stock.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Generated
public class ModificarControlStockRequest {
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
}
