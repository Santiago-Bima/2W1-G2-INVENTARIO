package ar.edu.utn.frc.tup.lc.iv.entities;

import ar.edu.utn.frc.tup.lc.iv.Generated;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import jdk.jfr.Description;
import lombok.Data;

/**
 * entidad de secciones.
 */
@Entity
@Table(name = "secciones")
@Data
@Generated
public class SeccionEntity {
    /**
     * id de la seccion.
     */
    @Id
    @Column(name = "id_seccion")
    @Description("Section id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * nombre de la seccion.
     */
    @Description("Section name")
    private String nombre;
    /**
     * id de la zona a la que pertenece.
     */
    @ManyToOne
    @JoinColumn(name = "id_zona")
    @Description("Zone id")
    private ZonaAlmacenamientoEntity zona;
    /**
     * lista de lotes de los cuales la contienen.
     */
    @OneToMany(mappedBy = "seccion", cascade = CascadeType.REFRESH)
    @Description("Section Batch")
    private List<LoteEntity> lotes;
}
