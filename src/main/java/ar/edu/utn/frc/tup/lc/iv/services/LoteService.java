package ar.edu.utn.frc.tup.lc.iv.services;

import ar.edu.utn.frc.tup.lc.iv.dtos.request.LoteRequest;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.LoteResponse;
import ar.edu.utn.frc.tup.lc.iv.entities.LoteEntity;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


/**
 * Service para ABMC lotes.
 */
@Service
public interface LoteService {
  /**
   * crear Lote.
   *
   * @param lote datos del lote a crear
   * @return LoteResponse
   */
  LoteResponse postLote(LoteRequest lote) throws Exception;
  /**
   * getLotes.
   *
   * @return LoteResponse[]
   */
  LoteResponse[] getLotes();
  /**
   * getLotesVencidos.
   *
   * @return List<LoteResponse>
   */
  List<LoteResponse> getLotesVencidos();
  /**
   * getLotesVencidos.
   *
   * @param dias cantidad de dias en los que se puede vencer
   * @return List<LoteResponse>
   */
  List<LoteResponse> getLotesAvencer(Long dias);
  /**
   * getLotesBySeccion.
   *
   * @param seccion id de la seccion a filtrar
   * @return List<LoteResponse>
   */
  List<LoteResponse> getLotesBySeccion(Long seccion);
  /**
   * getLotesByExistencia.
   *
   * @param codigoExistencia codigo de la existencia a filtrar
   * @return List<LoteResponse>
   */
  List<LoteResponse> getLotesByExistencia(String codigoExistencia);
  /**
   * restar stock de lote.
   *
   * @param lote lote a modificar
   * @param cantidad cantidad a restar
   * @return ResponseEntity
   */
  ResponseEntity restarStock(LoteEntity lote, Integer cantidad);
  /**
   * updateLote.
   *
   * @param lote datos del lote a actualizar
   * @param id id del lote a actualizar
   * @return LoteResponse
   */
  LoteResponse updateLote(LoteRequest lote, Long id);
  /**
   * eliminarLote.
   *
   * @param id id del lote a eliminar
   * @return LoteResponse
   */
  ResponseEntity eliminarLote(Long id);
}
