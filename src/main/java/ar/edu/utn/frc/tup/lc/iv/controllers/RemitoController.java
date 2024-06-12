package ar.edu.utn.frc.tup.lc.iv.controllers;

import ar.edu.utn.frc.tup.lc.iv.dtos.common.ErrorApi;
import ar.edu.utn.frc.tup.lc.iv.dtos.request.CrearRemitoRequest;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.RemitoDto;
import ar.edu.utn.frc.tup.lc.iv.services.RemitoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador que maneja las solicitudes relacionadas con remitos.
 */
@RestController
@RequestMapping("/inventario/remitos")
public class RemitoController {

    /**
     * Instancia para acceder a la lógica de negocio para el manejo de remitos.
     */
    @Autowired
    private RemitoService remitoService;

    /**
     * Obtiene una lista de remitos confirmados.
     *
     * @return Una lista de remitos.
     */
    @Operation(
            summary = "Search for Shipments",
            description = "Return confirmed Remitos"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful operation",
                    content = @Content(schema =
                    @Schema(implementation = RemitoDto.class))),
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(schema =
                    @Schema(implementation = ErrorApi.class)))
    }
    )
    @GetMapping("/listar")
    public List<RemitoDto> getRemitos() {
        return remitoService.listarRemitos();
    }

    /**
     * Crea un nuevo remito y lo almacena en la base de datos.
     *
     * @param remito La solicitud para crear el remito.
     * @return El remito creado.
     */
    @Operation(
            summary = "Create a shipment",
            description = "Return saved shipment"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful operation",
                    content = @Content(schema =
                    @Schema(implementation = RemitoDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "There is already a recipe "
                            + "with that id for that supplier",
                    content = @Content(schema =
                    @Schema(implementation = ErrorApi.class))),
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(schema =
                    @Schema(implementation = ErrorApi.class)))
    }
    )
    @PostMapping("/crear")
    public RemitoDto postRemito(final
            @RequestBody CrearRemitoRequest remito) {
        return remitoService.crearRemito(remito);
    }

    /**
     * Modifica un remito existente con la información proporcionada.
     *
     * @param id     El ID del remito a modificar.
     * @param remito El remito modificado.
     * @return El remito modificado.
     */
    @Operation(
            summary = "Modify a shipment",
            description = "Return modified shipment"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful operation",
                    content = @Content(schema =
                    @Schema(implementation = RemitoDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "There is already a recipe "
                            + "with that id for that supplier",
                    content = @Content(schema =
                    @Schema(implementation = ErrorApi.class))),
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(schema =
                    @Schema(implementation = ErrorApi.class)))
    }
    )
    @PutMapping("/modificar/{id}")
    public RemitoDto putRemito(
            final @PathVariable Long id,
            final @RequestBody CrearRemitoRequest remito) {
        return remitoService.modificarRemito(remito, id);
    }
}
