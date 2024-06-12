package ar.edu.utn.frc.tup.lc.iv.controllers;

import ar.edu.utn.frc.tup.lc.iv.dtos.request.CrearReservaRequest;
import ar.edu.utn.frc.tup.lc.iv.dtos.request.DetalleCrearReservaRequest;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.*;
import ar.edu.utn.frc.tup.lc.iv.services.RemitoService;
import ar.edu.utn.frc.tup.lc.iv.services.ReservaService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.when;

@WebMvcTest(ReservaController.class)
public class ReservaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservaService reservaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testPost() throws Exception {

        ExistenciaDto existenciaDto = new ExistenciaDto("A","A",1);
        DetalleResumenReserva detalleesperado = new DetalleResumenReserva(existenciaDto,1);
        ReservaResumenResponse esperado = new ReservaResumenResponse(1L,List.of(detalleesperado));

        when(reservaService.crearReserva(any())).thenReturn(esperado);

        List<DetalleCrearReservaRequest> detalles =
                List.of(new DetalleCrearReservaRequest("A",1));
        CrearReservaRequest request = new CrearReservaRequest();
        request.setStockReservado(detalles);

        MvcResult mvcGoodResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/inventario/reservas/crear")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String goodContent = mvcGoodResult.getResponse().getContentAsString();
        ReservaResumenResponse response = objectMapper.readValue(goodContent, ReservaResumenResponse.class);

        assertEquals(esperado, response);

        //error
        when(reservaService.crearReserva(any())).thenReturn(null);

        MvcResult mvcBadResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/inventario/reservas/crear")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andReturn();

        String responseString = mvcBadResult.getResponse().getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(responseString);

        String actualMessage = jsonNode.get("message").asText();
        assertEquals("Not being able to connect", actualMessage);
    }

    @Test
    public void testListar() throws Exception {

        ExistenciaDto existenciaDto = new ExistenciaDto("A","A",1);
        DetalleResumenReserva detalleesperado = new DetalleResumenReserva(existenciaDto,1);
        ReservaResumenResponse esperado = new ReservaResumenResponse(1L,List.of(detalleesperado));

        List<ReservaResumenResponse> listaEsperada = List.of(esperado);

        when(reservaService.listarReservas()).thenReturn(listaEsperada);

        MvcResult mvcGoodResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/inventario/reservas/listar/no-confirmadas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                .andReturn();

        String goodContent = mvcGoodResult.getResponse().getContentAsString();
        ReservaResumenResponse[] response = objectMapper.readValue(goodContent, ReservaResumenResponse[].class);

        assertEquals(listaEsperada, List.of(response));


        //error
        when(reservaService.listarReservas()).thenReturn(null);

        MvcResult mvcBadResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/inventario/reservas/listar/no-confirmadas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andReturn();

        String responseString = mvcBadResult.getResponse().getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(responseString);

        String actualMessage = jsonNode.get("message").asText();
        assertEquals("Not being able to connect", actualMessage);
    }


    @Test
    public void testConsultar() throws Exception {
        LocalDateTime fecha = LocalDateTime.now();

        ExistenciaDto existenciaDto = new ExistenciaDto("A","A",1);
        SeccionResponse seccionResponse = new SeccionResponse(1L,"A","A");
        LoteResponse loteResponse = new LoteResponse(1L,1,1,seccionResponse,existenciaDto,fecha);
        DetalleReservaDto detalleesperado = new DetalleReservaDto(loteResponse,1);
        ReservaDto esperado = new ReservaDto(1L,List.of(detalleesperado));

        when(reservaService.consultarReserva(1L)).thenReturn(esperado);

        MvcResult mvcGoodResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/inventario/reservas/consultar/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String goodContent = mvcGoodResult.getResponse().getContentAsString();
        ReservaDto response = objectMapper.readValue(goodContent, ReservaDto.class);

        assertEquals(esperado, response);

        //error
        when(reservaService.consultarReserva(1L)).thenReturn(null);

        MvcResult mvcBadResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/inventario/reservas/consultar/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        String responseString = mvcBadResult.getResponse().getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(responseString);

        String actualMessage = jsonNode.get("message").asText();
        assertEquals("Can't find reservation", actualMessage);
    }

    @Test
    public void testModificar() throws Exception {

        ExistenciaDto existenciaDto = new ExistenciaDto("A","A",1);
        DetalleResumenReserva detalleesperado = new DetalleResumenReserva(existenciaDto,1);
        ReservaResumenResponse esperado = new ReservaResumenResponse(1L,List.of(detalleesperado));

        when(reservaService.modificarReserva(any(),any())).thenReturn(esperado);

        List<DetalleCrearReservaRequest> detalles =
                List.of(new DetalleCrearReservaRequest("A",1));
        CrearReservaRequest request = new CrearReservaRequest();
        request.setStockReservado(detalles);

        MvcResult mvcGoodResult = mockMvc.perform(MockMvcRequestBuilders
                        .put("/inventario/reservas/modificar/1")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String goodContent = mvcGoodResult.getResponse().getContentAsString();
        ReservaResumenResponse response = objectMapper.readValue(goodContent, ReservaResumenResponse.class);

        assertEquals(esperado, response);

        //error
        when(reservaService.modificarReserva(any(),any())).thenReturn(null);

        MvcResult mvcBadResult = mockMvc.perform(MockMvcRequestBuilders
                        .put("/inventario/reservas/modificar/1")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andReturn();

        String responseString = mvcBadResult.getResponse().getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(responseString);

        String actualMessage = jsonNode.get("message").asText();
        assertEquals("Not being able to connect", actualMessage);
    }

    @Test
    public void testCancelar() throws Exception {

        ExistenciaDto existenciaDto = new ExistenciaDto("A","A",1);
        DetalleResumenReserva detalleesperado = new DetalleResumenReserva(existenciaDto,1);
        ReservaResumenResponse esperado = new ReservaResumenResponse(1L,List.of(detalleesperado));

        when(reservaService.eliminarReserva(any())).thenReturn(esperado);

        MvcResult mvcGoodResult = mockMvc.perform(MockMvcRequestBuilders
                        .delete("/inventario/reservas/cancelar/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String goodContent = mvcGoodResult.getResponse().getContentAsString();
        ReservaResumenResponse response = objectMapper.readValue(goodContent, ReservaResumenResponse.class);

        assertEquals(esperado, response);

        //error
        when(reservaService.eliminarReserva(any())).thenReturn(null);

        MvcResult mvcBadResult = mockMvc.perform(MockMvcRequestBuilders
                        .delete("/inventario/reservas/cancelar/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andReturn();

        String responseString = mvcBadResult.getResponse().getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(responseString);

        String actualMessage = jsonNode.get("message").asText();
        assertEquals("Not being able to connect", actualMessage);
    }


    @Test
    public void testConfirmar() throws Exception {

        ExistenciaDto existenciaDto = new ExistenciaDto("A","A",1);
        DetalleResumenReserva detalleesperado = new DetalleResumenReserva(existenciaDto,1);
        ReservaResumenResponse esperado = new ReservaResumenResponse(1L,List.of(detalleesperado));

        when(reservaService.confirmarReserva(any())).thenReturn(esperado);

        MvcResult mvcGoodResult = mockMvc.perform(MockMvcRequestBuilders
                        .delete("/inventario/reservas/confirmar/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String goodContent = mvcGoodResult.getResponse().getContentAsString();
        ReservaResumenResponse response = objectMapper.readValue(goodContent, ReservaResumenResponse.class);

        assertEquals(esperado, response);


        //error
        when(reservaService.confirmarReserva(any())).thenReturn(null);

        MvcResult mvcBadResult = mockMvc.perform(MockMvcRequestBuilders
                        .delete("/inventario/reservas/confirmar/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andReturn();

        String responseString = mvcBadResult.getResponse().getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(responseString);

        String actualMessage = jsonNode.get("message").asText();
        assertEquals("Not being able to connect", actualMessage);
    }
}
