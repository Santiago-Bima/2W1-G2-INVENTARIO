package ar.edu.utn.frc.tup.lc.iv.dtos.response;

import ar.edu.utn.frc.tup.lc.iv.Generated;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa un DTO (Data Transfer Object) para detalles de remito.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Generated
public class DetalleRemitoDto {
    /**
     * Identificador único del detalle de remito.
     */
    private long id;

    /**
     * Cantidad del producto en este detalle de remito.
     */
    @JsonProperty("quantity")
    private double cantidad;

    /**
     * Nombre del producto en este detalle de remito.
     */
    @JsonProperty("productName")
    private String nombreProducto;

    /**
     * Descripción adicional sobre este detalle de remito.
     */
    @JsonProperty("detail")
    private String detalle;
}

