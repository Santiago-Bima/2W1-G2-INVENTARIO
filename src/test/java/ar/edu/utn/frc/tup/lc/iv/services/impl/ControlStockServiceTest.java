package ar.edu.utn.frc.tup.lc.iv.services.impl;

import ar.edu.utn.frc.tup.lc.iv.configs.MappersConfig;
import ar.edu.utn.frc.tup.lc.iv.dtos.request.CrearControlStockRequest;
import ar.edu.utn.frc.tup.lc.iv.dtos.request.ModificarControlStockRequest;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.ControlStockResponse;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.DetalleEstadisticaResponse;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.EstadisticaResponse;
import ar.edu.utn.frc.tup.lc.iv.entities.*;
import ar.edu.utn.frc.tup.lc.iv.repositories.ControlStockRepository;
import ar.edu.utn.frc.tup.lc.iv.repositories.LoteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
@Import(MappersConfig.class)
public class ControlStockServiceTest {
    @Mock
    private ControlStockRepository controlStockRepository;
    @Mock
    private LoteRepository loteRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ControlStockServiceImpl controlStockService;

    @Autowired
    private ObjectMapper objectMapper;

    private final LoteEntity loteEntity = new LoteEntity();
    private final ControlDeStockEntity controlDeStockEntity = new ControlDeStockEntity(
            1L,
            loteEntity,
            "Descripción del control de stock",
            100.5,
            5.0,
            false,
            LocalDateTime.now()
    );

    @BeforeEach
    public void config(){
        loteEntity.setId(123L);
        loteEntity.setCantidad(10);
        loteEntity.setEstante(1);
        loteEntity.setSeccion(new SeccionEntity());
        loteEntity.setFechaVencimiento(LocalDateTime.now());
        loteEntity.setExistencia(new ExistenciaEntity());
    }

    @Test
    @Tag("crearControlStockTest-LoteNotFounded")
    public void crearControlStockTest(){
        CrearControlStockRequest controlStockRequest = new CrearControlStockRequest(
                123L,
                "Descripción del control de stock",
                100.5,
                5.0,
                false,
                LocalDateTime.now()
        );

        when(loteRepository.findById(controlStockRequest.getLoteId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
        { controlStockService.crearControlStock(controlStockRequest); });
    }

    @Test
    @Tag("crearControlStockTest-Exitoso")
    public void crearControlStockTest1(){
        CrearControlStockRequest controlStockRequest = new CrearControlStockRequest(
                123L,
                "Descripción del control de stock",
                100.5,
                5.0,
                false,
                LocalDateTime.now()
        );

        ControlStockResponse response = new ControlStockResponse(
                1L,
                123L,
                "Descripción del control de stock",
                100.5,
                5.0,
                false,
                LocalDateTime.now()
        );

        when(loteRepository.findById(controlStockRequest.getLoteId())).thenReturn(Optional.of(loteEntity));
        when(modelMapper.map(any(), eq(ControlDeStockEntity.class)))
                .thenReturn(controlDeStockEntity);
        when(modelMapper.map(any(), eq(ControlStockResponse.class)))
                .thenReturn(response);

        ControlStockResponse result = controlStockService.crearControlStock(controlStockRequest);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(123L, result.getLoteId());
    }

    @Test
    @Tag("modificarControlTest-ControlNotFounded")
    public void modificarControlTest1(){
        Long controlId = 1L;
        ModificarControlStockRequest modificarControlStockRequest = new ModificarControlStockRequest(
                123L,
                "Nueva descripción del control de stock",
                75.0,
                2.5,
                true
        );

        when(controlStockRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            controlStockService.modificarControlStock(1L, modificarControlStockRequest);
        });
    }

    @Test
    @Tag("modificarControlTest-LoteNotFounded")
    public void modificarControlTest2(){
        Long controlId = 1L;
        ModificarControlStockRequest modificarControlStockRequest = new ModificarControlStockRequest(
                123L,
                "Nueva descripción del control de stock",
                75.0,
                2.5,
                true
        );

        when(controlStockRepository.findById(1L)).thenReturn(Optional.of(new ControlDeStockEntity()));
        when(loteRepository.findById(123L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            controlStockService.modificarControlStock(1L, modificarControlStockRequest);
        });
    }

    @Test
    @Tag("modificarControlTest-Exitoso")
    public void modificarControlTest3(){
        Long controlId = 1L;
        ModificarControlStockRequest modificarControlStockRequest = new ModificarControlStockRequest(
                123L,
                "Nueva descripción del control de stock",
                75.0,
                2.5,
                true
        );

        when(controlStockRepository.findById(1L)).thenReturn(Optional.of(controlDeStockEntity));
        when(loteRepository.findById(123L)).thenReturn(Optional.of(loteEntity));
        when(modelMapper.map(any(), eq(ControlStockResponse.class)))
                .thenReturn(new ControlStockResponse());

        ControlStockResponse result = controlStockService
                .modificarControlStock(controlId, modificarControlStockRequest);

        assertNotNull(result);
    }

    @Test
    @Tag("eliminarControl-ControlNotFounded")
    public void eliminarControlTest(){
        Long controlId = 1L;

        when(controlStockRepository.findById(controlId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            controlStockService.eliminarControlStock(controlId);
        });
    }

    @Test
    @Tag("eliminarControl-exitoso")
    public void eliminarControlTest1(){
        Long controlId = 1L;

        when(controlStockRepository.findById(controlId)).thenReturn(Optional.of(new ControlDeStockEntity()));
        when(modelMapper.map(any(), eq(ControlStockResponse.class))).thenReturn(new ControlStockResponse());

        assertNotNull(controlStockService.eliminarControlStock(controlId));
    }

    @Test
    @Tag("listarControlesUltimoMes")
    public void listarControlesUltimoMesTest(){
        List<ControlDeStockEntity> controlDeStockEntityList = List.of(
                new ControlDeStockEntity(1L, loteEntity, "Descripción 1",
                        50.0, 2.0, false, LocalDateTime.now()),
                new ControlDeStockEntity(2L, loteEntity, "Descripción 2",
                        75.0, 5.0, true,
                        LocalDateTime.parse("2023-10-26T01:13:32.512"))

        );

        when(controlStockRepository.findAll()).thenReturn(controlDeStockEntityList);

        List<ControlStockResponse> result = controlStockService.listarControlesDelUltimoMes();

        assertEquals(1, result.size());
    }

    @Test
    @Tag("listarControlesUltimoMes")
    public void listarControlesUltimoMesTest2(){
        List<ControlDeStockEntity> controlDeStockEntityList = List.of(
                new ControlDeStockEntity(1L, loteEntity, "Descripción 1",
                        50.0, 2.0, false,
                        LocalDateTime.parse("2023-10-26T01:13:32.512")),
                new ControlDeStockEntity(2L, loteEntity, "Descripción 2",
                        75.0, 5.0, true,
                        LocalDateTime.parse("2023-10-26T01:13:32.512"))

        );

        when(controlStockRepository.findAll()).thenReturn(controlDeStockEntityList);

        List<ControlStockResponse> result = controlStockService.listarControlesDelUltimoMes();

        assertNotNull(result);
    }

    @Test
    @Tag("listarControles")
    public void listarControles(){
        ControlStockResponse[] controlStockResponses = {
                new ControlStockResponse(1L, 123L, "Descripción 1",
                        50.0, 2.0, false, LocalDateTime.now()),
                new ControlStockResponse(2L, 123L, "Descripción 2",
                        75.0, 5.0, true,
                        LocalDateTime.parse("2023-10-26T01:13:32.512"))

        };

        when(modelMapper.map(any(), eq(ControlStockResponse[].class))).thenReturn(controlStockResponses);

        ControlStockResponse[] result = controlStockService.getControlesDeStock();

        assertEquals(2, result.length);
    }

    @Test
    @Tag("testearEstadisticas")
    public void estadisticasTest(){
        LocalDateTime fecha = LocalDateTime.now();

        ExistenciaEntity existenciaEntity = new ExistenciaEntity();
        existenciaEntity.setCodigo("A");
        existenciaEntity.setNombre("A");
        existenciaEntity.setStockMinimo(1);

        SeccionEntity seccionEntity = new SeccionEntity();
        seccionEntity.setId(1L);
        seccionEntity.setNombre("A");
        ZonaAlmacenamientoEntity zonaAlmacenamientoEntity = new ZonaAlmacenamientoEntity();
        zonaAlmacenamientoEntity.setId(1L);
        zonaAlmacenamientoEntity.setNombre("A");
        seccionEntity.setZona(zonaAlmacenamientoEntity);

        LoteEntity loteEntity =
                new LoteEntity(1L, fecha,1,1,seccionEntity,existenciaEntity);

        ControlDeStockEntity[] controlStockResponses = {
                new ControlDeStockEntity(1L, loteEntity, "Descripción 1",
                        50.0, 2.0, false, LocalDateTime.now()),
                new ControlDeStockEntity(2L, loteEntity, "Descripción 2",
                        75.0, 5.0, true,
                        LocalDateTime.parse("2023-10-26T01:13:32.512"))

        };
        when(controlStockRepository.findAll()).thenReturn(List.of(controlStockResponses));

        DetalleEstadisticaResponse[] detalles = {
                new DetalleEstadisticaResponse("A",5,5, BigDecimal.valueOf(100))
        };
        when(modelMapper.map(any(), eq(DetalleEstadisticaResponse[].class))).thenReturn(detalles);

        EstadisticaResponse result = controlStockService.getEstadisticas("vencidas");

        assertEquals(1, result.getItems().length);
    }

}
