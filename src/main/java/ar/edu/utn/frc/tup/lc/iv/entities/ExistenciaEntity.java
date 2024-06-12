package ar.edu.utn.frc.tup.lc.iv.entities;

import ar.edu.utn.frc.tup.lc.iv.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import jdk.jfr.Description;
import lombok.Data;

@Entity
@Table(name = "existencias")
@Data
@Generated
public class ExistenciaEntity {
    /**
     * id.
     */
    @Id
    @Column(name = "codigo_existencia")
    @Description("Existence code")
    private String codigo;
    /**
     * nombre.
     */
    @Description("Existence name")
    @Column(name = "nombre")
    private String nombre;
    /**
     * stockMinimo.
     */
    @Description("Existence min stock")
    @Column(name = "stock_minimo")
    private double stockMinimo;
    /**
     * lotes.
     */
    @Description("Existence's Batches")
    @OneToMany(mappedBy = "existencia")
    private List<LoteEntity> lotes;
}
