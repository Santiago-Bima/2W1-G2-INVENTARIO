package ar.edu.utn.frc.tup.lc.iv.clients;

import ar.edu.utn.frc.tup.lc.iv.dtos.response.ProductoCatalogoDto;
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
 * Rest client que interactura con la api de catalogo.
 */
@Service
@Slf4j
@Getter
public class CatalogoClient {
    /**
     * restTemplate.
     */
    @Autowired
    private RestTemplate restTemplate;
    /**
     * counter.
     */
    private final Integer counter = 0;
    /**
     * RESILIENCE4J_INSTANCE_NAME.
     */
    private static final String RESILIENCE4J_INSTANCE_NAME
            = "microCircuitBreakerCatalogo";
    /**
     * FALLBACK_METHOD.
     */
    private static final String FALLBACK_METHOD_ALL = "fallbackGetAllCatalogo";
    /**
     * FALLBACK_METHOD.
     */
    private static final String FALLBACK_METHOD_GET = "fallbackGetCatalogo";
    /**
     * baseResourceUrl.
     */
    @Value("${direccion.catalogo}")
    private String baseResourceUrl = "";


    /**
     * getAll.
     *
     * @return ResponseEntity
     */
    @CircuitBreaker(name = RESILIENCE4J_INSTANCE_NAME,
            fallbackMethod = FALLBACK_METHOD_ALL)
    public ResponseEntity<ProductoCatalogoDto[]> getAll() {
        String url = baseResourceUrl + "/products";
        log.info("Called to baseResourceUrl: " + url);
        return restTemplate.getForEntity(url,
                ProductoCatalogoDto[].class
        );
    }


    /**
     * fallbackGetAllCatalogo.
     *
     * @param throwable error que devuelve al fallar la peticion.
     * @return ResponseEntity
     */
    public ResponseEntity<ProductoCatalogoDto[]> fallbackGetAllCatalogo(
            final Throwable throwable) {
        log.info("Execution N° " + counter + " - FallBack "
                + "- Exception Message: "
                + throwable.getMessage());
        ProductoCatalogoDto[] r = {};
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(r);
    }


    /**
     * getAll.
     *
     * @param codigo codigo de la existencia.
     * @return ResponseEntity
     */
    @CircuitBreaker(name = RESILIENCE4J_INSTANCE_NAME,
            fallbackMethod = FALLBACK_METHOD_GET)
    public ResponseEntity<ProductoCatalogoDto> get(final String codigo) {
        String url = baseResourceUrl + "/products" + "/" + codigo;
        log.info("Called to baseResourceUrl: " + url);
        ResponseEntity<ProductoCatalogoDto> response =
                restTemplate.getForEntity(url,
                ProductoCatalogoDto.class
        );
        return response;
    }
    /**
     * fallbackGetCatalogo.
     *
     * @param throwable error del fallback
     * @return ResponseEntity
     */
    public ResponseEntity<ProductoCatalogoDto> fallbackGetCatalogo(
            final Throwable throwable) {
        log.info("Execution N° " + counter + " - FallBack "
                + "- Exception Message: "
                + throwable.getMessage());
        ProductoCatalogoDto r = new ProductoCatalogoDto();
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(r);
    }
}
