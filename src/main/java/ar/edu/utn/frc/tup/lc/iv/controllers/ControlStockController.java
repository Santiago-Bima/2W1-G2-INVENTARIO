package ar.edu.utn.frc.tup.lc.iv.controllers;

import ar.edu.utn.frc.tup.lc.iv.dtos.common.ErrorApi;
import ar.edu.utn.frc.tup.lc.iv.dtos.request.CrearControlStockRequest;
import ar.edu.utn.frc.tup.lc.iv.dtos.request.ModificarControlStockRequest;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.ControlStockResponse;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.EstadisticaResponse;
import ar.edu.utn.frc.tup.lc.iv.services.ControlStockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador que maneja las solicitudes
 * relacionadas con el control de stock de inventario.
 */
@RestController
@RequestMapping("/inventario/controles-stock")
public class ControlStockController {

    /**
     * ControlSotckService.
     */
    @Autowired
    private ControlStockService controlStockService;

    /**
     * Crea un informe de control de stock.
     *
     * @param request La solicitud para crear un informe de control de stock.
     * @return El informe de control de stock creado.
     */
    @Operation(
            summary = "Create a stock control report",
            description = "Return created stock control report"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful operation",
                    content = @Content(schema =
                    @Schema(implementation = ControlStockResponse.class))),
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
    public ControlStockResponse crearControlStock(final
            @RequestBody CrearControlStockRequest request) {
        return controlStockService.crearControlStock(request);
    }

    /**
     * Modifica un informe de control de stock existente.
     *
     * @param id   El ID del informe de control de stock a modificar.
     * @param body La solicitud para modificar el informe de control de stock.
     * @return El informe de control de stock modificado.
     */
    @Operation(
            summary = "Modify a stock control report",
            description = "Return modified stock control report"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful operation",
                    content = @Content(schema =
                    @Schema(implementation = ControlStockResponse.class))),
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
    public ControlStockResponse modificarControlStock(
            final @PathVariable Long id,
            final @RequestBody ModificarControlStockRequest body) {
        return controlStockService.modificarControlStock(id, body);
    }

    /**
     * Elimina un informe de control de stock existente.
     *
     * @param id El ID del informe de control de stock a eliminar.
     * @return El informe de control de stock eliminado.
     */
    @Operation(
            summary = "Delete a stock control report",
            description = "Return deleted stock control report"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful operation",
                    content = @Content(schema =
                    @Schema(implementation = ControlStockResponse.class))),
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
    @DeleteMapping("/eliminar/{id}")
    public ControlStockResponse eliminarControlStock(
            final @PathVariable Long id) {
        return controlStockService.eliminarControlStock(id);
    }

    /**
     * Consulta todas las instancias de
     * control de stock y retorna los informes guardados.
     *
     * @return Un array de respuestas de control de stock.
     */
    @Operation(
            summary = "Consult all instances of stock control",
            description = "Return all the saved instances of stock control"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                 description = "Successful operation",
                 content = @Content(schema =
                 @Schema(implementation = ControlStockResponse.class))),
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(schema =
                    @Schema(implementation = ErrorApi.class)))

    })
    @GetMapping("/listar/")
    public ControlStockResponse[] getControles() {
        return controlStockService.getControlesDeStock();
    }

    /**
     * Consulta las instancias de
     * control de stock del mes actual y retorna los informes guardados.
     *
     * @return Una lista de respuestas de control de stock del mes actual.
     */
    @Operation(
            summary = "Consult this month instances of stock control",
            description = "Return all the saved "
                    + "instances of stock control of the current month"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful operation",
                    content = @Content(schema =
                    @Schema(implementation = ControlStockResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "There are no stock controls on this month",
                    content = @Content(schema =
                    @Schema(implementation = ErrorApi.class))),
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(schema =
                    @Schema(implementation = ErrorApi.class)))

    })
    @GetMapping("/listaMesActual")
    public List<ControlStockResponse> getControlesOfThisMonth() {
        return controlStockService.listarControlesDelUltimoMes();
    }

    /**
     * Entrega un informe de estadisticas vencidas o dañadas
     * (para que sean vencidas tienen que poner ?tipo=vencida).
     *
     * @param tipo tipo de la estadistica a obtener
     * @return Una lista de respuestas de control de stock del mes actual.
     */
    @Operation(
            summary = "Consult stadistics of controls",
            description = "Return stadistics of controls"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful operation",
                    content = @Content(schema
                       = @Schema(implementation = ControlStockResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "There are no stock controls on this month",
                    content = @Content(schema
                            = @Schema(implementation = ErrorApi.class))),
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(schema
                            = @Schema(implementation = ErrorApi.class)))

    })
    @GetMapping("/estadisticas")
    public EstadisticaResponse getEstadisticasControles(final
                               @RequestParam String tipo) {
        return controlStockService.getEstadisticas(tipo);
    }
}
