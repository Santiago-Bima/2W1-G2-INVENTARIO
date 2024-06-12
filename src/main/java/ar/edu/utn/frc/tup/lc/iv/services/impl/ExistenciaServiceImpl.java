package ar.edu.utn.frc.tup.lc.iv.services.impl;

import static java.util.Objects.isNull;

import ar.edu.utn.frc.tup.lc.iv.clients.CatalogoClient;
import ar.edu.utn.frc.tup.lc.iv.dtos.request.CrearExistenciaRequestDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.request.ModificarExistenciaRequestDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.ExistenciaBajaDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.ExistenciaDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.ExistenciaTotalResponse;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.ProductoCatalogoDto;
import ar.edu.utn.frc.tup.lc.iv.entities.DetalleReservaEntity;
import ar.edu.utn.frc.tup.lc.iv.entities.ExistenciaEntity;
import ar.edu.utn.frc.tup.lc.iv.entities.LoteEntity;
import ar.edu.utn.frc.tup.lc.iv.repositories.DetalleReservaRepository;
import ar.edu.utn.frc.tup.lc.iv.repositories.ExistenciaRepository;
import ar.edu.utn.frc.tup.lc.iv.repositories.LoteRepository;
import ar.edu.utn.frc.tup.lc.iv.services.ExistenciaService;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * implementacion del service de existencias.
 */
@Service
public class ExistenciaServiceImpl implements ExistenciaService {
  /**
   * existenciaRepository.
   */
  @Autowired
  private ExistenciaRepository existenciaRepository;

  /**
   * loteRepository.
   */
  @Autowired
  private LoteRepository loteRepository;
  /**
   * catalogoClient.
   */
  @Autowired
  private CatalogoClient catalogoClient;
  /**
   * repositorio de detalles de reserva.
   */
  @Autowired
  private DetalleReservaRepository detalleReservaRepository;
  /**
   * modelMapper.
   */
  @Autowired
  private ModelMapper modelMapper;

  /**
   * consultarCatalogo.
   *
   * @return ProductoCatalogoDto
   */
  @Override
  public ProductoCatalogoDto[] consultarCatalogo() {
    return catalogoClient.getAll().getBody();
  }

  /**
   * crearExistencia.
   *
   * @param existenciaNueva datos de la existencia que se va a crear
   * @return ExistenciaDto
   */
  @Override
  public ExistenciaDto crearExistencia(
          final CrearExistenciaRequestDto existenciaNueva) throws Exception {
    ProductoCatalogoDto prCatalogo = catalogoClient
            .get(existenciaNueva.getCodigoProducto()).getBody();
    if (isNull(prCatalogo)) {
      throw new EntityNotFoundException("Product Not found");
    }
    Optional<ExistenciaEntity> existenciaTest =
            existenciaRepository.findByCodigo(
                    existenciaNueva.getCodigoProducto());
    if (existenciaTest.isPresent()) {
      throw new EntityNotFoundException("Product already with existance");
    }
    ExistenciaEntity existencia = new ExistenciaEntity();
    existencia.setStockMinimo(existenciaNueva.getStockMinimo());
    existencia.setNombre(existenciaNueva.getNombre());
    existencia.setCodigo(existenciaNueva.getCodigoProducto());
    existencia.setLotes(new ArrayList<>());
    existencia = existenciaRepository.save(existencia);
    return modelMapper.map(existencia, ExistenciaDto.class);
  }

  /**
   * modificarExistencia.
   *
   * @param codigo id de la existencia a modificar
   * @param existenciaModificada datos de la existencia modificada
   * @return ExistenciaDto
   */
  @Override
  public ExistenciaDto modificarExistencia(
          final String codigo,
          final ModificarExistenciaRequestDto existenciaModificada) {
    Optional<ExistenciaEntity> e = existenciaRepository.findByCodigo(codigo);
    if (e.isEmpty()) {
      return null;
    }
    ExistenciaEntity existencia = e.get();
    existencia.setStockMinimo(existenciaModificada.getStockMinimo());
    existenciaRepository.save(existencia);
    return modelMapper.map(existencia, ExistenciaDto.class);
  }

  /**
   * eliminarExistencia.
   *
   * @param codigo id de la existencia a eliminar
   * @return ExistenciaDto
   */
  @Override
  public ExistenciaDto eliminarExistencia(final String codigo) {
    Optional<ExistenciaEntity> e = existenciaRepository.findByCodigo(codigo);
    if (e.isEmpty()) {
      return null;
    }
    if (!loteRepository.findByExistencia(e.get(),
            Sort.by(Sort.Direction.ASC, "fechaVencimiento")).isEmpty()) {
      throw new EntityNotFoundException("Existance already used");
    }
    existenciaRepository.delete(e.get());
    return modelMapper.map(e.get(), ExistenciaDto.class);
  }

  /**
   * getTotalExistencia.
   *
   * @param codigo id de la existencia a buscar
   * @return ExistenciaTotalResponse
   */
  @Override
  public ExistenciaTotalResponse getTotalExistencia(final String codigo) {
    Optional<ExistenciaEntity> existenciaEntity =
            existenciaRepository.findByCodigo(codigo);
    if (existenciaEntity.isEmpty()) {
      throw new EntityNotFoundException();
    }


    double total = 0;
    List<LoteEntity> lotesEntity = loteRepository.findByExistencia(
            existenciaEntity.get(),
            Sort.by(Sort.Direction.ASC, "fechaVencimiento"));
    for (LoteEntity l : lotesEntity) {
      total += l.getCantidad();
    }

    for (LoteEntity l : lotesEntity) {
      List<DetalleReservaEntity> listaDetallesReserva =
              detalleReservaRepository.findAllByLote(l);
      for (DetalleReservaEntity d : listaDetallesReserva) {
        total -= d.getCantidad();
      }
    }

    ExistenciaTotalResponse existenciaTotalResponse =
            modelMapper.map(existenciaEntity.get(),
                    ExistenciaTotalResponse.class);
    existenciaTotalResponse.setTotal(total);

    return existenciaTotalResponse;
  }
  /**
   * metodo para obtener totales.
   *
   * @return List<ExistenciaTotalResponse>
   */
  @Override
  public List<ExistenciaTotalResponse> getTotalExistencias() {
    List<ExistenciaEntity> listaExitencias = existenciaRepository.findAll();
    List<ExistenciaTotalResponse> listaExistenciasTotales = new ArrayList<>();

    for (ExistenciaEntity e : listaExitencias) {
      ExistenciaTotalResponse existenciaTotalResponse =
              modelMapper.map(e, ExistenciaTotalResponse.class);
      existenciaTotalResponse.setTotal(
              getTotalExistencia(e.getCodigo()).getTotal());
      listaExistenciasTotales.add(existenciaTotalResponse);
    }

    return listaExistenciasTotales;
  }

  /**
   * metodo para obtener existencias cuyo
   * stock es menor al indicado.
   *
   * @return List<ExistenciaBajaDto>
   */
  @Override
  public List<ExistenciaBajaDto> getExistenciasBajas() {
    List<ExistenciaBajaDto> existenciaBajaDtoList = new ArrayList<>();

    List<ExistenciaEntity> existenciasList = existenciaRepository.findAll();
    for (ExistenciaEntity e : existenciasList) {
      double total = 0;
      List<LoteEntity> lotesEntity =
              loteRepository.findByExistencia(e,
                      Sort.by(Sort.Direction.ASC, "fechaVencimiento"));
      for (LoteEntity l : lotesEntity) {
        total += l.getCantidad();
      }

      if (total < e.getStockMinimo()) {
        ExistenciaBajaDto existenciaBaja =
                modelMapper.map(e, ExistenciaBajaDto.class);
        existenciaBaja.setStockTotal(total);
        existenciaBajaDtoList.add(existenciaBaja);
      }
    }
    return existenciaBajaDtoList;
  }
}
