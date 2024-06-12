package ar.edu.utn.frc.tup.lc.iv.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    /**
     * corsConfigurer.
     *
     * @return registry
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            /**
             * addCorsMappings.
             *
             * @return registry
             */
            @Override
            public void addCorsMappings(final CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*") // Puedes configurar los orígenes
                                            // permitidos según tus necesidades
                        .allowedMethods("GET", "POST", "PUT", "DELETE");
                                    // Puedes configurar los métodos permitidos
            }
        };
    }
}
