package ca.mcgill.ecse321.sportCenterRegistration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI sportCenterOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Sport Center Registration API")
                        .description("Sport Center Registration and Management system")
                        .version("v1.0.0")
                        .license(new License().name("MIT License").url("https://opensource.org/licenses/MIT")))
                .externalDocs(new ExternalDocumentation()
                        .description("Sport Center Registration Documentation")
                        .url("https://github.com/yourusername/sport-center-registration"));
    }
}