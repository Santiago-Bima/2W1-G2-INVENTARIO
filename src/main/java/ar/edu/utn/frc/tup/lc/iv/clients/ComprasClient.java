package ar.edu.utn.frc.tup.lc.iv.clients;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

/**
 * Cliente para realizar solicitudes de compras
 * y gestionar la recuperación de errores.
 */
@Service
@Slf4j
@Getter
public class ComprasClient {

    /**
     * Plantilla para realizar solicitudes HTTP.
     */
    @Autowired
    private RestTemplate restTemplate;

    /**
     * Contador de ejecuciones fallidas para el fallback.
     */
    private Integer counter = 0;

    /**
     * Nombre de la instancia de Resilience4j Circuit Breaker.
     */
    private static final String RESILIENCE4J_INSTANCE_NAME
            = "microCircuitBreakerCompras";

    /**
     * Nombre del método de fallback.
     */
    private static final String FALLBACK_METHOD = "fallback";

    /**
     * URL base del recurso para las solicitudes.
     */
    @Value("${direccion.compras}")
    private String baseResourceUrl = "";

    /**
     * Realiza una solicitud de creación de un remito
     * y maneja la recuperación de errores
     * mediante un circuit breaker.
     *
     * @param remitoId El ID del remito a crear.
     * @return La respuesta que incluye el Id del remito creado.
     */
    @CircuitBreaker(name = RESILIENCE4J_INSTANCE_NAME,
            fallbackMethod = FALLBACK_METHOD)
    public ResponseEntity<Long> postRemitoId(final Long remitoId) {
        String url = baseResourceUrl + "/remitos/remitoAviso";

        log.info("Called to baseResourceUrl: " + url);
        return restTemplate.postForEntity(url,
                remitoId, Long.class);
    }

    /**
     * Método de fallback que se ejecuta en caso de errores en la solicitud.
     *
     * @param throwable La excepción que desencadenó el fallback.
     * @return Una respuesta de error con un valor de ID de remito -1.
     */
    public ResponseEntity<Long> fallback(final
            Throwable throwable) {
        counter++;
        log.warn("Ejecución N° " + counter
                + " - FallBack - Mensaje de excepción: "
                + throwable.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(-1L);
    }
}
