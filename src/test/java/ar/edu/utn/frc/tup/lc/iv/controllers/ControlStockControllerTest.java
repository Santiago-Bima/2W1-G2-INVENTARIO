package ar.edu.utn.frc.tup.lc.iv.controllers;

import ar.edu.utn.frc.tup.lc.iv.dtos.request.CrearControlStockRequest;
import ar.edu.utn.frc.tup.lc.iv.dtos.request.ModificarControlStockRequest;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.ControlStockResponse;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.DetalleEstadisticaResponse;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.EstadisticaResponse;
import ar.edu.utn.frc.tup.lc.iv.services.ControlStockService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(ControlStockController.class)
public class ControlStockControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ControlStockService controlStockService;
    @Autowired
    private ControlStockController controlStockController;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Tag("crearControlStock-NotFound")
    public void crearControlTest() throws Exception {
        CrearControlStockRequest controlStockRequest = new CrearControlStockRequest(
                123L,
                "Descripción del control de stock",
                100.5,
                5.0,
                false,
                LocalDateTime.now()
        );

        when(controlStockService.crearControlStock(controlStockRequest))
                .thenThrow(new ResponseStatusException(
                        HttpStatus.NOT_FOUND));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/inventario/controles-stock/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(controlStockRequest));

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @Test
    @Tag("crearControlStock-succesful")
    public void crearControlTest1() throws Exception {
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

        when(controlStockService.crearControlStock(controlStockRequest))
                .thenReturn(response);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/inventario/controles-stock/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(controlStockRequest));

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L));
    }

    @Test
    @Tag("crearControlStock-NotFound")
    public void modificarControlTest() throws Exception {
        ModificarControlStockRequest controlStockRequest = new ModificarControlStockRequest(
                123L,
                "Descripción del control de stock",
                100.5,
                5.0,
                false
        );

        when(controlStockService.modificarControlStock(1L, controlStockRequest))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/inventario/controles-stock/modificar/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(controlStockRequest));

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Tag("crearControlStock-succesful")
    public void modificarControlTest1() throws Exception {
        ModificarControlStockRequest controlStockRequest = new ModificarControlStockRequest(
                123L,
                "Descripción del control de stock",
                100.5,
                5.0,
                false
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

        when(controlStockService.modificarControlStock(1L, controlStockRequest))
                .thenReturn(response);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/inventario/controles-stock/modificar/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(controlStockRequest));

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L));
    }

    @Test
    @Tag("eliminarControlStock-NotFound")
    public void eliminarControlTest() throws Exception {
        ModificarControlStockRequest controlStockRequest = new ModificarControlStockRequest(
                123L,
                "Descripción del control de stock",
                100.5,
                5.0,
                false
        );

        when(controlStockService.eliminarControlStock(1L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/inventario/controles-stock/eliminar/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(controlStockRequest));

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Tag("eliminarControlStock-Succesfull")
    public void eliminarControlTest1() throws Exception {
        ModificarControlStockRequest controlStockRequest = new ModificarControlStockRequest(
                123L,
                "Descripción del control de stock",
                100.5,
                5.0,
                false
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

        when(controlStockService.eliminarControlStock(1L))
                .thenReturn(response);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/inventario/controles-stock/eliminar/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(controlStockRequest));

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Tag("listarControles")
    public void listarControlesTest() throws Exception {
        ControlStockResponse[] list = {
                new ControlStockResponse(
                        1L,
                        123L,
                        "Descripción 1",
                        50.0,
                        2.0,
                        false,
                        LocalDateTime.now()
                ),
                new ControlStockResponse(
                        2L,
                        456L,
                        "Descripción 2",
                        75.0,
                        5.0,
                        true,
                        LocalDateTime.now()
                )
        };

        when(controlStockService.getControlesDeStock()).thenReturn(list);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/inventario/controles-stock/listar/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(list));

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));
    }

    @Test
    @Tag("listarControlesDelMesActual")
    public void getControlesOfThisMonthTest() throws Exception {
        List<ControlStockResponse> list = List.of(new ControlStockResponse[]{
                new ControlStockResponse(
                        1L,
                        123L,
                        "Descripción 1",
                        50.0,
                        2.0,
                        false,
                        LocalDateTime.now()
                ),
                new ControlStockResponse(
                        2L,
                        456L,
                        "Descripción 2",
                        75.0,
                        5.0,
                        true,
                        LocalDateTime.now()
                )
        });

        when(controlStockService.listarControlesDelUltimoMes()).thenReturn(list);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/inventario/controles-stock//listaMesActual")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(list));

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));
    }

    @Test
    public void testEstadistica()  throws Exception{
        LocalDateTime fecha = LocalDateTime.now();
        LocalDateTime fecha2 = LocalDateTime.now().plusMonths(6);

        DetalleEstadisticaResponse[] detalles = {
                new DetalleEstadisticaResponse("A",5,5, BigDecimal.valueOf(100))
        };
        EstadisticaResponse result = new EstadisticaResponse(fecha,fecha2,detalles);
        when(controlStockService.getEstadisticas("vencidas")).thenReturn(result);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/inventario/controles-stock/estadisticas?tipo=vencidas")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
