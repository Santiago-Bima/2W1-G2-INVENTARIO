package ar.edu.utn.frc.tup.lc.iv.dtos.request;

import ar.edu.utn.frc.tup.lc.iv.Generated;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa una solicitud para la creación de un lote.
 */
@Builder
@NoArgsConstructor
@Data
@AllArgsConstructor
@Generated
public class LoteRequest {
  /**
   * Fecha de vencimiento del lote.
   */
  @Schema(title = "Expiration Date",
          example = "11/06/2023",
          description = "The date when the batch get expired"
  )
  @NotNull(message = "Date can't be null")
  @JsonProperty("dueDate")
  private LocalDateTime fechaVencimiento;

  /**
   * Cantidad total del producto dentro del lote.
   */
  @Schema(title = "Total of the product",
          example = "100",
          description = "The total quantity of the product inside the batch"
  )
  @NotNull(message = "Quantity can't be null")
  @NotBlank(message = "Quantity can't be blank")
  @JsonProperty("quantity")
  private double cantidad;

  /**
   * Número del estante donde se encuentra el lote.
   */
  @Schema(title = "Number of the shelf",
          example = "3",
          description = "The number of the shelf"
  )
  @NotNull(message = "Shelf can't be null")
  @NotBlank(message = "Shelf can't be blank")
  @JsonProperty("shelf")
  private Integer estante;

  /**
   * Código de existencia que identifica al producto al que pertenece el lote.
   */
  @Schema(title = "Existence Id",
          example = "1",
          description = "Number of the product which it belongs"
  )
  @NotNull(message = "Existence id can't be null")
  @NotBlank(message = "Existence id can't be blank")
  @JsonProperty("existenceCode")
  private String codigoExistencia;

  /**
   * Identificador de la sección donde se almacenará el lote.
   */
  @Schema(title = "Section id",
          example = "A",
          description = "Number of the Section where will be stored"
  )
  @NotNull(message = "Section id can't be null")
  @NotBlank(message = "Section id can't be blank")
  @JsonProperty("sectionId")
  private Long idSeccion;
}
