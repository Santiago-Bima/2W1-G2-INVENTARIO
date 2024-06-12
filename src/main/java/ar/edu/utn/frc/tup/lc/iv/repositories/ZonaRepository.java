package ar.edu.utn.frc.tup.lc.iv.repositories;

import ar.edu.utn.frc.tup.lc.iv.entities.ZonaAlmacenamientoEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * repositorio de zonas.
 */
@Repository
public interface ZonaRepository
        extends JpaRepository<ZonaAlmacenamientoEntity, Long> {
  /**
   * metodo para encontrar zonas por nombre.
   *
   * @param nombre nombre de la zona a buscar
   * @return Optional<ZonaAlmacenamientoEntity>
   */
  Optional<ZonaAlmacenamientoEntity> findZonaByNombre(String nombre);
}
