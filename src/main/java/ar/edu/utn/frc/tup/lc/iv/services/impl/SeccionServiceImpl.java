package ar.edu.utn.frc.tup.lc.iv.services.impl;

import ar.edu.utn.frc.tup.lc.iv.dtos.request.CrearSeccionRequest;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.SeccionResponse;
import ar.edu.utn.frc.tup.lc.iv.entities.LoteEntity;
import ar.edu.utn.frc.tup.lc.iv.entities.SeccionEntity;
import ar.edu.utn.frc.tup.lc.iv.entities.ZonaAlmacenamientoEntity;
import ar.edu.utn.frc.tup.lc.iv.repositories.LoteRepository;
import ar.edu.utn.frc.tup.lc.iv.repositories.SeccionRepository;
import ar.edu.utn.frc.tup.lc.iv.repositories.ZonaRepository;
import ar.edu.utn.frc.tup.lc.iv.services.SeccionService;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Implementacion del service de Secciones.
 */
@Service
public class SeccionServiceImpl implements SeccionService {
  /**
   * Repositorio de secciones.
   */
  @Autowired
  private SeccionRepository seccionRepository;
  /**
   * Repositorio de Zonas.
   */
  @Autowired
  private ZonaRepository zonaRepository;
  /**
   * Repositorio de lotes.
   */
  @Autowired
  private LoteRepository loteRepository;
  /**
   * Model mapper.
   */
  @Autowired
  private ModelMapper modelMapper;

  /**
   * Método para crear secciones.
   *
   * @param seccionRequest datos de la seccion a crear
   * @return SeccionResponse
   */
  @Override
  public SeccionResponse crearSeccion(
          final CrearSeccionRequest seccionRequest) {
    Optional<ZonaAlmacenamientoEntity> zonaEntityOptional =
            zonaRepository.findById(seccionRequest.getIdZonaAlmacenamiento());
    if (zonaEntityOptional.isEmpty()) {
      throw new EntityNotFoundException();
    }

    List<SeccionEntity> listaSeccionesEntityOptional =
            seccionRepository.findAllByZona(zonaEntityOptional.get());
    for (SeccionEntity seccion : listaSeccionesEntityOptional) {
      if (Objects.equals(seccion.getNombre(), seccionRequest.getNombre())) {
        return null;
      }
    }


    SeccionEntity seccionEntity = new SeccionEntity();
    seccionEntity.setNombre(seccionRequest.getNombre());
    seccionEntity.setZona(zonaEntityOptional.get());
    SeccionEntity seccionEntitySaved =
            seccionRepository.save(seccionEntity);

    return modelMapper.map(seccionEntitySaved, SeccionResponse.class);
  }

  /**
   * @param nombreZona
   * @return List<SeccionResponse>
   * Método para obtener secciones por zonas.
   */
  @Override
  public List<SeccionResponse> getSeccionesByZona(final String nombreZona) {
    ZonaAlmacenamientoEntity zonaEntity =
            zonaRepository.findZonaByNombre(nombreZona).orElse(null);
    List<SeccionEntity> listaSecciones =
            seccionRepository.findAllByZona(zonaEntity);

    if (listaSecciones.isEmpty()) {
      throw new EntityNotFoundException(
              "No hay secciones registradas para esta zona");
    }

    List<SeccionResponse> listaSeccionesResponse = new ArrayList<>();
    for (int i = 0; i < listaSecciones.size(); i++) {
      listaSeccionesResponse.add(
              modelMapper.map(
                      listaSecciones.get(i), SeccionResponse.class));
      listaSeccionesResponse.get(i).setNombreZona(nombreZona);
    }
    return listaSeccionesResponse;
  }

  /**
   * Método para eliminar secciones.
   *
   * @param id id de la seccion a eliminar
   * @return ResponseEntity
   */
  @Override
  public ResponseEntity deleteSeccion(final Long id) throws Exception {
    Optional<SeccionEntity> seccionEntityOptional =
            seccionRepository.findById(id);
    if (seccionEntityOptional.isEmpty()) {
      throw new EntityNotFoundException();
    }
    List<LoteEntity> loteEntityList =
            loteRepository.findLotesBySeccion(seccionEntityOptional.get(),
                    Sort.by(Sort.Direction.ASC, "fechaVencimiento"));
    if (!loteEntityList.isEmpty()) {
      throw new Exception("No se puede eliminar esta sección "
              + "ya que hay lotes q dependen de ella");
    } else {
      try {
        seccionRepository.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
      } catch (Exception e) {
        throw e;
      }
    }
  }

  /**
   * Método para actualizar secciones.
   *
   * @param seccion datos de la seccion a actualizar
   * @param id id de la seccion a actualizar
   * @return SeccionResponse
   */
  @Override
  public SeccionResponse updateSeccion(
          final CrearSeccionRequest seccion, final Long id) {
    Optional<SeccionEntity> seccionEntityOptional =
            seccionRepository.findById(id);
    if (seccionEntityOptional.isEmpty()) {
      throw new EntityNotFoundException();
    }

    SeccionEntity seccionEntity = seccionEntityOptional.get();
    seccionEntity.setNombre(seccion.getNombre());
    Optional<ZonaAlmacenamientoEntity> zonaAlmacenamientoEntityOptional =
            zonaRepository.findById(seccion.getIdZonaAlmacenamiento());
    if (zonaAlmacenamientoEntityOptional.isEmpty()) {
      throw new EntityNotFoundException();
    }
    ZonaAlmacenamientoEntity zonaAlmacenamiento =
            zonaAlmacenamientoEntityOptional.get();

    seccionEntity.setZona(zonaAlmacenamiento);
    return modelMapper.map(
            seccionRepository.save(seccionEntity), SeccionResponse.class);
  }
}
