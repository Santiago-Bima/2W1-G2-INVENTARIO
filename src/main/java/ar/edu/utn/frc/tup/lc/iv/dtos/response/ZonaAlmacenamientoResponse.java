package ar.edu.utn.frc.tup.lc.iv.dtos.response;

import ar.edu.utn.frc.tup.lc.iv.Generated;
import com.fasterxml.jackson.annotation.JsonProperty;
import jdk.jfr.Description;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa la respuesta que proporciona
 * información sobre una Zona de Almacenamiento.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Generated
public class ZonaAlmacenamientoResponse {
    /**
     * Identificador de la Zona de Almacenamiento.
     */
    @Description("Storage zone id")
    @JsonProperty("id")
    private Long idZona;

    /**
     * Nombre de la Zona de Almacenamiento.
     */
    @Description("Storage zone name")
    @JsonProperty("name")
    private String nombreZona;
}
