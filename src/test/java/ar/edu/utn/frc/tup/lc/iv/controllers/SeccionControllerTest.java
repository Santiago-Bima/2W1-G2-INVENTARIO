package ar.edu.utn.frc.tup.lc.iv.controllers;

import ar.edu.utn.frc.tup.lc.iv.dtos.request.CrearSeccionRequest;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.SeccionResponse;
import ar.edu.utn.frc.tup.lc.iv.services.SeccionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@WebMvcTest(SeccionController.class)
public class SeccionControllerTest {
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private SeccionService seccionService;
  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void testPostEndpoint() throws Exception {
    CrearSeccionRequest seccionRequest = new CrearSeccionRequest("Z", 1L);
    SeccionResponse seccionResponse = new SeccionResponse(1L, "Z", "Corralito");

    when(seccionService.crearSeccion(seccionRequest)).thenReturn(seccionResponse);

    MvcResult mvcGoodResult = mockMvc.perform(MockMvcRequestBuilders
                    .post("/inventario/secciones/crear/")
                    .content(objectMapper.writeValueAsString(seccionRequest))
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

    String goodContent = mvcGoodResult.getResponse().getContentAsString();
    SeccionResponse resultadoRealBueno = objectMapper.readValue(goodContent, SeccionResponse.class);

    assertEquals(seccionResponse, resultadoRealBueno);


    when(seccionService.crearSeccion(seccionRequest)).thenReturn(null);

    MvcResult mvcBadResult = mockMvc.perform(MockMvcRequestBuilders
                    .post("/inventario/secciones/crear/")
                    .content(objectMapper.writeValueAsString(seccionRequest))
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn();

    String responseString = mvcBadResult.getResponse().getContentAsString();

    JsonNode jsonNode = objectMapper.readTree(responseString);

    String actualMessage = jsonNode.get("message").asText();
    assertEquals("Section with zone id 1 and name Z already exists", actualMessage);
  }

  @Test
  public void testGetEndpoint() throws Exception {
    SeccionResponse seccionPrueba1 = new SeccionResponse(1L, "2A", "Corralito");
    SeccionResponse seccionPrueba2 = new SeccionResponse(2L, "2B", "Corralito");

    List<SeccionResponse> listaSecciones = Arrays.asList(seccionPrueba1, seccionPrueba2);

    when(seccionService.getSeccionesByZona("Corralito")).thenReturn(listaSecciones);

    MvcResult mvcGoodResult = mockMvc.perform(MockMvcRequestBuilders
                    .get("/inventario/secciones/listar/{nombreZona}", "Corralito")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
            .andReturn();

    String goodContent = mvcGoodResult.getResponse().getContentAsString();
    SeccionResponse[] resultadoRealBueno = objectMapper.readValue(goodContent, SeccionResponse[].class);

    List<SeccionResponse> resultadoReal = Arrays.asList(resultadoRealBueno[0], resultadoRealBueno[1]);
    assertEquals(listaSecciones, resultadoReal);
  }

  @Test
  public void testDeleteEndpoint() throws Exception {

    when(seccionService.deleteSeccion(1L)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

    MvcResult mvcGoodResult = mockMvc.perform(MockMvcRequestBuilders
                    .delete("/inventario/secciones/eliminar/{id}", 1L)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();
  }

  @Test
  public void testEditEndpoint() throws Exception {
    CrearSeccionRequest crearSeccionRequest = new CrearSeccionRequest("2A", 1L);
    SeccionResponse seccionResponse = new SeccionResponse(1L, "2A", "Corralito");

    when(seccionService.updateSeccion(crearSeccionRequest, 1L)).thenReturn(seccionResponse);

    MvcResult mvcGoodResult = mockMvc.perform(MockMvcRequestBuilders
            .put("/inventario/secciones/editar/{id}", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(crearSeccionRequest)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

    String goodContent = mvcGoodResult.getResponse().getContentAsString();
    SeccionResponse resultadoRealBueno = objectMapper.readValue(goodContent, SeccionResponse.class);

    assertEquals(seccionResponse, resultadoRealBueno);
  }
}
