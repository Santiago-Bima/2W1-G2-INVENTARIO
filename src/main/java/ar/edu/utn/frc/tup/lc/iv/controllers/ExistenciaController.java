package ar.edu.utn.frc.tup.lc.iv.controllers;

import static java.util.Objects.isNull;

import ar.edu.utn.frc.tup.lc.iv.dtos.common.ErrorApi;
import ar.edu.utn.frc.tup.lc.iv.dtos.request.CrearExistenciaRequestDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.request.ModificarExistenciaRequestDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.ExistenciaBajaDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.ExistenciaDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.ExistenciaTotalResponse;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.ProductoCatalogoDto;
import ar.edu.utn.frc.tup.lc.iv.services.ExistenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
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


@RestController
@RequestMapping("inventario/existencias")
public class ExistenciaController {
    /**
     * existenciaService.
     */
    @Autowired
    private ExistenciaService existenciaService;

    /**
     * verCatalogo.
     *
     * @return ProductoCatalogoDto[]
     */
    @Operation(
            summary = "Ressearch for products of catalog",
            description = "Return products of catalog"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful operation",
                    content = @Content(schema
                    = @Schema(implementation = ProductoCatalogoDto.class))),
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(schema
                            = @Schema(implementation = ErrorApi.class)))
    }
    )
    @GetMapping("/consultarCatalogo/")
    public ProductoCatalogoDto[] verCatalogo() {
        ProductoCatalogoDto[] saved = existenciaService.consultarCatalogo();
        if (isNull(saved)) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Not being able to connect");
        }
        return saved;
    }

    /**
     * crear.
     *
     * @param requestDto datos de la existencia a crear
     * @return ExistenciaDto
     */
    @Operation(
            summary = "Create a new existance",
            description = "Return the existance created with your id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful operation",
                    content = @Content(schema
                            = @Schema(implementation = ExistenciaDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "Catalog product not exist or alredy used",
                    content = @Content(schema
                            = @Schema(implementation = ErrorApi.class))),
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(schema
                            = @Schema(implementation = ErrorApi.class)))
    }
    )
    @PostMapping("/crear/")
    public ExistenciaDto crear(final @RequestBody @Valid
                                    CrearExistenciaRequestDto requestDto)
                                    throws Exception {
        ExistenciaDto saved = existenciaService.crearExistencia(requestDto);
        if (isNull(saved)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The catalog product its alredy used");
        }
        return saved;
    }

    /**
     * modificar.
     *
     * @param codigo codigo de la existencia a modificar
     * @param requestDto datos de la existencia a modificar
     * @return ExistenciaDto
     */
    @Operation(
            summary = "Modify an existance",
            description = "Return the existance modified with your code"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful operation",
                    content = @Content(schema
                            = @Schema(implementation = ExistenciaDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "The existance doesn't exist",
                    content = @Content(schema
                            = @Schema(implementation = ErrorApi.class))),
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(schema
                            = @Schema(implementation = ErrorApi.class)))
    }
    )
    @PutMapping("/modificar/{codigo}")
    public ExistenciaDto modificar(final @PathVariable String codigo,
                                   @RequestBody
                                   @Valid
                                   final
                                   ModificarExistenciaRequestDto requestDto) {
        ExistenciaDto saved =
                existenciaService.modificarExistencia(codigo, requestDto);
        if (isNull(saved)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The existance doesn't exist");
        }

        return saved;
    }

    /**
     * eliminar.
     *
     * @param codigo codigo de la existencia a eliminar
     * @return ExistenciaDto
     */
    @Operation(
            summary = "Delete an existance",
            description = "Return the existance deleted with your id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful operation",
                    content = @Content(schema
                            = @Schema(implementation = ExistenciaDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "The existance doesn't exist",
                    content = @Content(schema
                            = @Schema(implementation = ErrorApi.class))),
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(schema
                            = @Schema(implementation = ErrorApi.class)))
    }
    )
    @DeleteMapping("/eliminar/{codigo}")
    public ExistenciaDto eliminar(@PathVariable final String codigo) {
        ExistenciaDto saved = existenciaService.eliminarExistencia(codigo);
        if (isNull(saved)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The existance doesn't exist");
        }

        return saved;
    }

    /**
     * getExistenciasBajas.
     * @return List<ExistenciaBajaDto>
     */
    @Operation(
            summary = "Consult existances",
            description = "Return all the existances with lowe stock"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful operation",
                    content = @Content(schema
                            = @Schema(implementation = ExistenciaDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "There are no existances with lower stocks",
                    content = @Content(schema
                            = @Schema(implementation = ErrorApi.class))),
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(schema
                            = @Schema(implementation = ErrorApi.class)))
    }
    )
    @GetMapping("/listar/bajoStock/")
    public List<ExistenciaBajaDto> getExistenciasBajas() {
        List<ExistenciaBajaDto> existencias =
                existenciaService.getExistenciasBajas();
        if (isNull(existencias)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "There are no existances with low stock");
        }

        return existencias;
    }
    @GetMapping("/total/{codigo}")
    private ExistenciaTotalResponse getTotalExistencia(@PathVariable
                                    final String codigo) {
        return existenciaService.getTotalExistencia(codigo);
    }
    @GetMapping("/total/")
    private List<ExistenciaTotalResponse> getTotalExistencias() {
        return existenciaService.getTotalExistencias();
    }
}
