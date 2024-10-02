package com.example.sms.infrastructure._configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfiguration {
    @Bean
    public OpenAPI springDocOpenApi() {
        return new OpenAPI()
                .info(new Info().title("HCOSS REST API").version("1.0.0"));
    }
}
