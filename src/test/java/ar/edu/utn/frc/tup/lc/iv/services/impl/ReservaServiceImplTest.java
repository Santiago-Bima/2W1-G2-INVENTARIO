package ar.edu.utn.frc.tup.lc.iv.services.impl;

import ar.edu.utn.frc.tup.lc.iv.configs.MappersConfig;
import ar.edu.utn.frc.tup.lc.iv.dtos.request.CrearReservaRequest;
import ar.edu.utn.frc.tup.lc.iv.dtos.request.DetalleCrearReservaRequest;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.*;
import ar.edu.utn.frc.tup.lc.iv.entities.*;
import ar.edu.utn.frc.tup.lc.iv.repositories.DetalleReservaRepository;
import ar.edu.utn.frc.tup.lc.iv.repositories.ExistenciaRepository;
import ar.edu.utn.frc.tup.lc.iv.repositories.LoteRepository;
import ar.edu.utn.frc.tup.lc.iv.repositories.ReservaRepository;
import ar.edu.utn.frc.tup.lc.iv.services.LoteService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@Import(MappersConfig.class)
public class ReservaServiceImplTest {
    @Spy
    private ModelMapper modelMapper;

    @Mock
    private DetalleReservaRepository detalleReservaRepository;

    @Mock
    private ExistenciaRepository existenciaRepository;

    @Mock
    private LoteRepository loteRepository;

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private LoteServiceImpl loteService;

    @InjectMocks
    private ReservaServiceImpl reservaService;

    @Test
    public void testCrear(){

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

        List<LoteEntity> loteEntityList = List.of(
                new LoteEntity(1L, LocalDateTime.now(),1,1,seccionEntity,existenciaEntity));

        List<DetalleCrearReservaRequest> detalles =
                List.of(new DetalleCrearReservaRequest("A",1));
        CrearReservaRequest request = new CrearReservaRequest();
        request.setStockReservado(detalles);

        List<DetalleReservaEntity> detallesEntity = new ArrayList<>();
        ReservaEntity entity = new ReservaEntity();
        entity.setId(1L);
        entity.setDetalles(detallesEntity);

        when(existenciaRepository.findById("A")).thenReturn(Optional.of(existenciaEntity));
        when(loteRepository.findByExistencia(any(),any())).
                thenReturn(loteEntityList);
        when(detalleReservaRepository.findAllByLote(any())).thenReturn(detallesEntity);
        when(reservaRepository.save(any())).thenReturn(entity);

        ReservaResumenResponse responce = reservaService.crearReserva(request);

        ExistenciaDto existenciaDto = new ExistenciaDto("A","A",1);
        DetalleResumenReserva detalleesperado = new DetalleResumenReserva(existenciaDto,1);
        ReservaResumenResponse esperado = new ReservaResumenResponse(1L,List.of(detalleesperado));

        assertEquals(esperado, responce);
    }

    @Test
    public void testConsultar(){
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


        List<DetalleReservaEntity> detallesEntity = new ArrayList<>();
        DetalleReservaEntity detalle = new DetalleReservaEntity();
        detalle.setId(1L);
        detalle.setCantidad(1);
        detalle.setLote(loteEntity);
        detallesEntity.add(detalle);

        ReservaEntity entity = new ReservaEntity();
        detalle.setReserva(entity);
        entity.setId(1L);
        entity.setDetalles(detallesEntity);

        when(reservaRepository.findById(any())).thenReturn(Optional.of(entity));

        ReservaDto responce = reservaService.consultarReserva(1L);

        ExistenciaDto existenciaDto = new ExistenciaDto("A","A",1);
        SeccionResponse seccionResponse = new SeccionResponse(1L,"A","A");
        LoteResponse loteResponse = new LoteResponse(1L,1,1,seccionResponse,existenciaDto,fecha);
        DetalleReservaDto detalleesperado = new DetalleReservaDto(loteResponse,1);
        ReservaDto esperado = new ReservaDto(1L,List.of(detalleesperado));

        assertEquals(esperado, responce);
    }

    @Test
    public void testListar(){
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


        List<DetalleReservaEntity> detallesEntity = new ArrayList<>();
        DetalleReservaEntity detalle = new DetalleReservaEntity();
        detalle.setId(1L);
        detalle.setCantidad(1);
        detalle.setLote(loteEntity);
        detallesEntity.add(detalle);

        ReservaEntity entity = new ReservaEntity();
        detalle.setReserva(entity);
        entity.setId(1L);
        entity.setDetalles(detallesEntity);
        List<ReservaEntity> entityList = new ArrayList<>();
        entityList.add(entity);

        when(reservaRepository.findAll()).thenReturn(entityList);

        List<ReservaResumenResponse> responce = reservaService.listarReservas();

        ExistenciaDto existenciaDto = new ExistenciaDto("A","A",1);
        DetalleResumenReserva detalleesperado = new DetalleResumenReserva(existenciaDto,1);
        ReservaResumenResponse esperado = new ReservaResumenResponse(1L,List.of(detalleesperado));
        List<ReservaResumenResponse> esperadoList = new ArrayList<>();
        esperadoList.add(esperado);

        assertEquals(esperadoList, responce);
    }

    @Test
    public void testConfirmar(){
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

        List<DetalleReservaEntity> detallesEntity = new ArrayList<>();
        DetalleReservaEntity detalle = new DetalleReservaEntity();
        detalle.setId(1L);
        detalle.setCantidad(1);
        detalle.setLote(loteEntity);
        detallesEntity.add(detalle);

        ReservaEntity entity = new ReservaEntity();
        detalle.setReserva(entity);
        entity.setId(1L);
        entity.setDetalles(detallesEntity);

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(loteService.restarStock(any(),any())).thenReturn(null);

        ReservaResumenResponse responce = reservaService.confirmarReserva(1L);

        ExistenciaDto existenciaDto = new ExistenciaDto("A","A",1);
        DetalleResumenReserva detalleesperado = new DetalleResumenReserva(existenciaDto,1);
        ReservaResumenResponse esperado = new ReservaResumenResponse(1L,List.of(detalleesperado));

        assertEquals(esperado, responce);
    }


    @Test
    public void testEliminar(){
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

        List<DetalleReservaEntity> detallesEntity = new ArrayList<>();
        DetalleReservaEntity detalle = new DetalleReservaEntity();
        detalle.setId(1L);
        detalle.setCantidad(1);
        detalle.setLote(loteEntity);
        detallesEntity.add(detalle);

        ReservaEntity entity = new ReservaEntity();
        detalle.setReserva(entity);
        entity.setId(1L);
        entity.setDetalles(detallesEntity);

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(entity));

        ReservaResumenResponse responce = reservaService.eliminarReserva(1L);

        ExistenciaDto existenciaDto = new ExistenciaDto("A","A",1);
        DetalleResumenReserva detalleesperado = new DetalleResumenReserva(existenciaDto,1);
        ReservaResumenResponse esperado = new ReservaResumenResponse(1L,List.of(detalleesperado));

        assertEquals(esperado, responce);
    }

    @Test
    public void testModificar(){
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
        List<LoteEntity> loteEntityList = List.of(
                new LoteEntity(1L, LocalDateTime.now(),1,1,seccionEntity,existenciaEntity));


        List<DetalleReservaEntity> detallesEntity = new ArrayList<>();
        DetalleReservaEntity detalle = new DetalleReservaEntity();
        detalle.setId(1L);
        detalle.setCantidad(1);
        detalle.setLote(loteEntity);
        detallesEntity.add(detalle);

        ReservaEntity entity = new ReservaEntity();
        detalle.setReserva(entity);
        entity.setId(1L);
        entity.setDetalles(detallesEntity);

        CrearReservaRequest crearReservaRequest = new CrearReservaRequest();
        List<DetalleCrearReservaRequest> detalleCrearReservaRequests = List.of(new DetalleCrearReservaRequest("A",1L));
        crearReservaRequest.setStockReservado(detalleCrearReservaRequests);


        when(existenciaRepository.findById("A")).thenReturn(Optional.of(existenciaEntity));
        when(loteRepository.findByExistencia(any(),any())).thenReturn(loteEntityList);
        when(detalleReservaRepository.findAllByLote(any())).thenReturn(detallesEntity);
        when(reservaRepository.save(any())).thenReturn(entity);
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(entity));

        ReservaResumenResponse responce = reservaService.modificarReserva(crearReservaRequest,1L);

        ExistenciaDto existenciaDto = new ExistenciaDto("A","A",1);
        DetalleResumenReserva detalleesperado = new DetalleResumenReserva(existenciaDto,1);
        ReservaResumenResponse esperado = new ReservaResumenResponse(1L,List.of(detalleesperado));

        assertEquals(esperado, responce);
    }
}
