package ar.edu.utn.frc.tup.lc.iv.repositories;

import ar.edu.utn.frc.tup.lc.iv.entities.ControlDeStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * repositorio de controles de stock.
 */
@Repository
public interface ControlStockRepository
        extends JpaRepository<ControlDeStockEntity, Long> {
}
