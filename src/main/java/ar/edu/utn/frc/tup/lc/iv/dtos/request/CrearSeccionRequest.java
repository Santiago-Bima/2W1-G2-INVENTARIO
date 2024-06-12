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
 * SeccionRequest.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Generated
public class CrearSeccionRequest {
    /**
     * Nombre de la sección.
     */
    @Schema(title = "Section name",
            example = "A",
            description = "Name of the section"
    )
    @NotNull(message = "name can't be null")
    @NotBlank(message = "name can't be blank")
    @JsonProperty("name")
    private String nombre;

    /**
     * Identificador de la Zona de Almacenamiento donde se encuentra la sección.
     */
    @Schema(title = "zone Id",
            example = "Corralon",
            description = "Identifier of the Storage "
                    + "Area where the Section is located"
    )
    @NotNull(message = "id_zona_almacenamiento can't be null")
    @JsonProperty("zoneId")
    private Long idZonaAlmacenamiento;
}
