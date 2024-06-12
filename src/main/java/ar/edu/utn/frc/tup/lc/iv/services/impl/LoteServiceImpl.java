package ar.edu.utn.frc.tup.lc.iv.services.impl;

import ar.edu.utn.frc.tup.lc.iv.dtos.request.LoteRequest;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.ExistenciaDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.LoteResponse;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.SeccionResponse;
import ar.edu.utn.frc.tup.lc.iv.entities.ExistenciaEntity;
import ar.edu.utn.frc.tup.lc.iv.entities.LoteEntity;
import ar.edu.utn.frc.tup.lc.iv.entities.SeccionEntity;
import ar.edu.utn.frc.tup.lc.iv.repositories.ExistenciaRepository;
import ar.edu.utn.frc.tup.lc.iv.repositories.LoteRepository;
import ar.edu.utn.frc.tup.lc.iv.repositories.SeccionRepository;
import ar.edu.utn.frc.tup.lc.iv.services.LoteService;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
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
 * SImplementacion de service de lotes.
 */
@Service
public class LoteServiceImpl implements LoteService {
  /**
   * model mapper.
   */
  @Autowired
  private ModelMapper modelMapper;
  /**
   * repositorio de lotes.
   */
  @Autowired
  private LoteRepository loteRepository;
  /**
   * repositorio de existencias.
   */
  @Autowired
  private ExistenciaRepository existenciaRepository;
  /**
   * repositorio de seccion.
   */
  @Autowired
  private SeccionRepository seccionRepository;

  /**
   * método de creacion de lotes.
   *
   * @param lote informacion del lote que se va acrear
   * @return LoteResponse
   * @throws Exception en caso de que no se encuentre la seccion
   */
  @Override
  public LoteResponse postLote(final LoteRequest lote) throws Exception {
    List<LoteEntity> listaLotes = loteRepository.findAll();
    Optional<SeccionEntity> seccionEntityOptional =
            seccionRepository.findById(lote.getIdSeccion());
    if (seccionEntityOptional.isEmpty()) {
      throw  new EntityNotFoundException("No existe esa entidad");
    }
    SeccionEntity seccionEntity = seccionEntityOptional.get();

    for (LoteEntity listaLote : listaLotes) {
      if (Objects.equals(listaLote.getEstante(), lote.getEstante())
              && (Objects.equals(listaLote.getSeccion().getId(),
              lote.getIdSeccion())
              && Objects.equals(listaLote.getSeccion()
              .getZona().getId(), seccionEntity.getZona().getId()))) {
        throw new Exception("No puede haber un lote en la misma "
                + "ubicación (estante, sección y zona) que otro");
      }
    }

    LoteEntity loteEntity = new LoteEntity();
    loteEntity.setCantidad(lote.getCantidad());
    loteEntity.setEstante(lote.getEstante());
    loteEntity.setFechaVencimiento(lote.getFechaVencimiento());
    loteEntity.setExistencia(existenciaRepository.getReferenceById(
            lote.getCodigoExistencia()));
    loteEntity.setSeccion(seccionRepository.getReferenceById(
            lote.getIdSeccion()));
    return modelMapper.map(loteRepository.save(loteEntity), LoteResponse.class);
  }

  /**
   * metodo para obtener lotes.
   *
   * @return LoteResponse[]
   */
  @Override
  public LoteResponse[] getLotes() {
    return modelMapper.map(loteRepository.findAll(
            Sort.by(Sort.Direction.ASC, "fechaVencimiento")),
            LoteResponse[].class);
  }

  /**
   * metodo para obtener lotes.
   *
   * @return LoteResponse[]
   */
  @Override
  public List<LoteResponse> getLotesVencidos() {
    LoteResponse[] lotes = getLotes();
    List<LoteResponse> lotesVencidos = new ArrayList<>();
    for (LoteResponse l : lotes) {
      if (l.getFechaVencimiento().isBefore(LocalDateTime.now())) {
        lotesVencidos.add(l);
      }
    }
    return lotesVencidos;
  }


  /**
   * metodo para obtener lotes.
   *
   * @return LoteResponse[]
   */
  @Override
  public List<LoteResponse> getLotesAvencer(final Long dias) {
    LoteResponse[] lotes = getLotes();
    List<LoteResponse> lotesVencidos = new ArrayList<>();
    for (LoteResponse l : lotes) {
      if (l.getFechaVencimiento().isAfter(LocalDateTime.now())
              &&
          l.getFechaVencimiento().isBefore(
                  LocalDateTime.now().plusDays(dias))) {
        lotesVencidos.add(l);
      }
    }
    return lotesVencidos;
  }

  /**
   * metodo para eliminar lotes sin stock.
   *
   * @param lote lote el cual se va a eliminar
   * @return Responseentity
   */
  private ResponseEntity eliminarLoteSinStock(final LoteEntity lote) {
    if (lote.getCantidad() == 0) {
      if (!loteRepository.existsById(lote.getId())) {
        return new ResponseEntity("No existe el lote "
                + "ingresado en la base de datos", HttpStatus.NOT_FOUND);
      }

      try {
        loteRepository.delete(lote);
        return new ResponseEntity("Se pudo eliminar el lote", HttpStatus.OK);
      } catch (Exception e) {
        throw e;
      }
    } else {
      return new ResponseEntity("El lote aún posee stock",
              HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * metodo para restar el stock que se venda.
   *
   * @param lote el lote al que se le va a restar la cantidad
   * @param cantidad cantidad de stock a restar
   * @return ResponseEntity
   */
  public ResponseEntity restarStock(
          final LoteEntity lote, final Integer cantidad) {
    double total = lote.getCantidad() - cantidad;
    if (total < 0) {
      return new ResponseEntity("El lote posee menos stock que el indicado",
              HttpStatus.BAD_REQUEST);
    }

    lote.setCantidad(total);
    loteRepository.save(lote);
    ResponseEntity eliminado = eliminarLoteSinStock(lote);
    if (eliminado.getStatusCode() == HttpStatus.OK) {
      return new ResponseEntity("El stock del lote quedó en 0 y fué eliminado",
              HttpStatus.OK);
    } else {
      if (eliminado.getStatusCode() == HttpStatus.NOT_FOUND) {
        return eliminado;
      } else {
        return new ResponseEntity("Se pudo descontar la cantidad del lote",
                HttpStatus.OK);
      }
    }
  }

  /**
   * metodo para obtener lotes po secciones.
   *
   * @param seccion
   * @return List<LoteResponse>
   */
  @Override
  public List<LoteResponse> getLotesBySeccion(final Long seccion) {
    SeccionEntity seccionEntity =
            seccionRepository.findById(seccion).orElse(null);
    List<LoteEntity> listaLotesEntity =
            loteRepository.findLotesBySeccion(seccionEntity,
                    Sort.by(Sort.Direction.ASC, "fechaVencimiento"));
    if (listaLotesEntity.isEmpty()) {
      throw new EntityNotFoundException();
    }

    List<LoteResponse> loteResponses = new ArrayList<>();

    for (int i = 0; i < listaLotesEntity.size(); i++) {
      loteResponses.add(modelMapper.map(listaLotesEntity.get(i),
              LoteResponse.class));
      loteResponses.get(i).setSeccion(modelMapper.map(seccionEntity,
              SeccionResponse.class));
    }

    return loteResponses;
  }

  /**
   * metodo para obtener lotes por sus existencias.
   *
   * @param existencia
   * @return List<LoteResponse>
   */
  @Override
  public List<LoteResponse> getLotesByExistencia(final String existencia) {
    ExistenciaEntity existenciaEntity =
            existenciaRepository.findByCodigo(existencia).orElse(null);
    List<LoteEntity> listaLotesEntity =
            loteRepository.findByExistencia(existenciaEntity,
                    Sort.by(Sort.Direction.ASC, "fechaVencimiento"));
    if (listaLotesEntity.isEmpty()) {
      throw new EntityNotFoundException();
    }

    List<LoteResponse> loteResponses = new ArrayList<>();

    for (int i = 0; i < listaLotesEntity.size(); i++) {
      loteResponses.add(modelMapper.map(listaLotesEntity.get(i),
              LoteResponse.class));
      loteResponses.get(i).setExistencia(modelMapper.map(existenciaEntity,
              ExistenciaDto.class));
    }

    modelMapper.map(loteRepository.findAll(
                    ),
            LoteResponse[].class);

    return loteResponses;
  }

  /**
   * metodo para actualizar lotes.
   *
   * @param lote datos a modificar
   * @param id id del lote a modificar
   * @return LoteResponse
   */
  @Override
  public LoteResponse updateLote(final LoteRequest lote, final Long id) {
    Optional<LoteEntity> loteEntityOptional = loteRepository.findById(id);
    if (loteEntityOptional.isEmpty()) {
      throw new EntityNotFoundException();
    }

    LoteEntity loteEntity = loteEntityOptional.get();
    loteEntity.setCantidad(lote.getCantidad());
    loteEntity.setEstante(lote.getEstante());
    loteEntity.setFechaVencimiento(lote.getFechaVencimiento());

    Optional<SeccionEntity> seccionEntityOptional =
            seccionRepository.findById(lote.getIdSeccion());
    if (seccionEntityOptional.isEmpty()) {
      throw new EntityNotFoundException();
    }
    SeccionEntity seccionEntity = seccionEntityOptional.get();
    loteEntity.setSeccion(seccionEntity);

    Optional<ExistenciaEntity> existenciaEntityOptional =
            existenciaRepository.findById(lote.getCodigoExistencia());
    if (existenciaEntityOptional.isEmpty()) {
      throw new EntityNotFoundException();
    }
    ExistenciaEntity existenciaEntity = existenciaEntityOptional.get();
    loteEntity.setExistencia(existenciaEntity);

    return modelMapper.map(loteRepository.save(loteEntity), LoteResponse.class);
  }


  /**
   * metodo para eliminar lotes.
   *
   * @param id id del lote a eliminar
   * @return LoteResponse
   */
  @Override
  public ResponseEntity eliminarLote(final Long id) {
    if (!loteRepository.existsById(id)) {
      return new ResponseEntity("El lote no existe",
              HttpStatus.NOT_FOUND);
    }
    try {
      loteRepository.deleteById(id);
      return new ResponseEntity("El lote se elimino",
              HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity("El lote no se pudo eliminar",
              HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
