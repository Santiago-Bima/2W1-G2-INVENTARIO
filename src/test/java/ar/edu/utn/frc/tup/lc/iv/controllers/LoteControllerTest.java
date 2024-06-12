package ar.edu.utn.frc.tup.lc.iv.controllers;

import ar.edu.utn.frc.tup.lc.iv.dtos.request.CrearSeccionRequest;
import ar.edu.utn.frc.tup.lc.iv.dtos.request.LoteRequest;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.ExistenciaDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.LoteResponse;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.SeccionResponse;
import ar.edu.utn.frc.tup.lc.iv.services.LoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;


@WebMvcTest(LoteController.class)
public class LoteControllerTest {
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private LoteService loteService;
  @Autowired
  private ObjectMapper objectMapper;
  private final LocalDateTime fechaHora = LocalDateTime.parse("2024-12-15T00:00:00");
  private LoteRequest loteRequest;
  private SeccionResponse seccionResponse;
  private LoteResponse loteResponse;

  @BeforeEach()
  void setUp(){
    loteRequest = new LoteRequest(fechaHora, 100, 4, "2HA", 1L);
    seccionResponse = new SeccionResponse(1L, "Z", "Corralito");
    loteResponse = new LoteResponse(1L, 100, 4, seccionResponse, new ExistenciaDto(), fechaHora);
  }

  @Test
  public void testPostEndpoint() throws Exception {
    when(loteService.postLote(loteRequest)).thenReturn(loteResponse);

    MvcResult mvcGoodResult = mockMvc.perform(MockMvcRequestBuilders
                    .post("/inventario/lotes/crear/")
                    .content(objectMapper.writeValueAsString(loteRequest))
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

    String goodContent = mvcGoodResult.getResponse().getContentAsString();
    LoteResponse resultadoRealBueno = objectMapper.readValue(goodContent, LoteResponse.class);

    assertEquals(loteResponse, resultadoRealBueno);
  }

  @Test
  public void testGetEndpoint() throws Exception {
    LoteResponse loteResponse2 = new LoteResponse(2L, 150, 4, seccionResponse, new ExistenciaDto(), fechaHora);
    LoteResponse[] listaLotes = {loteResponse, loteResponse2};

    when(loteService.getLotes()).thenReturn(listaLotes);

    MvcResult mvcGoodResult = mockMvc.perform(MockMvcRequestBuilders
                    .get("/inventario/lotes/listar/")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
            .andReturn();

    String goodContent = mvcGoodResult.getResponse().getContentAsString();
    LoteResponse[] resultadoRealBueno = objectMapper.readValue(goodContent, LoteResponse[].class);

    assertEquals(listaLotes[0], resultadoRealBueno[0]);
    assertEquals(listaLotes[1], resultadoRealBueno[1]);
  }

  @Test
  public void testGetBySeccionEndpoint() throws Exception {
    LoteResponse loteResponse2 = new LoteResponse(2L, 150, 4, seccionResponse, new ExistenciaDto(), fechaHora);
    List<LoteResponse> listaLotes = Arrays.asList(loteResponse, loteResponse2);

    when(loteService.getLotesBySeccion(any(Long.class))).thenReturn(listaLotes);

    MvcResult mvcGoodResult = mockMvc.perform(MockMvcRequestBuilders
                    .get("/inventario/lotes/listarPorSección/{idSeccion}", 1L)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
            .andReturn();

    String goodContent = mvcGoodResult.getResponse().getContentAsString();
    LoteResponse[] resultadoRealBueno = objectMapper.readValue(goodContent, LoteResponse[].class);
    List<LoteResponse> resultadoReal = Arrays.asList(resultadoRealBueno[0], resultadoRealBueno[1]);

    assertEquals(listaLotes.get(0), resultadoReal.get(0));
    assertEquals(listaLotes.get(1), resultadoReal.get(1));
  }

  @Test
  public void testGetByExistenciaEndpoint() throws Exception {
    LoteResponse loteResponse2 = new LoteResponse(2L, 150, 4, seccionResponse, new ExistenciaDto(), fechaHora);
    List<LoteResponse> listaLotes = Arrays.asList(loteResponse, loteResponse2);

    when(loteService.getLotesByExistencia(any(String.class))).thenReturn(listaLotes);

    MvcResult mvcGoodResult = mockMvc.perform(MockMvcRequestBuilders
                    .get("/inventario/lotes/listarPorExistencia/{existenceCode}", "2HA")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
            .andReturn();

    String goodContent = mvcGoodResult.getResponse().getContentAsString();
    LoteResponse[] resultadoRealBueno = objectMapper.readValue(goodContent, LoteResponse[].class);
    List<LoteResponse> resultadoReal = Arrays.asList(resultadoRealBueno[0], resultadoRealBueno[1]);

    assertEquals(listaLotes.get(0), resultadoReal.get(0));
    assertEquals(listaLotes.get(1), resultadoReal.get(1));
  }

  @Test
  public void testEditEndpoint() throws Exception {
    when(loteService.updateLote(loteRequest, 1L)).thenReturn(loteResponse);

    MvcResult mvcGoodResult = mockMvc.perform(MockMvcRequestBuilders
                    .put("/inventario/lotes/editar/{id}", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loteRequest)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

    String goodContent = mvcGoodResult.getResponse().getContentAsString();
    LoteResponse resultadoRealBueno = objectMapper.readValue(goodContent, LoteResponse.class);

    assertEquals(loteResponse, resultadoRealBueno);
  }

  @Test
  public void testGetVencidosEndpoint() throws Exception {
    LocalDateTime fechaHoraVencida = LocalDateTime.parse("2023-10-15T00:00:00");

    loteResponse.setFechaVencimiento(fechaHoraVencida);
    LoteResponse loteResponse2 = new LoteResponse(2L, 150, 4, seccionResponse, new ExistenciaDto(), fechaHoraVencida);
    List<LoteResponse> listaLotes = Arrays.asList(loteResponse, loteResponse2);

    when(loteService.getLotesVencidos()).thenReturn(listaLotes);

    MvcResult mvcGoodResult = mockMvc.perform(MockMvcRequestBuilders
                    .get("/inventario/lotes/listarVencidos/")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
            .andReturn();

    String goodContent = mvcGoodResult.getResponse().getContentAsString();
    LoteResponse[] resultadoRealBueno = objectMapper.readValue(goodContent, LoteResponse[].class);
    List<LoteResponse> resultadoReal = Arrays.asList(resultadoRealBueno[0], resultadoRealBueno[1]);

    assertEquals(listaLotes.get(0), resultadoReal.get(0));
    assertEquals(listaLotes.get(1), resultadoReal.get(1));
  }

  @Test
  public void testGetAVencerEndpoint() throws Exception {
    LocalDateTime fechaHoraVencida = LocalDateTime.parse("2023-10-15T00:00:00");

    loteResponse.setFechaVencimiento(fechaHoraVencida);
    LoteResponse loteResponse2 = new LoteResponse(2L, 150, 4, seccionResponse, new ExistenciaDto(), fechaHoraVencida);
    List<LoteResponse> listaLotes = Arrays.asList(loteResponse, loteResponse2);

    when(loteService.getLotesAvencer(any(Long.class))).thenReturn(listaLotes);

    MvcResult mvcGoodResult = mockMvc.perform(MockMvcRequestBuilders
                    .get("/inventario/lotes/listarAVencer/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("dias", "40"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
            .andReturn();

    String goodContent = mvcGoodResult.getResponse().getContentAsString();
    LoteResponse[] resultadoRealBueno = objectMapper.readValue(goodContent, LoteResponse[].class);
    List<LoteResponse> resultadoReal = Arrays.asList(resultadoRealBueno[0], resultadoRealBueno[1]);

    assertEquals(listaLotes.get(0), resultadoReal.get(0));
    assertEquals(listaLotes.get(1), resultadoReal.get(1));
  }

  @Test
  void testDeleteEndpoint() throws Exception {
    when(loteService.eliminarLote(any(Long.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));

    MvcResult mvcGoodResult = mockMvc.perform(MockMvcRequestBuilders
                    .delete("/inventario/lotes/eliminar/{id}", 1L)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

    assertEquals(HttpStatus.OK, loteService.eliminarLote(1L).getStatusCode());
  }
}
