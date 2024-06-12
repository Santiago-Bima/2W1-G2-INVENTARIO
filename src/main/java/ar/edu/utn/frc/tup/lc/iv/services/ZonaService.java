package ar.edu.utn.frc.tup.lc.iv.services;

import ar.edu.utn.frc.tup.lc.iv.dtos.response.ZonaAlmacenamientoResponse;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Servicio de ABMC de zonas.
 */
@Service
public interface ZonaService {
  /**
   * Método para obtener zonas de almacenamiento.
   * @return List<ZonaAlmacenamientoResponse>
   */
  List<ZonaAlmacenamientoResponse> getZonas();
}
