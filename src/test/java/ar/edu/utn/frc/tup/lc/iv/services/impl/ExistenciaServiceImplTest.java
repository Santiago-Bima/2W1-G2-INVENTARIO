package ar.edu.utn.frc.tup.lc.iv.services.impl;

import ar.edu.utn.frc.tup.lc.iv.clients.CatalogoClient;
import ar.edu.utn.frc.tup.lc.iv.clients.ComprasClient;
import ar.edu.utn.frc.tup.lc.iv.dtos.request.CrearExistenciaRequestDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.request.CrearSeccionRequest;
import ar.edu.utn.frc.tup.lc.iv.dtos.request.ModificarExistenciaRequestDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.*;
import ar.edu.utn.frc.tup.lc.iv.entities.*;
import ar.edu.utn.frc.tup.lc.iv.repositories.DetalleReservaRepository;
import ar.edu.utn.frc.tup.lc.iv.repositories.ExistenciaRepository;
import ar.edu.utn.frc.tup.lc.iv.repositories.LoteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExistenciaServiceImplTest {

    @Mock
    private CatalogoClient catalogoClient;
    @Mock
    private ExistenciaRepository existenciaRepository;
    @Mock
    private LoteRepository loteRepository;
    @Mock
    private DetalleReservaRepository detalleReservaRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private ExistenciaServiceImpl existenciaService;


    @BeforeEach
    void setup() {
    }

    @Test
    void testConsultarCatalogo() throws Exception {
        ProductoCatalogoDto[] productosCatalogo = {new ProductoCatalogoDto("1","Tornillos")};
        when(catalogoClient.getAll()).thenReturn(ResponseEntity.ok(productosCatalogo));

        ProductoCatalogoDto[] resultados = existenciaService.consultarCatalogo();

        assertEquals(resultados, productosCatalogo);

    }


    @Test
    void testConsultarCatalogo2() throws Exception {
        ProductoCatalogoDto productosCatalogo = new ProductoCatalogoDto("1","Tornillos");
        when(catalogoClient.get("1")).thenReturn(ResponseEntity.ok(productosCatalogo));

        ProductoCatalogoDto resultados = catalogoClient.get("1").getBody();

        assertEquals(resultados, productosCatalogo);

    }

    @Test
    void testCrear() throws Exception {

        ProductoCatalogoDto productosCatalogo = new ProductoCatalogoDto("1","Tornillos");
        when(catalogoClient.get("1")).thenReturn(ResponseEntity.ok(productosCatalogo));

        CrearExistenciaRequestDto crearExistenciaRequestDto=new CrearExistenciaRequestDto();
        crearExistenciaRequestDto.setNombre("Tornillos");
        crearExistenciaRequestDto.setCodigoProducto("1");
        crearExistenciaRequestDto.setStockMinimo(25);

        ExistenciaEntity existenciaEntity=new ExistenciaEntity();
        existenciaEntity.setNombre("Tornillos");
        existenciaEntity.setCodigo("1");
        existenciaEntity.setStockMinimo(25);
        existenciaEntity.setLotes(new ArrayList<>());

        ExistenciaDto existenciaDto=new ExistenciaDto();
        existenciaDto.setNombre("Tornillos");
        existenciaDto.setCodigo("1");
        existenciaDto.setStockMinimo(25);

        when(existenciaRepository.save(existenciaEntity)).thenReturn(existenciaEntity);
        when(modelMapper.map(existenciaEntity, ExistenciaDto.class)).thenReturn(existenciaDto);

        ExistenciaDto e = existenciaService.crearExistencia(crearExistenciaRequestDto);


        assertEquals(e.getNombre(),crearExistenciaRequestDto.getNombre());

    }

    @Test
    void testModificar() throws Exception {
        ExistenciaEntity existenciaEntity=new ExistenciaEntity();
        existenciaEntity.setNombre("Tornillos");
        existenciaEntity.setCodigo("1");
        existenciaEntity.setStockMinimo(25);
        existenciaEntity.setLotes(new ArrayList<>());

        when(existenciaRepository.findByCodigo("1")).thenReturn(Optional.of(existenciaEntity));

        ModificarExistenciaRequestDto requestDto=new ModificarExistenciaRequestDto();
        requestDto.setStockMinimo(10);

        existenciaEntity.setStockMinimo(10);

        ExistenciaDto existenciaDto=new ExistenciaDto();
        existenciaDto.setNombre("Tornillos");
        existenciaDto.setCodigo("1");
        existenciaDto.setStockMinimo(10);

        when(existenciaRepository.save(existenciaEntity)).thenReturn(existenciaEntity);
        when(modelMapper.map(existenciaEntity, ExistenciaDto.class)).thenReturn(existenciaDto);

        existenciaDto= existenciaService.modificarExistencia("1",requestDto);

        assertEquals(existenciaDto.getStockMinimo(), 10);

    }

    @Test
    void testEliminar() throws Exception {
        when(existenciaRepository.findByCodigo("1")).thenReturn(Optional.of(new ExistenciaEntity()));
        when(modelMapper.map(any(), eq(ExistenciaDto.class))).thenReturn(new ExistenciaDto());
        when(loteRepository.findByExistencia(any(), any())).thenReturn(new ArrayList<>());
        assertNotNull(existenciaService.eliminarExistencia("1"));

    }


    @Test
    void testGetBajas() throws Exception {

        List<ExistenciaEntity> existenciaEntityList=new ArrayList<>();
        ExistenciaEntity existenciaEntity=new ExistenciaEntity();
        existenciaEntity.setNombre("Tornillos");
        existenciaEntity.setCodigo("1");
        existenciaEntity.setStockMinimo(25);
        existenciaEntity.setLotes(new ArrayList<>());
        existenciaEntityList.add(existenciaEntity);

        when(existenciaRepository.findAll()).thenReturn(existenciaEntityList);

        List<LoteEntity> lotesEntity=new ArrayList<>();
        LoteEntity loteEntity=new LoteEntity();
        loteEntity.setCantidad(10);
        lotesEntity.add(loteEntity);
        when(loteRepository.findByExistencia(any(), any())).thenReturn(lotesEntity);

        ExistenciaBajaDto eb = new ExistenciaBajaDto();
        eb.setNombre("Tornillos");
        eb.setCodigo("1");
        eb.setStockMinimo(25);
        eb.setStockTotal(10);
        when(modelMapper.map(any(), eq(ExistenciaBajaDto.class))).thenReturn(eb);

        List<ExistenciaBajaDto> existenciaDto= existenciaService.getExistenciasBajas();

        assertNotNull(existenciaDto);

    }

    @Test
    void testGetTotalExistencia() throws Exception {

        ExistenciaEntity existenciaEntity=new ExistenciaEntity();
        existenciaEntity.setNombre("Tornillos");
        existenciaEntity.setCodigo("1");
        existenciaEntity.setStockMinimo(25);
        existenciaEntity.setLotes(new ArrayList<>());
        when(existenciaRepository.findByCodigo(any())).thenReturn(Optional.of(existenciaEntity));


        List<DetalleReservaEntity> detalleReservaEntities=new ArrayList<>();
        when(detalleReservaRepository.findAllByLote(any())).thenReturn(detalleReservaEntities);

        ExistenciaTotalResponse eb = new ExistenciaTotalResponse();
        eb.setNombre("Tornillos");
        eb.setCodigo("1");
        eb.setTotal(10);
        when(modelMapper.map(any(), eq(ExistenciaTotalResponse.class))).thenReturn(eb);

        List<LoteEntity> lotesEntity=new ArrayList<>();
        LoteEntity loteEntity=new LoteEntity();
        loteEntity.setCantidad(10);
        lotesEntity.add(loteEntity);
        when(loteRepository.findByExistencia(any(), any())).thenReturn(lotesEntity);

        ExistenciaTotalResponse existenciaDto= existenciaService.getTotalExistencia("1");

        assertNotNull(existenciaDto);

    }
    @Test
    void testGetAll() throws Exception {

        ExistenciaEntity existenciaEntity=new ExistenciaEntity();
        existenciaEntity.setNombre("Tornillos");
        existenciaEntity.setCodigo("1");
        existenciaEntity.setStockMinimo(25);
        existenciaEntity.setLotes(new ArrayList<>());
        when(existenciaRepository.findByCodigo(any())).thenReturn(Optional.of(existenciaEntity));

        List<ExistenciaEntity> existenciaEntityList=new ArrayList<>();
        existenciaEntityList.add(existenciaEntity);
        when(existenciaRepository.findAll()).thenReturn(existenciaEntityList);


        List<DetalleReservaEntity> detalleReservaEntities=new ArrayList<>();
        when(detalleReservaRepository.findAllByLote(any())).thenReturn(detalleReservaEntities);

        ExistenciaTotalResponse eb = new ExistenciaTotalResponse();
        eb.setNombre("Tornillos");
        eb.setCodigo("1");
        eb.setTotal(10);
        when(modelMapper.map(any(), eq(ExistenciaTotalResponse.class))).thenReturn(eb);

        List<LoteEntity> lotesEntity=new ArrayList<>();
        LoteEntity loteEntity=new LoteEntity();
        loteEntity.setCantidad(10);
        lotesEntity.add(loteEntity);
        when(loteRepository.findByExistencia(any(), any())).thenReturn(lotesEntity);

        when(existenciaService.getTotalExistencia(any())).thenReturn(eb);

        List<ExistenciaTotalResponse> existenciaDto= existenciaService.getTotalExistencias();

        assertNotNull(existenciaDto);

    }
}
