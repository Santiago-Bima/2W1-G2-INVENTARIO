package ar.edu.utn.frc.tup.lc.iv.repositories;

import ar.edu.utn.frc.tup.lc.iv.entities.DetalleReservaEntity;
import ar.edu.utn.frc.tup.lc.iv.entities.LoteEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * repositorio de detalles de reservas.
 */
@Repository
public interface DetalleReservaRepository
        extends JpaRepository<DetalleReservaEntity, Long> {
    /**
     * findAllByLote.
     * @param loteEntity
     * @return List<DetalleReservaEntity>
     */
    List<DetalleReservaEntity> findAllByLote(
            LoteEntity loteEntity);
}
