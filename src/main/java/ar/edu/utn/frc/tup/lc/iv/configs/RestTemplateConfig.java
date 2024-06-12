package ar.edu.utn.frc.tup.lc.iv.configs;

import java.time.Duration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuración para crear una instancia de RestTemplate en la aplicación.
 */
@Configuration
public class RestTemplateConfig {
    /**
     * TIME_OUT.
     */
    private static final int TIME_OUT = 1000;
    // expressed in milliseconds 1000 = 1 sec.
    /**
     * Crea una instancia de RestTemplate.
     *
     * @param builder constructor del restTemplate
     * @return La instancia de RestTemplate.
     */
    @Bean
    public RestTemplate restTemplate(
            final RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofMillis(TIME_OUT))
                .setReadTimeout(Duration.ofMillis(TIME_OUT))
                .build();
    }
}
