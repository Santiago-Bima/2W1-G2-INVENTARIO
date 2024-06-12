package ar.edu.utn.frc.tup.lc.iv.controllers;

import ar.edu.utn.frc.tup.lc.iv.dtos.common.ErrorApi;
import ar.edu.utn.frc.tup.lc.iv.dtos.request.CrearSeccionRequest;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.SeccionResponse;
import ar.edu.utn.frc.tup.lc.iv.services.SeccionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("inventario/secciones")
public class SeccionController {
    /**
     * seccionService.
     */
    @Autowired
    private SeccionService seccionService;

    /**
     * crearSeccion.
     * @param seccionRequest
     * @return SeccionResponse
     * @throws Exception
     */
    @Operation(
            summary = "Create a new section",
            description = "Return the section created with your id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful operation",
                    content = @Content(schema =
                    @Schema(implementation = SeccionResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "Section with that id already exists",
                    content = @Content(schema =
                    @Schema(implementation = ErrorApi.class))),
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(schema =
                    @Schema(implementation = ErrorApi.class)))
    }
    )
    @PostMapping("/crear/")
    public SeccionResponse crearSeccion(@RequestBody @Valid final
                     CrearSeccionRequest seccionRequest) throws Exception {
        SeccionResponse seccionSaved =
                seccionService.crearSeccion(seccionRequest);
        if (Objects.isNull(seccionSaved)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Section with zone id "
                            + seccionRequest.getIdZonaAlmacenamiento()
                            + " and name " + seccionRequest.getNombre()
                            + " already exists");
        }

        return seccionSaved;
    }


    /**
     * getSeccionesByZona.
     * @param nombreZona
     * @return List<SeccionResponse>
     */
    @GetMapping("/listar/{nombreZona}")
    public List<SeccionResponse> getSeccionesByZona(@PathVariable
                                                    final String nombreZona) {
      return seccionService.getSeccionesByZona(nombreZona);
    }

    /**
     * deleteSeccion.
     * @param id
     * @return ResponseEntity
     * @throws Exception
     */
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity deleteSeccion(@PathVariable
                                            final Long id) throws Exception {
        return seccionService.deleteSeccion(id);
    }

    /**
     * updateSeccion.
     * @param seccion
     * @param id
     * @return SeccionResponse
     */
    @PutMapping("/editar/{id}")
    SeccionResponse updateSeccion(
            @RequestBody final CrearSeccionRequest seccion,
            @PathVariable final Long id) {
        return seccionService.updateSeccion(seccion, id);
    }

}
