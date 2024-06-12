package ar.edu.utn.frc.tup.lc.iv.services.impl;

import ar.edu.utn.frc.tup.lc.iv.dtos.request.CrearSeccionRequest;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.SeccionResponse;
import ar.edu.utn.frc.tup.lc.iv.entities.LoteEntity;
import ar.edu.utn.frc.tup.lc.iv.entities.SeccionEntity;
import ar.edu.utn.frc.tup.lc.iv.entities.ZonaAlmacenamientoEntity;
import ar.edu.utn.frc.tup.lc.iv.repositories.LoteRepository;
import ar.edu.utn.frc.tup.lc.iv.repositories.SeccionRepository;
import ar.edu.utn.frc.tup.lc.iv.repositories.ZonaRepository;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SeccionServiceImplTest {
  @Mock
  private SeccionRepository seccionRepository;
  @Mock
  private ZonaRepository zonaRepository;
  @Mock
  private LoteRepository loteRepository;
  @Mock
  private ModelMapper modelMapper;
  @InjectMocks
  private SeccionServiceImpl seccionService;

  private ZonaAlmacenamientoEntity zonaAlmacenamientoEntity;
  private CrearSeccionRequest seccionRequest;
  private SeccionEntity seccionEntity;
  private SeccionResponse seccionResponse;
  private Optional<ZonaAlmacenamientoEntity> optionalZonaAlmacenamientoEntity;

  @BeforeEach
  void setup() {
    zonaAlmacenamientoEntity = new ZonaAlmacenamientoEntity();
    seccionRequest = new CrearSeccionRequest("Z", 1L);
    seccionEntity = new SeccionEntity();
    seccionResponse = new SeccionResponse(1L, "Z", "Corralito");
    optionalZonaAlmacenamientoEntity = Optional.of(zonaAlmacenamientoEntity);

    zonaAlmacenamientoEntity.setNombre("Corralito");
    zonaAlmacenamientoEntity.setId(1L);

    seccionEntity.setId(1L);
    seccionEntity.setNombre("Z");
    seccionEntity.setZona(zonaAlmacenamientoEntity);
  }

  @Test
  void testPostSeccion() throws Exception {
    when(modelMapper.map(any(SeccionEntity.class), eq(SeccionResponse.class)))
            .thenReturn(new SeccionResponse(1L, "Z", "Corralito"));

    when(zonaRepository.findById(any(Long.class))).thenReturn(Optional.empty());
    assertThrows(EntityNotFoundException.class, () -> seccionService.crearSeccion(seccionRequest));


    List<SeccionEntity> seccionesEnZona = Collections.singletonList(
            seccionEntity
    );
    when(zonaRepository.findById(any(Long.class))).thenReturn(optionalZonaAlmacenamientoEntity);
    when(seccionRepository.findAllByZona(zonaAlmacenamientoEntity)).thenReturn(seccionesEnZona);

    SeccionResponse resultadoNulo = seccionService.crearSeccion(seccionRequest);
    assertNull(resultadoNulo);


    when(zonaRepository.findById(any(Long.class))).thenReturn(optionalZonaAlmacenamientoEntity);
    when(seccionRepository.findAllByZona(any(ZonaAlmacenamientoEntity.class))).thenReturn(new ArrayList<>());
    when(seccionRepository.save(any(SeccionEntity.class))).thenReturn(seccionEntity);

    SeccionResponse resultado = seccionService.crearSeccion(seccionRequest);
    assertEquals(seccionResponse, resultado);
  }

  @Test
  void testGetSecciones() {
    when(zonaRepository.findZonaByNombre(any(String.class))).thenReturn(optionalZonaAlmacenamientoEntity);
    when(seccionRepository.findAllByZona(any(ZonaAlmacenamientoEntity.class))).thenReturn(Collections.emptyList());

    assertThrows(EntityNotFoundException.class, () -> seccionService.getSeccionesByZona("Corralito"));

    SeccionEntity seccionEntity2 = new SeccionEntity();
    seccionEntity2.setNombre("J");
    seccionEntity2.setId(2L);
    seccionEntity2.setZona(zonaAlmacenamientoEntity);

    SeccionResponse seccionResponse2 = new SeccionResponse();
    seccionResponse2.setNombre("J");
    seccionResponse2.setId(2L);
    seccionResponse2.setNombreZona("Corralito");

    when(modelMapper.map(seccionEntity, SeccionResponse.class))
            .thenReturn(new SeccionResponse(1L, "Z", "Corralito"));
    when(modelMapper.map(seccionEntity2, SeccionResponse.class))
            .thenReturn(new SeccionResponse(2L, "J", "Corralito"));

    List<SeccionEntity> listaEntities = Arrays.asList(seccionEntity, seccionEntity2);
    List<SeccionResponse> listaResponses = Arrays.asList(seccionResponse, seccionResponse2);
    when(seccionRepository.findAllByZona(any(ZonaAlmacenamientoEntity.class))).thenReturn(listaEntities);

    assertEquals(listaEntities.size(), seccionService.getSeccionesByZona("Corralito").size());
  }

  @Test
  void testDeletSeccion() throws Exception {
    when(seccionRepository.findById(any(Long.class))).thenReturn(Optional.of(seccionEntity));
    when(loteRepository.findLotesBySeccion(any(SeccionEntity.class),
            eq(Sort.by(Sort.Direction.ASC, "fechaVencimiento")))).thenReturn(Collections.emptyList());

    assertEquals(HttpStatus.OK, seccionService.deleteSeccion(1L).getStatusCode());

    when(loteRepository.findLotesBySeccion(any(SeccionEntity.class),
            eq(Sort.by(Sort.Direction.ASC, "fechaVencimiento"))))
            .thenReturn(Arrays.asList(new LoteEntity()));
    assertThrows(Exception.class, () -> seccionService.deleteSeccion(1L));

    when(seccionRepository.findById(any(Long.class))).thenReturn(Optional.empty());
    assertThrows(EntityNotFoundException.class, () -> seccionService.deleteSeccion(1L));
  }

  @Test
  void testPutSeccion() throws Exception {
    when(seccionRepository.save(any(SeccionEntity.class))).thenReturn(seccionEntity);
    when(seccionRepository.findById(any(Long.class))).thenReturn(Optional.of(seccionEntity));
    when(zonaRepository.findById(any(Long.class))).thenReturn(Optional.of(zonaAlmacenamientoEntity));
    when(modelMapper.map(seccionEntity, SeccionResponse.class))
            .thenReturn(new SeccionResponse(1L, "Z", "Corralito"));

    assertEquals(seccionResponse, seccionService.updateSeccion(seccionRequest,1L));

    when(zonaRepository.findById(any(Long.class)))
            .thenReturn(Optional.empty());
    assertThrows(EntityNotFoundException.class, () -> seccionService.updateSeccion(seccionRequest, 1L));

    when(seccionRepository.findById(any(Long.class))).thenReturn(Optional.empty());
    assertThrows(EntityNotFoundException.class, () -> seccionService.updateSeccion(seccionRequest, 1L));
  }
}
