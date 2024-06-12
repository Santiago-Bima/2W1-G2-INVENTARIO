package ar.edu.utn.frc.tup.lc.iv.dtos.response;

import ar.edu.utn.frc.tup.lc.iv.Generated;
import com.fasterxml.jackson.annotation.JsonProperty;
import jdk.jfr.Description;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa la respuesta de una existencia con su stock total y minimo.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Generated
public class ExistenciaBajaDto {

    /**
     * id.
     */
    @Description("The Existence's id")
    @JsonProperty("code")
    private String codigo;

    /**
     * nombre.
     */
    @Description("Existence's name")
    @JsonProperty("name")
    private String nombre;

    /**
     * stockMinimo.
     */
    @Description("Existence's min quantity ")
    @JsonProperty("minimunStock")
    private double stockMinimo;

    /**
     * stockTotal.
     */
    @Description("Existence's quantity ")
    @JsonProperty("stock")
    private double stockTotal;
}
