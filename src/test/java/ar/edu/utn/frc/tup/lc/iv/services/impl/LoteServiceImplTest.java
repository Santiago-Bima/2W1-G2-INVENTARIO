package ar.edu.utn.frc.tup.lc.iv.services.impl;

import ar.edu.utn.frc.tup.lc.iv.dtos.request.LoteRequest;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.ExistenciaDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.LoteResponse;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.SeccionResponse;
import ar.edu.utn.frc.tup.lc.iv.entities.ExistenciaEntity;
import ar.edu.utn.frc.tup.lc.iv.entities.LoteEntity;
import ar.edu.utn.frc.tup.lc.iv.entities.SeccionEntity;
import ar.edu.utn.frc.tup.lc.iv.entities.ZonaAlmacenamientoEntity;
import ar.edu.utn.frc.tup.lc.iv.repositories.ExistenciaRepository;
import ar.edu.utn.frc.tup.lc.iv.repositories.LoteRepository;
import ar.edu.utn.frc.tup.lc.iv.repositories.SeccionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class LoteServiceImplTest {
  @Mock
  private LoteRepository loteRepository;
  @Mock
  private ExistenciaRepository existenciaRepository;
  @Mock
  private SeccionRepository seccionRepository;
  @Mock
  private ModelMapper modelMapper;
  @InjectMocks
  private LoteServiceImpl loteService;

  private final LocalDateTime fechaHora = LocalDateTime.parse("2024-12-15T00:00:00");
  private LoteRequest loteRequest;
  private SeccionResponse seccionResponse;
  private LoteResponse loteResponse;
  private LoteEntity loteEntity;
  private SeccionEntity seccionEntity;
  private ExistenciaEntity existenciaEntity;
  private  ZonaAlmacenamientoEntity zonaAlmacenamientoEntity;

  @BeforeEach()
  void setUp(){
    loteRequest = new LoteRequest(fechaHora, 100, 4, "2HA", 1L);
    seccionResponse = new SeccionResponse(1L, "Z", "Corralito");
    loteResponse = new LoteResponse(1L, 100, 4, seccionResponse, new ExistenciaDto(), fechaHora);

    zonaAlmacenamientoEntity = new ZonaAlmacenamientoEntity();
    zonaAlmacenamientoEntity.setId(1L);

    seccionEntity = new SeccionEntity();
    seccionEntity.setId(1L);
    seccionEntity.setNombre("Z");
    seccionEntity.setZona(zonaAlmacenamientoEntity);

    existenciaEntity = new ExistenciaEntity();
    existenciaEntity.setCodigo("2HA");
    existenciaEntity.setNombre("Boltz");

    loteEntity = new LoteEntity(1L, fechaHora, 100, 4, seccionEntity, existenciaEntity);
  }

  @Test
  void testPostLote() throws Exception{
    LoteEntity loteEntity2 = loteEntity;
    loteEntity2.setId(2L);

    List<LoteEntity> listLotesEntities =
            Arrays.asList(loteEntity, loteEntity2);

    SeccionEntity seccionEntity2 = seccionEntity;
    seccionEntity2.setNombre("H");
    seccionEntity2.setId(3L);
    seccionEntity2.setZona(zonaAlmacenamientoEntity);

    LoteResponse loteResponse2 = loteResponse;
    seccionResponse.setNombre("H");
    seccionResponse.setId(3L);
    loteResponse2.setSeccion(seccionResponse);
    loteResponse2.setEstante(7);

    LoteRequest loteRequest2 = loteRequest;
    loteRequest2.setIdSeccion(3L);
    loteRequest2.setEstante(8);

    LoteEntity loteEntity3 = loteEntity;
    loteEntity3.setId(3L);
    loteEntity3.setSeccion(seccionEntity2);
    loteEntity3.setEstante(7);

    when(loteRepository.findAll()).thenReturn(listLotesEntities);
    when(seccionRepository.findById(any(Long.class))).thenReturn(Optional.of(seccionEntity2));
    when(existenciaRepository.getReferenceById(any(String.class))).thenReturn(existenciaEntity);
    when(seccionRepository.getReferenceById(any(Long.class))).thenReturn(seccionEntity2);
    when(loteRepository.save(any(LoteEntity.class))).thenReturn(loteEntity3);
    when(modelMapper.map(loteEntity3, LoteResponse.class)).thenReturn(loteResponse2);

    assertEquals(loteResponse2, loteService.postLote(loteRequest2));

    loteRequest2.setEstante(7);
    loteRequest2.setIdSeccion(3L);
    System.out.println(loteRequest2);
    System.out.println(listLotesEntities.get(0));
    System.out.println(listLotesEntities.get(1));
    assertThrows(Exception.class, () -> loteService.postLote(loteRequest2));

    when(seccionRepository.findById(any(Long.class))).thenReturn(Optional.empty());
    assertThrows(EntityNotFoundException.class, () -> loteService.postLote(loteRequest));
  }

  @Test
  void testFindAll() {
    LoteEntity loteEntity2 = loteEntity;
    loteEntity2.setId(2L);

    List<LoteEntity> listLotesEntities =
            Arrays.asList(loteEntity, loteEntity2);

    LoteResponse[] listaResponses = {loteResponse, new LoteResponse()};

    when(loteRepository.findAll(eq(Sort.by(Sort.Direction.ASC, "fechaVencimiento")))).thenReturn(listLotesEntities);
    when(modelMapper.map(listLotesEntities, LoteResponse[].class)).thenReturn(listaResponses);

    assertEquals(listaResponses.length, loteService.getLotes().length);
  }

  @Test
  void testGetVencidos() {
    LocalDateTime fechaHoraVencida = LocalDateTime.parse("2023-10-15T00:00:00");

    LoteEntity loteEntity2 = loteEntity;
    loteEntity2.setId(2L);
    loteEntity2.setFechaVencimiento(fechaHoraVencida);

    loteEntity.setFechaVencimiento(fechaHoraVencida);

    List<LoteEntity> listLotesEntities =
            Arrays.asList(loteEntity, loteEntity2);

    loteResponse.setFechaVencimiento(fechaHoraVencida);
    LoteResponse loteResponse2 = loteResponse;
    loteResponse2.setFechaVencimiento(fechaHoraVencida);

    LoteResponse[] listaResponses = {loteResponse, loteResponse2};
    when(loteRepository.findAll(eq(Sort.by(Sort.Direction.ASC, "fechaVencimiento")))).thenReturn(listLotesEntities);
    when(modelMapper.map(listLotesEntities, LoteResponse[].class)).thenReturn(listaResponses);

    List<LoteResponse> loteResponseList = Arrays.asList(listaResponses[0], listaResponses[1]);
    assertEquals(loteResponseList.size(), loteService.getLotesVencidos().size());

  }

  @Test
  void testGetAVencer() {
    LocalDateTime fechaHoraVencida = LocalDateTime.parse("2023-12-15T00:00:00");

    LoteEntity loteEntity2 = loteEntity;
    loteEntity2.setId(2L);
    loteEntity2.setFechaVencimiento(fechaHoraVencida);

    loteEntity.setFechaVencimiento(fechaHoraVencida);

    List<LoteEntity> listLotesEntities =
            Arrays.asList(loteEntity, loteEntity2);

    loteResponse.setFechaVencimiento(fechaHoraVencida);
    LoteResponse loteResponse2 = loteResponse;
    loteResponse2.setFechaVencimiento(fechaHoraVencida);

    LoteResponse[] listaResponses = {loteResponse, loteResponse2};
    when(loteRepository.findAll(eq(Sort.by(Sort.Direction.ASC, "fechaVencimiento")))).thenReturn(listLotesEntities);
    when(modelMapper.map(listLotesEntities, LoteResponse[].class)).thenReturn(listaResponses);

    List<LoteResponse> loteResponseList = Arrays.asList(listaResponses[0], listaResponses[1]);
    assertEquals(loteResponseList.size(), loteService.getLotesAvencer(40L).size());

  }

  @Test
  void testEliminarSinStock() throws Exception {
    Method method = LoteServiceImpl.class.getDeclaredMethod("eliminarLoteSinStock", LoteEntity.class);
    method.setAccessible(true);
    loteEntity.setCantidad(0);

    when(loteRepository.existsById(any(Long.class))).thenReturn(Boolean.FALSE);
    ResponseEntity result = (ResponseEntity) method.invoke(loteService, loteEntity);
    assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

    when(loteRepository.existsById(any(Long.class))).thenReturn(Boolean.TRUE);
    result = (ResponseEntity) method.invoke(loteService, loteEntity);
    assertEquals(HttpStatus.OK, result.getStatusCode());

    loteEntity.setCantidad(10);
    result = (ResponseEntity) method.invoke(loteService, loteEntity);
    assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
  }

  @Test
  void testRestarStock() {
    assertEquals(HttpStatus.BAD_REQUEST, loteService.restarStock(loteEntity, 300).getStatusCode());

    loteEntity.setCantidad(10);

    when(loteRepository.existsById(any(Long.class))).thenReturn(Boolean.TRUE);
    assertEquals(HttpStatus.OK, loteService.restarStock(loteEntity, 10).getStatusCode());

    loteEntity.setCantidad(10);
    assertEquals(HttpStatus.OK, loteService.restarStock(loteEntity, 5).getStatusCode());

    when(loteRepository.existsById(any(Long.class))).thenReturn(Boolean.FALSE);
    assertEquals(HttpStatus.NOT_FOUND, loteService.restarStock(loteEntity, 5).getStatusCode());
  }

  @Test
  void testGetBySeccion(){
    List<LoteEntity> loteEntityList = Arrays.asList(loteEntity, loteEntity);

    when(seccionRepository.findById(any(Long.class))).thenReturn(Optional.of(seccionEntity));
    when(loteRepository.findLotesBySeccion(any(SeccionEntity.class), eq(Sort.by(Sort.Direction.ASC, "fechaVencimiento")))).thenReturn(loteEntityList);
    when(modelMapper.map(any(LoteEntity.class), eq(LoteResponse.class))).thenReturn(loteResponse);
    when(modelMapper.map(any(SeccionEntity.class), eq(SeccionResponse.class))).thenReturn(seccionResponse);

    assertEquals(loteEntityList.size(), loteService.getLotesBySeccion(1L).size());
    assertEquals(loteResponse, loteService.getLotesBySeccion(1L).get(0));
    assertEquals(loteResponse, loteService.getLotesBySeccion(1L).get(1));

    when(loteRepository.findLotesBySeccion(any(SeccionEntity.class), eq(Sort.by(Sort.Direction.ASC, "fechaVencimiento")))).thenReturn(new ArrayList<>());
    assertThrows(EntityNotFoundException.class, ()->loteService.getLotesBySeccion(1L));
  }

  @Test
  void testGetByExistencia(){
    List<LoteEntity> loteEntityList = Arrays.asList(loteEntity, loteEntity);
    ExistenciaDto existenciaDto = new ExistenciaDto();
    existenciaDto.setCodigo("2HA");
    existenciaDto.setNombre("Boltz");

    when(existenciaRepository.findByCodigo(any(String.class))).thenReturn(Optional.of(existenciaEntity));
    when(loteRepository.findByExistencia(any(ExistenciaEntity.class), eq(Sort.by(Sort.Direction.ASC, "fechaVencimiento")))).thenReturn(loteEntityList);
    when(modelMapper.map(any(LoteEntity.class), eq(LoteResponse.class))).thenReturn(loteResponse);
    when(modelMapper.map(any(ExistenciaEntity.class), eq(ExistenciaDto.class))).thenReturn(existenciaDto);

    assertEquals(loteEntityList.size(), loteService.getLotesByExistencia("2HA").size());
    assertEquals(loteResponse, loteService.getLotesByExistencia("2HA").get(0));
    assertEquals(loteResponse, loteService.getLotesByExistencia("2HA").get(1));

    when(loteRepository.findByExistencia(any(ExistenciaEntity.class), eq(Sort.by(Sort.Direction.ASC, "fechaVencimiento")))).thenReturn(new ArrayList<>());
    assertThrows(EntityNotFoundException.class, ()->loteService.getLotesByExistencia("2HA"));
  }

  @Test
  void testUpdateLote() {
    when(loteRepository.findById(any(Long.class))).thenReturn(Optional.of(loteEntity));
    when(seccionRepository.findById(any(Long.class))).thenReturn(Optional.of(seccionEntity));
    when(existenciaRepository.findById(any(String.class))).thenReturn(Optional.of(existenciaEntity));
    when(loteRepository.save(any(LoteEntity.class))).thenReturn(loteEntity);
    when(modelMapper.map(any(LoteEntity.class), eq(LoteResponse.class))).thenReturn(loteResponse);

    assertEquals(loteResponse, loteService.updateLote(loteRequest, 1L));

    when(existenciaRepository.findById(any(String.class))).thenReturn(Optional.empty());
    assertThrows(EntityNotFoundException.class, ()->loteService.updateLote(loteRequest, 1L));

    when(seccionRepository.findById(any(Long.class))).thenReturn(Optional.empty());
    assertThrows(EntityNotFoundException.class, ()->loteService.updateLote(loteRequest, 1L));

    when(loteRepository.findById(any(Long.class))).thenReturn(Optional.empty());
    assertThrows(EntityNotFoundException.class, ()->loteService.updateLote(loteRequest, 1L));
  }

  @Test
  void testEliminarLote(){
    when(loteRepository.existsById(any(Long.class))).thenReturn(Boolean.TRUE);
    assertEquals(HttpStatus.OK, loteService.eliminarLote(1L).getStatusCode());

    when(loteRepository.existsById(any(Long.class))).thenReturn(Boolean.FALSE);
    assertEquals(HttpStatus.NOT_FOUND, loteService.eliminarLote(1L).getStatusCode());
  }
}
