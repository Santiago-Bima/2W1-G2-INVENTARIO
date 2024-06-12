package ar.edu.utn.frc.tup.lc.iv.repositories;

import ar.edu.utn.frc.tup.lc.iv.entities.SeccionEntity;
import ar.edu.utn.frc.tup.lc.iv.entities.ZonaAlmacenamientoEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * repositorio de secciones.
 */
@Repository
public interface SeccionRepository extends JpaRepository<SeccionEntity, Long> {
  /**
   * metodo para encontrar secciones por zonas.
   *
   * @param zona por la cual se va a filtrar
   * @return List<SeccionEntity>
   */
  List<SeccionEntity> findAllByZona(ZonaAlmacenamientoEntity zona);
}
