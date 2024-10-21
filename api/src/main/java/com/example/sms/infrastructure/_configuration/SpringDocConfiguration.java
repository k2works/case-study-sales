package com.example.sms.infrastructure._configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.servers.ServerVariable;
import io.swagger.v3.oas.models.servers.ServerVariables;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SpringDocConfiguration {
    String TITLE = "SMS REST API";
    String VERSION = "1.0.0";

    @Bean
    public OpenAPI springDocOpenApi() {
        ServerVariables serverVariables = new ServerVariables();
        ServerVariable variable = new ServerVariable();
        variable._default("localhost:8080");
        variable.description("Base server url");
        serverVariables.addServerVariable("serverUrl", variable);

        List<Server> servers = new ArrayList<>();
        servers.add(new Server().url("http://{serverUrl}").variables(serverVariables).description("HTTP server"));
        servers.add(new Server().url("https://{serverUrl}").variables(serverVariables).description("HTTPS server"));

        return new OpenAPI().servers(servers).info(new Info().title(TITLE).version(VERSION));
    }
}
