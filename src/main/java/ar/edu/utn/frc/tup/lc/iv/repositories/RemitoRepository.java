package ar.edu.utn.frc.tup.lc.iv.repositories;

import ar.edu.utn.frc.tup.lc.iv.entities.RemitoEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio que gestiona las operaciones
 * de base de datos para la entidad RemitoEntity.
 */
public interface RemitoRepository
        extends JpaRepository<RemitoEntity, Long> {
    /**
     * Busca un remito por su número y el nombre del proveedor asociado.
     *
     * @param nroRemito       Número de remito a buscar.
     * @param nombreProveedor Nombre del proveedor asociado al remito.
     * @return Una instancia de RemitoEntity si se encuentra o un valor vacío.
     */
    Optional<RemitoEntity> findByNroRemitoAndNombreProveedor(
            Long nroRemito, String nombreProveedor);
}

