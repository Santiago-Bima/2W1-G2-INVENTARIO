package ar.edu.utn.frc.tup.lc.iv.repositories;

import ar.edu.utn.frc.tup.lc.iv.entities.ReservaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * repositorio de reserva.
 */
@Repository
public interface ReservaRepository extends JpaRepository<ReservaEntity, Long> {
}
