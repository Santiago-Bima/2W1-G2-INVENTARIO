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
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa la entidad DetalleRemito en la base de datos.
 */
@Entity
@Table(name = "detalles_remito")
@Data
@NoArgsConstructor
@Generated
public class DetalleRemitoEntity {
    /**
     * Identificador único de la entidad DetalleRemito.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Remito al que está asociado este detalle de remito.
     */
    @ManyToOne
    @JoinColumn(name = "id_remito")
    private RemitoEntity remito;

    /**
     * Cantidad del producto en este detalle de remito.
     */
    @Column(name = "cantidad")
    private double cantidad;

    /**
     * Nombre del producto en este detalle de remito.
     */
    @Column(name = "nombre_producto")
    private String nombreProducto;

    /**
     * Descripción adicional sobre este detalle de remito.
     */
    @Column(name = "detalle")
    private String detalle;
}
