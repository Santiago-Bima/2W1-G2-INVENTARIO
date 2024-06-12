package ar.edu.utn.frc.tup.lc.iv.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

/**
 * entidad de reservas.
 */
@Entity
@Table(name = "reservas")
@Data
@NoArgsConstructor
@Generated
public class ReservaEntity {
  /**
   * Identificador único de este detalle de reserva.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  /**
   * Detalles asociados a la reserva.
   */
  @OneToMany(mappedBy = "reserva", cascade =
          CascadeType.ALL, orphanRemoval = true)
  private List<DetalleReservaEntity> detalles;
}
