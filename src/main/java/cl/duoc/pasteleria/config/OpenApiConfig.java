package cl.duoc.pasteleria.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    // boton authorize de swagger
    @Bean
    OpenAPI openApi() {
        return new OpenAPI()
                .info(new Info().title("Pasteleria 1000 Sabores API").version("v1"))
                .addSecurityItem(new SecurityRequirement().addList("token"))
                .components(new Components().addSecuritySchemes("token",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }
}
