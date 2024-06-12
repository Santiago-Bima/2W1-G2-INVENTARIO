package ar.edu.utn.frc.tup.lc.iv.controllers;

import ar.edu.utn.frc.tup.lc.iv.dtos.request.CrearExistenciaRequestDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.request.ModificarExistenciaRequestDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.*;
import ar.edu.utn.frc.tup.lc.iv.services.ExistenciaService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(ExistenciaController.class)
public class ExistenciaControllerTest {
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private ExistenciaService existenciaService;
  @Autowired
  private ObjectMapper objectMapper;
  private ProductoCatalogoDto productoCatalogoDto;
  private ExistenciaDto existenciaDto;
  private CrearExistenciaRequestDto crearExistenciaRequestDto;
  private ExistenciaTotalResponse existenciaTotalResponse;

  @BeforeEach
  void setUp(){
    existenciaTotalResponse = new ExistenciaTotalResponse();
    existenciaTotalResponse.setTotal(200);
    existenciaTotalResponse.setCodigo("2HA");
    existenciaTotalResponse.setNombre("Boltz");

    productoCatalogoDto = new ProductoCatalogoDto();
    productoCatalogoDto.setCodigo("A");
    productoCatalogoDto.setNombre("Boltz");

    existenciaDto = new ExistenciaDto();
    existenciaDto.setNombre("Boltz");
    existenciaDto.setCodigo("2HA");
    existenciaDto.setStockMinimo(100);

    crearExistenciaRequestDto = new CrearExistenciaRequestDto();
    crearExistenciaRequestDto.setNombre("Boltz");
    crearExistenciaRequestDto.setStockMinimo(100);
    crearExistenciaRequestDto.setCodigoProducto("2HA");
  }

  @Test
  void testConsultarCatalogo() throws Exception {
    ProductoCatalogoDto productoCatalogoDto2 = productoCatalogoDto;
    productoCatalogoDto2.setCodigo("A");
    productoCatalogoDto2.setNombre("Tornillo");

    ProductoCatalogoDto[] productoCatalogoList = {productoCatalogoDto, productoCatalogoDto2};
    when(existenciaService.consultarCatalogo()).thenReturn(productoCatalogoList);

    MvcResult mvcGoodResult = mockMvc.perform(MockMvcRequestBuilders
                    .get("/inventario/existencias/consultarCatalogo/")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
            .andReturn();

    String goodContent = mvcGoodResult.getResponse().getContentAsString();
    ProductoCatalogoDto[] resultadoRealBueno = objectMapper.readValue(goodContent, ProductoCatalogoDto[].class);

    assertEquals(productoCatalogoList[0], resultadoRealBueno[0]);
    assertEquals(productoCatalogoList[1], resultadoRealBueno[1]);

    when(existenciaService.consultarCatalogo()).thenReturn(null);

    MvcResult mvcBadResult = mockMvc.perform(MockMvcRequestBuilders
                    .get("/inventario/existencias/consultarCatalogo/")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isInternalServerError())
            .andReturn();

    String responseString = mvcBadResult.getResponse().getContentAsString();

    JsonNode jsonNode = objectMapper.readTree(responseString);

    String actualMessage = jsonNode.get("message").asText();
    assertEquals("Not being able to connect", actualMessage);
  }

  @Test
  void testPut() throws Exception {
    ModificarExistenciaRequestDto modificarExistenciaRequestDto = new ModificarExistenciaRequestDto();
    modificarExistenciaRequestDto.setStockMinimo(100);

    when(existenciaService.modificarExistencia(any(String.class), any(ModificarExistenciaRequestDto.class))).thenReturn(existenciaDto);

    MvcResult mvcGoodResult = mockMvc.perform(MockMvcRequestBuilders
                    .put("/inventario/existencias/modificar/{codigo}", "2HA")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(modificarExistenciaRequestDto)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

    String goodContent = mvcGoodResult.getResponse().getContentAsString();
    ExistenciaDto resultadoRealBueno = objectMapper.readValue(goodContent, ExistenciaDto.class);

    assertEquals(existenciaDto, resultadoRealBueno);


    when(existenciaService.modificarExistencia(any(String.class), any(ModificarExistenciaRequestDto.class))).thenReturn(null);

    MvcResult mvcBadResult = mockMvc.perform(MockMvcRequestBuilders
                    .put("/inventario/existencias/modificar/{codigo}", "2HA")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(modificarExistenciaRequestDto)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn();

    String responseString = mvcBadResult.getResponse().getContentAsString();

    JsonNode jsonNode = objectMapper.readTree(responseString);

    String actualMessage = jsonNode.get("message").asText();
    assertEquals("The existance doesn't exist", actualMessage);
  }

  @Test
  void testPost() throws Exception {
    when(existenciaService.crearExistencia(any(CrearExistenciaRequestDto.class))).thenReturn(existenciaDto);

    MvcResult mvcGoodResult = mockMvc.perform(MockMvcRequestBuilders
                    .post("/inventario/existencias/crear/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(crearExistenciaRequestDto)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

    String goodContent = mvcGoodResult.getResponse().getContentAsString();
    ExistenciaDto resultadoRealBueno = objectMapper.readValue(goodContent, ExistenciaDto.class);

    assertEquals(existenciaDto, resultadoRealBueno);


    when(existenciaService.crearExistencia(any(CrearExistenciaRequestDto.class))).thenReturn(null);

    MvcResult mvcBadResult = mockMvc.perform(MockMvcRequestBuilders
                    .post("/inventario/existencias/crear/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(crearExistenciaRequestDto)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn();

    String responseString = mvcBadResult.getResponse().getContentAsString();

    JsonNode jsonNode = objectMapper.readTree(responseString);

    String actualMessage = jsonNode.get("message").asText();
    assertEquals("The catalog product its alredy used", actualMessage);
  }

  @Test
  void testDelete() throws Exception {
    when(existenciaService.eliminarExistencia(any(String.class))).thenReturn(existenciaDto);

    MvcResult mvcGoodResult = mockMvc.perform(MockMvcRequestBuilders
                    .delete("/inventario/existencias/eliminar/{codigo}", "2HA")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

    String goodContent = mvcGoodResult.getResponse().getContentAsString();
    ExistenciaDto resultadoRealBueno = objectMapper.readValue(goodContent, ExistenciaDto.class);

    assertEquals(existenciaDto, resultadoRealBueno);


    when(existenciaService.eliminarExistencia(any(String.class))).thenReturn(null);

    MvcResult mvcBadResult = mockMvc.perform(MockMvcRequestBuilders
                    .delete("/inventario/existencias/eliminar/{codigo}", "2HA")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn();

    String responseString = mvcBadResult.getResponse().getContentAsString();

    JsonNode jsonNode = objectMapper.readTree(responseString);

    String actualMessage = jsonNode.get("message").asText();
    assertEquals("The existance doesn't exist", actualMessage);
  }

  @Test
  void testGetBajoStock() throws Exception {
    ExistenciaBajaDto existenciaBajaDto = new ExistenciaBajaDto();
    existenciaBajaDto.setCodigo("2HA");
    existenciaBajaDto.setNombre("Boltz");
    existenciaBajaDto.setStockMinimo(100);
    existenciaBajaDto.setStockTotal(50);

    List<ExistenciaBajaDto> existenciaBajaDtoList = Arrays.asList(existenciaBajaDto);

    when(existenciaService.getExistenciasBajas()).thenReturn(existenciaBajaDtoList);

    MvcResult mvcGoodResult = mockMvc.perform(MockMvcRequestBuilders
                    .get("/inventario/existencias/listar/bajoStock/")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

    String goodContent = mvcGoodResult.getResponse().getContentAsString();
    ExistenciaBajaDto[] resultadoRealBueno = objectMapper.readValue(goodContent, ExistenciaBajaDto[].class);

    List<ExistenciaBajaDto> resultadoReal = Arrays.asList(resultadoRealBueno[0]);

    assertEquals(existenciaBajaDtoList.get(0), resultadoReal.get(0));

    when(existenciaService.getExistenciasBajas()).thenReturn(null);

    MvcResult mvcBadResult = mockMvc.perform(MockMvcRequestBuilders
                    .get("/inventario/existencias/listar/bajoStock/")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn();

    String responseString = mvcBadResult.getResponse().getContentAsString();

    JsonNode jsonNode = objectMapper.readTree(responseString);

    String actualMessage = jsonNode.get("message").asText();
    assertEquals("There are no existances with low stock", actualMessage);
  }

  @Test
  void testGetTotalByCodigo() throws Exception {
    when(existenciaService.getTotalExistencia(any(String.class))).thenReturn(existenciaTotalResponse);

    MvcResult mvcGoodResult = mockMvc.perform(MockMvcRequestBuilders
                    .get("/inventario/existencias/total/{codigo}", "2HA")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

    String goodContent = mvcGoodResult.getResponse().getContentAsString();
    ExistenciaTotalResponse resultadoRealBueno = objectMapper.readValue(goodContent, ExistenciaTotalResponse.class);

    assertEquals(existenciaTotalResponse, resultadoRealBueno);
  }

  @Test
  void testGetTotal() throws Exception {
    List<ExistenciaTotalResponse> existenciaTotalResponseList = Arrays.asList(existenciaTotalResponse);
    when(existenciaService.getTotalExistencias()).thenReturn(existenciaTotalResponseList);

    MvcResult mvcGoodResult = mockMvc.perform(MockMvcRequestBuilders
                    .get("/inventario/existencias/total/")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
            .andReturn();

    String goodContent = mvcGoodResult.getResponse().getContentAsString();
    ExistenciaTotalResponse[] resultadoRealBueno = objectMapper.readValue(goodContent, ExistenciaTotalResponse[].class);

    List<ExistenciaTotalResponse> resultadoReal = Arrays.asList(resultadoRealBueno[0]);

    assertEquals(existenciaTotalResponseList.get(0), resultadoReal.get(0));
  }
}
