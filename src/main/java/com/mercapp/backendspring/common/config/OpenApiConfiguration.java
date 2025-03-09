package com.mercapp.backendspring.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setDescription("Backend Spring API");

        Info info = new Info();
        info.setTitle("Backend Spring API");
        info.setDescription("Backend Spring API");
        info.setVersion("1.0");

        return new OpenAPI()
                .servers(List.of(server))
                .info(info);
    }
}
