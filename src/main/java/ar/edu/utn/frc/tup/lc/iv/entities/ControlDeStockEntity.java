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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa el control de stock de un lote de productos.
 */
@Entity
@Table(name = "control_stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Generated
public class ControlDeStockEntity {
    /**
     * Identificador único de este control de stock.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Referencia al lote asociado.
     */
    @ManyToOne
    @JoinColumn(name = "id_lote")
    private LoteEntity lote;

    /**
     * Descripción del control de stock.
     */
    @Column
    private String descripcion;

    /**
     * Cantidad de productos inspeccionados en el lote.
     */
    @Column(name = "cantidad_inspeccionada")
    private double cantidadInspeccionada;

    /**
     * Cantidad de productos dañados en el lote.
     */
    @Column(name = "cantidad_dañada")
    private double cantidadDaniada;

    /**
     * Indica si el lote está vencido o no.
     */
    @Column(name = "lote_vencido")
    private boolean loteVencido;

    /**
     * Fecha en la que se elaboró el control.
     */
    @Column
    private LocalDateTime fecha;
}

