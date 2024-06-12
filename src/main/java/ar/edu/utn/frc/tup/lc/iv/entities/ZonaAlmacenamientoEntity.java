package ar.edu.utn.frc.tup.lc.iv.entities;

import ar.edu.utn.frc.tup.lc.iv.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Data;

/**
 * entidad de zonas de almacenamiento.
 */
@Entity
@Data
@Table(name = "zonas_almacenamiento")
@Generated
public class ZonaAlmacenamientoEntity {
    /**
     * Id de la zona.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_zona", unique = true)
    private Long id;
    /**
     * nombre de la zona.
     */
    @Column(name = "nombre_zona", unique = true)
    private String nombre;
    /**
     * Lista de las secciones que la contienen.
     */
    @OneToMany(mappedBy = "zona")
    private List<SeccionEntity> secciones;
}
