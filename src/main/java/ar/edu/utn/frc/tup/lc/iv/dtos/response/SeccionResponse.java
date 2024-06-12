package ar.edu.utn.frc.tup.lc.iv.dtos.response;

import ar.edu.utn.frc.tup.lc.iv.Generated;
import com.fasterxml.jackson.annotation.JsonProperty;
import jdk.jfr.Description;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Generated
public class SeccionResponse {
  /**
   * id de la seccion.
   */
  @Description("Section id")
  private Long id;
  /**
   * nombre de la seccion.
   */
  @Description("Section name")
  @JsonProperty("name")
  private String nombre;
  /**
   * nombre de la zona de la seccion.
   */
  @Description("Section Zone name")
  @JsonProperty("zone")
  private String nombreZona;
}
