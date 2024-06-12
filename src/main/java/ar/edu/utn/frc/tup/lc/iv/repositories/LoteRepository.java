package ar.edu.utn.frc.tup.lc.iv.repositories;

import ar.edu.utn.frc.tup.lc.iv.entities.ExistenciaEntity;
import ar.edu.utn.frc.tup.lc.iv.entities.LoteEntity;
import ar.edu.utn.frc.tup.lc.iv.entities.SeccionEntity;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * repositorio de lotes.
 */
@Repository
public interface LoteRepository extends JpaRepository<LoteEntity, Long> {
    /**
     * findByExistance.
     *
     * @param existencia existencia que filtra los lotes
     * @param fechaVencimiento valor para ordenar la lista
     * @return List<LoteEntity>
     */
    List<LoteEntity> findByExistencia(ExistenciaEntity existencia,
                                      Sort fechaVencimiento);
    /**
     * findBySection.
     *
     * @param seccion por la que se filtra los lotes
     * @param fechaVencimiento valor para ordenar la lista
     * @return List<LoteEntity>
     */
    List<LoteEntity> findLotesBySeccion(SeccionEntity seccion,
                                        Sort fechaVencimiento);

}
