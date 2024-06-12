package ar.edu.utn.frc.tup.lc.iv.dtos.response;

import ar.edu.utn.frc.tup.lc.iv.Generated;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jdk.jfr.Description;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa un objeto de transferencia de datos
 * que proporciona información sobre un producto en el catálogo.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Generated
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductoCatalogoDto {
    /**
     * Identificador único del producto.
     */
    @Description("Product id")
    private String codigo;

    /**
     * Nombre del producto.
     */
    @Description("Product name")
    private String nombre;
}
