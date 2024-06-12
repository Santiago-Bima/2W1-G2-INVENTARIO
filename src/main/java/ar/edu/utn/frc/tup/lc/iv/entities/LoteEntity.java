package ar.edu.utn.frc.tup.lc.iv.entities;

import ar.edu.utn.frc.tup.lc.iv.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import jdk.jfr.Description;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "lotes")
@Generated
@AllArgsConstructor
@NoArgsConstructor
public class LoteEntity {
  /**
   * id del lote.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_lote", unique = true)
  @Description("Batch id")
  private Long id;
  /**
   * fecha de vencimiento del lote.
   */
  @Description("Batch due date")
  @Column(name = "fecha_vencimiento")
  private LocalDateTime fechaVencimiento;
  /**
   * cantidad del lote.
   */
  @Description("Batch quantity")
  private double cantidad;
  /**
   * estante en el que se almacena el lote.
   */
  @Description("Batch shelf")
  private Integer estante;
  /**
   * seccion en la que se encuentra almacenado.
   */
  @ManyToOne
  @Description("Batch's section")
  @JoinColumn(name = "id_seccion")
  private SeccionEntity seccion;
  /**
   * existencia a la cual pertenece.
   */
  @ManyToOne
  @Description("Batch's existence")
  @JoinColumn(name = "codigo_existencia")
  private ExistenciaEntity existencia;
}
