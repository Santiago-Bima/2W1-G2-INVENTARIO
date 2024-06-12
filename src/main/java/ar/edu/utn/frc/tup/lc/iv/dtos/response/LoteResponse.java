package ar.edu.utn.frc.tup.lc.iv.dtos.response;

import ar.edu.utn.frc.tup.lc.iv.Generated;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import jdk.jfr.Description;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa la respuesta detallada de un lote.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Generated
public class LoteResponse {
  /**
   * Identificación única del lote.
   */
  @Description("Batch's Id")
  private Long id;

  /**
   * Cantidad de productos en el lote.
   */
  @Description("Batch's quantity")
  @JsonProperty("quantity")
  private double cantidad;

  /**
   * Número de estante donde se encuentra el lote.
   */
  @Description("Batch's shelf")
  @JsonProperty("shelf")
  private Integer estante;

  /**
   * Sección a la que pertenece el lote.
   */
  @Description("Batch's section")
  @JsonProperty("section")
  private SeccionResponse seccion;

  /**
   * Información sobre la existencia del lote.
   */
  @Description("Batch's existence")
  @JsonProperty("existence")
  private ExistenciaDto existencia;

  /**
   * Fecha de vencimiento del lote.
   */
  @Description("Batch's due date")
  @JsonProperty("dueDate")
  private LocalDateTime fechaVencimiento;
}
