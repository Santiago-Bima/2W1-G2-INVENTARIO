package ar.edu.utn.frc.tup.lc.iv.services.impl;

import ar.edu.utn.frc.tup.lc.iv.clients.ComprasClient;
import ar.edu.utn.frc.tup.lc.iv.dtos.request.CrearDetalleRemitoRequest;
import ar.edu.utn.frc.tup.lc.iv.dtos.request.CrearRemitoRequest;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.DetalleRemitoDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.RemitoDto;
import ar.edu.utn.frc.tup.lc.iv.entities.RemitoEntity;
import ar.edu.utn.frc.tup.lc.iv.repositories.DetalleRemitoRepository;
import ar.edu.utn.frc.tup.lc.iv.repositories.RemitoRepository;
import ar.edu.utn.frc.tup.lc.iv.services.impl.RemitoServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RemitoServiceTest {
    @Mock
    @Autowired
    private ModelMapper modelMapper;
    @Mock
    private RemitoRepository remitoRepository;
    @Mock
    private DetalleRemitoRepository detalleRemitoRepository;
    @Mock
    private ComprasClient comprasClient;
    @Autowired
    @InjectMocks
    private RemitoServiceImpl remitoService;

    @Test
    @Tag("createRemitoTest1")
    public void createRemitoTest1() {
        CrearRemitoRequest crearRemitoRequest = new CrearRemitoRequest(
                1L,
                LocalDateTime.parse("2023-10-26T01:13:32.512"),
                12345,
                "Some description",
                "Some supplier",
                List.of(
                        new CrearDetalleRemitoRequest(5.0, "Sample Product",
                                "Sample detail description")
                )
        );

        RemitoDto crearRemitoResponse = new RemitoDto(
                4L,
                1L,
                12345,
                "Some description",
                LocalDateTime.parse("2023-10-26T01:13:32.512"),
                "Some supplier",
                List.of(
                        new DetalleRemitoDto(7L,5.0, "Sample Product",
                                "Sample detail description")
                )
        );

        when(remitoRepository.findByNroRemitoAndNombreProveedor(1L, "Some supplier"))
                .thenReturn(Optional.empty());
        when(remitoRepository.save(modelMapper.map(crearRemitoRequest, RemitoEntity.class))).thenReturn(
                modelMapper.map(crearRemitoResponse, RemitoEntity.class));

        when(comprasClient.postRemitoId(crearRemitoResponse.getId())).thenReturn(ResponseEntity.ok(4L));

        RemitoDto result = remitoService.crearRemito(crearRemitoRequest);

        assertEquals(crearRemitoResponse.getId(), result.getId());
    }

    @Test
    @Tag("createRemitoTest2")
    public void createRemitoTest2() {
        CrearRemitoRequest crearRemitoRequest = new CrearRemitoRequest(
                1L,
                LocalDateTime.parse("2023-10-26T01:13:32.512"),
                12345,
                "Some description",
                "nombre 1",
                List.of(
                        new CrearDetalleRemitoRequest(5.0, "Sample Product",
                                "Sample detail description")
                )
        );

        when(remitoRepository.findByNroRemitoAndNombreProveedor(1L, "nombre 1"))
                .thenReturn(Optional.of(new RemitoEntity()));


        assertThrows(ResponseStatusException.class, () ->{remitoService.crearRemito(crearRemitoRequest);});
    }

    @Transactional
    @Test
    @Tag("listRemitoTest")
    public void listRemitoTest(){
        assertEquals(3, remitoService.listarRemitos().size());
    }

    @Transactional
    @Test
    @Tag("modificarRemitoTest1")
    public void modificarRemitoTest1(){
        CrearRemitoRequest modificarRemitoRequest = new CrearRemitoRequest(
                1L,
                LocalDateTime.parse("2023-10-26T01:13:32.512"),
                1,
                "coincide con la orden de compra",
                "nombre 1",
                List.of(
                        new CrearDetalleRemitoRequest(5.0, "Tornillo",
                                "Tornillo")
                )
        );

        RemitoDto result = remitoService.modificarRemito(modificarRemitoRequest, 1L);

        assertEquals(modificarRemitoRequest.getFechaLlegada(), result.getFechaLlegada());
        assertEquals(modificarRemitoRequest.getDetalles().get(0).getCantidad(),
                result.getDetalles().get(0).getCantidad());
        assertEquals(1, result.getDetalles().size());
    }

    @Test
    @Tag("modificarRemitoTest2")
    public void modificarRemitoTest2(){
        CrearRemitoRequest modificarRemitoRequest = new CrearRemitoRequest(
                1L,
                LocalDateTime.parse("2023-10-26T01:13:32.512"),
                1,
                "coincide con la orden de compra",
                "nombre 1",
                List.of(
                        new CrearDetalleRemitoRequest(5.0, "Tornillo",
                                "Tornillo")
                )
        );

        assertThrows(EntityNotFoundException.class, () ->
        {remitoService.modificarRemito(modificarRemitoRequest, 5L);});
    }

    @Transactional
    @Test
    @Tag("modificarRemitoTest3")
    public void modificarRemitoTest3(){
        CrearRemitoRequest modificarRemitoRequest = new CrearRemitoRequest(
                1L,
                LocalDateTime.parse("2023-10-26T01:13:32.512"),
                1,
                "coincide con la orden de compra",
                "nombre 1",
                List.of(
                        new CrearDetalleRemitoRequest(5.0, "Tornillo",
                                "Tornillo"),
                        new CrearDetalleRemitoRequest(5.0, "Tornillo",
                                "Tornillo")
                )
        );

        RemitoDto result = remitoService.modificarRemito(modificarRemitoRequest, 1L);

        assertEquals(modificarRemitoRequest.getFechaLlegada(), result.getFechaLlegada());
        assertEquals(modificarRemitoRequest.getDetalles().get(0).getCantidad(),
                result.getDetalles().get(0).getCantidad());
    }
}
