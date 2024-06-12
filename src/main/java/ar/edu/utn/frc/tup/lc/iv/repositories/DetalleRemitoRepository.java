package ar.edu.utn.frc.tup.lc.iv.repositories;

import ar.edu.utn.frc.tup.lc.iv.entities.DetalleRemitoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio que gestiona las operaciones
 * de base de datos para la entidad DetalleRemitoEntity.
 */
@Repository
public interface DetalleRemitoRepository
        extends JpaRepository<DetalleRemitoEntity, Long> {
}

