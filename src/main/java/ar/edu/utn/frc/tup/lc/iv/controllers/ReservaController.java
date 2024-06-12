package ar.edu.utn.frc.tup.lc.iv.controllers;

import static java.util.Objects.isNull;

import ar.edu.utn.frc.tup.lc.iv.dtos.common.ErrorApi;
import ar.edu.utn.frc.tup.lc.iv.dtos.request.CrearReservaRequest;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.ReservaDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.ReservaResumenResponse;
import ar.edu.utn.frc.tup.lc.iv.services.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * controlador de reservas.
 */
@RestController
@RequestMapping("inventario/reservas")
public class ReservaController {
    /**
     * reservaService.
     */
    @Autowired
    private ReservaService reservaService;

    /**
     * crearReserva.
     *
     * @param reservaRequest datos de la reserva a crear
     * @return ReservaResumenResponse
     */
    @Operation(
            summary = "Search for unconfirmed reservations",
            description = "Return unconfirmed reservations"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful operation",
                    content = @Content(schema =
                    @Schema(implementation = ReservaResumenResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "Bad request",
                    content = @Content(schema =
                    @Schema(implementation = ErrorApi.class))),
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(schema =
                    @Schema(implementation = ErrorApi.class)))
    }
    )
    @PostMapping("/crear")
    public ReservaResumenResponse crearReserva(final
            @RequestBody CrearReservaRequest reservaRequest) {
        ReservaResumenResponse response =
                reservaService.crearReserva(reservaRequest);
        if (isNull(response)) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Not being able to connect");
        }
        return response;
    }

    /**
     * listarReservas.
     * @return List<ReservaResumenResponse>
     */
    @Operation(
            summary = "Search for unconfirmed reservations",
            description = "Return unconfirmed reservations"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful operation",
                    content = @Content(schema =
                    @Schema(implementation = ReservaResumenResponse.class))),
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(schema =
                    @Schema(implementation = ErrorApi.class)))
    }
    )
    @GetMapping("/listar/no-confirmadas")
    public List<ReservaResumenResponse> listarReservas() {
        List<ReservaResumenResponse> response = reservaService.listarReservas();
        if (isNull(response)) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Not being able to connect");
        }
        return response;
    }


    /**
     * consultarReserva.
     *
     * @param id id de la reserva a consultar
     * @return ReservaDto
     */
    @Operation(
            summary = "Search for unconfirmed reservations",
            description = "Return unconfirmed reservations"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful operation",
                    content = @Content(schema =
                    @Schema(implementation = ReservaDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Entity not found error",
                    content = @Content(schema =
                    @Schema(implementation = ErrorApi.class))),
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(schema =
                    @Schema(implementation = ErrorApi.class)))
    }
    )
    @GetMapping("/consultar/{id}")
    public ReservaDto consultarReserva(final @PathVariable Long id) {
        ReservaDto response = reservaService.consultarReserva(id);
        if (isNull(response)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Can't find reservation");
        }
        return response;
    }

    /**
     * modificarReserva.
     *
     * @param id id de la reserva a modificar
     * @param reservaRequest datos de la reserva a modificar
     * @return ReservaDto
     */
    @Operation(
            summary = "Modify a reservation",
            description = "Return modified reservation"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful operation",
                    content = @Content(schema =
                    @Schema(implementation = ReservaResumenResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "Bad request",
                    content = @Content(schema =
                    @Schema(implementation = ErrorApi.class))),
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(schema =
                    @Schema(implementation = ErrorApi.class)))
    }
    )
    @PutMapping("/modificar/{id}")
    public ReservaResumenResponse modificarReserva(final
            @RequestBody CrearReservaRequest reservaRequest,
            final @PathVariable Long id) {
        ReservaResumenResponse response =
                reservaService.modificarReserva(reservaRequest, id);
        if (isNull(response)) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Not being able to connect");
        }
        return response;
    }

    /**
     * cancelarReserva.
     *
     * @param id id de la reserva a cancelar
     * @return ReservaDto
     */
    @Operation(
            summary = "Delete a reservation",
            description = "Return deleted reservation"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful operation",
                    content = @Content(schema =
                    @Schema(implementation = ReservaResumenResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "Bad request",
                    content = @Content(schema =
                    @Schema(implementation = ErrorApi.class))),
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(schema =
                    @Schema(implementation = ErrorApi.class)))
    }
    )
    @DeleteMapping("/cancelar/{id}")
    public ReservaResumenResponse cancelarReserva(final
            @PathVariable Long id) {
        ReservaResumenResponse response = reservaService.eliminarReserva(id);
        if (isNull(response)) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Not being able to connect");
        }
        return response;
    }

    /**
     * confirmarReserva.
     *
     * @param id id de la reserva a confirmar
     * @return ReservaDto
     */
    @Operation(
            summary = "Confirm a reservation",
            description = "Return confirmed reservation"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful operation",
                    content = @Content(schema =
                    @Schema(implementation = ReservaResumenResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "Bad request",
                    content = @Content(schema =
                    @Schema(implementation = ErrorApi.class))),
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(schema =
                    @Schema(implementation = ErrorApi.class)))
    }
    )
    @DeleteMapping("/confirmar/{id}")
    public ReservaResumenResponse confirmarReserva(final
            @PathVariable Long id) {
        ReservaResumenResponse response = reservaService.confirmarReserva(id);
        if (isNull(response)) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Not being able to connect");
        }
        return response;
    }
}
