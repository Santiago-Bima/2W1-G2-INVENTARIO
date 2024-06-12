package ar.edu.utn.frc.tup.lc.iv.controllers;

import ar.edu.utn.frc.tup.lc.iv.dtos.request.LoteRequest;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.LoteResponse;
import ar.edu.utn.frc.tup.lc.iv.services.LoteService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * controlador de lotes.
 */
@RestController
@RequestMapping("inventario/lotes")
public class LoteController {
  /**
   * service de lotes.
   */
  @Autowired
  private LoteService loteService;

  /**
   * metodo para crear un lote.
   *
   * @param lote datos del lote a crear
   * @return LoteResponse datos del lote creado
   * @throws Exception error proveniente del service
   */
  @PostMapping("/crear/")
  public LoteResponse crearLote(@RequestBody final LoteRequest lote)
          throws Exception {
    return loteService.postLote(lote);
  }

  /**
   * metodo para obtener los lotes.
   *
   * @return LoteResponse[]
   */
  @GetMapping("/listar/")
  public LoteResponse[] getLotes() {
    return loteService.getLotes();
  }

  /**
   * metodo para obtener lotes vencidos.
   *
   * @return List<LoteResponse>
   */
  @GetMapping("/listarVencidos/")
  public List<LoteResponse> getLotesVencidos() {
    return loteService.getLotesVencidos();
  }

  /**
   * metodo para listar lotes vencidos.
   *
   * @param dias rango de dias que quedan para que se venza
   * @return List<LoteResponse> lista de lotes a vencer
   */
  @GetMapping("/listarAVencer/")
  public List<LoteResponse> getLotesAvencer(@RequestParam final Long dias) {
    return loteService.getLotesAvencer(dias);
  }

  /**
   * metodo para listar lotes por secciones.
   *
   * @param idSeccion id de la seccion a filtrar
   * @return List<LoteResponse>
   */
  @GetMapping("/listarPorSección/{idSeccion}")
  public List<LoteResponse> getLotesBySeccion(
          @PathVariable final Long idSeccion) {
    return loteService.getLotesBySeccion(idSeccion);
  }

  /**
   * metodo para listar por existencias.
   *
   * @param existenceCode codigo de la existencia a filtrar
   * @return List<LoteResponse>
   */
  @GetMapping("/listarPorExistencia/{existenceCode}")
  public List<LoteResponse> getLotesByExistencia(
          @PathVariable final String existenceCode) {
    return loteService.getLotesByExistencia(existenceCode);
  }

  /**
   * metodo para modificar un lote.
   *
   * @param lote datos modificados del lote
   * @param id id del lote a modificar
   * @return LoteResponse lote modificado
   */
  @PutMapping("/editar/{id}")
  LoteResponse updateLote(
          @RequestBody final LoteRequest lote,
          @PathVariable final Long id) {
    return loteService.updateLote(lote, id);
  }

  /**
   * metodo para eliminar un lote.
   *
   * @param id id del lote a eliminar
   * @return ResponseEntity
   */
  @DeleteMapping("/eliminar/{id}")
  ResponseEntity eliminarLote(@PathVariable final Long id) {
    return loteService.eliminarLote(id);
  }
}
