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


@Entity
@Table(name = "detalles_reserva")
@Data
@NoArgsConstructor
@Generated
public class DetalleReservaEntity {
    /**
     * Identificador único de este detalle de reserva.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    /**
     * La reserva a la que se asocia este detalle.
     */
    @ManyToOne
    @JoinColumn(name = "id_reserva", referencedColumnName = "id",
            nullable = false)
    private ReservaEntity reserva;
    /**
     * El lote relacionado con este detalle.
     */
    @ManyToOne
    @JoinColumn(name = "id_lote")
    private LoteEntity lote;
    /**
     * La cantidad de productos o elementos reservados en este detalle.
     */
    @Column(name = "cantidad")
    private double cantidad;
}

