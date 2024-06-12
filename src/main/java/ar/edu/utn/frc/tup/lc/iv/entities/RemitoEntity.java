package ar.edu.utn.frc.tup.lc.iv.entities;

import ar.edu.utn.frc.tup.lc.iv.Generated;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Clase que representa la entidad Remito en la base de datos.
 */
@Entity
@Table(name = "remitos")
@Data
@NoArgsConstructor
@Generated
public class RemitoEntity {
  /**
   * Identificador único de la entidad Remito.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  /**
   * Fecha de llegada del remito.
   */
  @Column(name = "fecha_llegada")
  private LocalDateTime fechaLlegada;

  /**
   * Número de remito.
   */
  @Column(name = "nro_remito")
  private Long nroRemito;

  /**
   * Número de orden de compra asociado al remito.
   */
  @Column(name = "nro_orden_compra")
  private int nroOrdenCompra;

  /**
   * Campo que indica si la recepción del remito fue rechazada o no.
   */
  @Column(name = "rechazado")
  private boolean rechazado;

  /**
   * Descripción asociada a la recepción del remito.
   */
  @Column(name = "observaciones")
  private String observaciones;

  /**
   * Nombre del proveedor que emitió el remito.
   */
  @Column(name = "nombre_proveedor")
  private String nombreProveedor;

  /**
   * Lista de detalles de remito asociados a este remito.
   */
  @OneToMany(mappedBy = "remito", cascade = CascadeType.ALL,
          orphanRemoval = true)
  private List<DetalleRemitoEntity> detalles;
}
