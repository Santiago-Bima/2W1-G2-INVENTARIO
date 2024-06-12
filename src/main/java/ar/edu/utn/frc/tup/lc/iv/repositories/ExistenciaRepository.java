package ar.edu.utn.frc.tup.lc.iv.repositories;

import ar.edu.utn.frc.tup.lc.iv.entities.ExistenciaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * repositorio de existencias.
 */
@Repository
public interface ExistenciaRepository extends
        JpaRepository<ExistenciaEntity, String> {
  /**
   * findByCodigo.
   *
   * @param codigo codigo de la existencia a buscar
   * @return Optional<ExistenciaEntity>
   */
  Optional<ExistenciaEntity> findByCodigo(String codigo);
}
