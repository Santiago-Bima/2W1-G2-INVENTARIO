package ar.edu.utn.frc.tup.lc.iv.controllers;

import ar.edu.utn.frc.tup.lc.iv.dtos.response.ZonaAlmacenamientoResponse;
import ar.edu.utn.frc.tup.lc.iv.services.ZonaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * controlador de zonas de almacenamiento.
 */
@RestController
public class ZonaController {
  /**
   * zonaService.
   */
  @Autowired
  private ZonaService zonaService;

  /**
   * getZonas.
   * @return List<ZonaAlmacenamientoResponse>
   */
  @GetMapping("inventario/zonas/")
  List<ZonaAlmacenamientoResponse> getZonas() {
    return zonaService.getZonas();
  }
}
