package ar.edu.utn.frc.tup.lc.iv.services.impl;

import ar.edu.utn.frc.tup.lc.iv.dtos.response.ZonaAlmacenamientoResponse;
import ar.edu.utn.frc.tup.lc.iv.entities.ZonaAlmacenamientoEntity;
import ar.edu.utn.frc.tup.lc.iv.repositories.ZonaRepository;
import ar.edu.utn.frc.tup.lc.iv.services.ZonaService;
import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementacion del service de zonas.
 */
@Service
public class ZonaServiceImpl implements ZonaService {
  /**
   * ModelMapper.
   */
  @Autowired
  private ModelMapper modelMapper;
  /**
   * Repositorio de Zonas.
   */
  @Autowired
  private ZonaRepository zonaRepository;

  /**
   * Método para obtener las zonas de almacenamiento.
   */
  @Override
  public List<ZonaAlmacenamientoResponse> getZonas() {
    List<ZonaAlmacenamientoEntity> listaZonasEntities =
            zonaRepository.findAll();
    List<ZonaAlmacenamientoResponse> listaZonasResponse =
            new ArrayList<>();
    for (int i = 0; i < listaZonasEntities.size(); i++) {
      listaZonasResponse.add(modelMapper.map(
              listaZonasEntities.get(i), ZonaAlmacenamientoResponse.class));
    }
    return listaZonasResponse;
  }
}
