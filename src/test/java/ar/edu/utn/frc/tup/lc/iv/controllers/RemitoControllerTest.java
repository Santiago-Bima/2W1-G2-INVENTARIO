package ar.edu.utn.frc.tup.lc.iv.controllers;

import ar.edu.utn.frc.tup.lc.iv.dtos.request.CrearDetalleRemitoRequest;
import ar.edu.utn.frc.tup.lc.iv.dtos.request.CrearRemitoRequest;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.DetalleRemitoDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.RemitoDto;
import ar.edu.utn.frc.tup.lc.iv.services.RemitoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RemitoController.class)
public class RemitoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RemitoService remitoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Tag("listRemitos")
    public void getRemitosTest() throws Exception {
        List<RemitoDto> remitos = List.of(
                new RemitoDto(
                        4L,
                        1L,
                        12345,
                        "Some description",
                        LocalDateTime.parse("2023-10-26T01:13:32.512"),
                        "Some supplier",
                        List.of(
                                new DetalleRemitoDto(7L, 5.0, "Sample Product",
                                        "Sample detail description")
                        )
                ),
                new RemitoDto(
                        3L,
                        2L,
                        12345,
                        "Some description",
                        LocalDateTime.parse("2023-10-26T01:13:32.512"),
                        "Some supplier",
                        List.of(
                                new DetalleRemitoDto(7L, 5.0, "Sample Product",
                                        "Sample detail description")
                        )
                )
        );

        when(remitoService.listarRemitos()).thenReturn(remitos);

        MvcResult mvcResult = this.mockMvc
                .perform(get("/inventario/remitos/listar"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();


        List<RemitoDto> result =
                List.of(objectMapper.readValue(mvcResult.getResponse().getContentAsString(), RemitoDto[].class));

        assertEquals(2, result.size());
        assertEquals(4L, result.get(0).getId());
        assertEquals(3L, result.get(1).getId());
    }

    @Test
    @Tag("postRemitoSuccessful")
    void testPostRemito() throws Exception {
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

        RemitoDto remitoDto = new RemitoDto(
                4L,
                1L,
                12345,
                "Some description",
                LocalDateTime.parse("2023-10-26T01:13:32.512"),
                "Some supplier",
                List.of(
                        new DetalleRemitoDto(7L, 5.0, "Sample Product",
                                "Sample detail description")
                )
        );

        when(remitoService.crearRemito(crearRemitoRequest)).thenReturn(remitoDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/inventario/remitos/crear")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(crearRemitoRequest));

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(remitoDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.observations").value(remitoDto.getObservaciones()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Tag("postRemitoBadRequest")
    void testPostRemito1() throws Exception {
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

        when(remitoService.crearRemito(crearRemitoRequest)).thenThrow(new
                ResponseStatusException(HttpStatus.BAD_REQUEST, "Remito with nro: "
                + crearRemitoRequest.getNroRemito()
                + " and supplier " + crearRemitoRequest.getNombreProveedor() + " already exists"));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/inventario/remitos/crear")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(crearRemitoRequest));

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Tag("putRemitoSuccessful")
    void testPutRemito() throws Exception {
        Long remitoId = 1L;
        CrearRemitoRequest modificarRemitoRequest = new CrearRemitoRequest(
                1L,
                LocalDateTime.parse("2023-10-26T01:13:32.512"),
                1,
                "coincide con la orden de compra",
                "nombre 1",
                List.of(
                        new CrearDetalleRemitoRequest(5.0, "Sample Product",
                                "Sample detail description"),
                        new CrearDetalleRemitoRequest(5.0, "Sample Product",
                                "Sample detail description")
                )
        );
        RemitoDto remitoDto = new RemitoDto(
                1L,
                1L,
                1,
                "coincide con la orden de compra",
                LocalDateTime.parse("2023-10-26T01:13:32.512"),
                "nombre 1",
                List.of(
                        new DetalleRemitoDto(1L, 5.0, "Tornillo",
                                "Tornillo")
                )
        );

        when(remitoService.modificarRemito(modificarRemitoRequest, remitoId)).thenReturn(remitoDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/inventario/remitos/modificar/{id}", remitoId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(modificarRemitoRequest));

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(remitoId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.observations")
                        .value(remitoDto.getObservaciones()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details.size()").value(1))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Tag("putRemitoNotFound")
    void testPutRemito1() throws Exception {
        Long remitoId = 1L;
        CrearRemitoRequest modificarRemitoRequest = new CrearRemitoRequest(
                1L,
                LocalDateTime.parse("2023-10-26T01:13:32.512"),
                1,
                "coincide con la orden de compra",
                "nombre 1",
                List.of(
                        new CrearDetalleRemitoRequest(5.0, "Sample Product",
                                "Sample detail description"),
                        new CrearDetalleRemitoRequest(5.0, "Sample Product",
                                "Sample detail description")
                )
        );

        when(remitoService.modificarRemito(modificarRemitoRequest, remitoId)).thenThrow(new
                ResponseStatusException(
                HttpStatus.NOT_FOUND, ""));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/inventario/remitos/modificar/{id}", remitoId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(modificarRemitoRequest));

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
