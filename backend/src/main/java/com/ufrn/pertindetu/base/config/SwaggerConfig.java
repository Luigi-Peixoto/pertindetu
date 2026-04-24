package com.ufrn.pertindetu.base.config;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;



/**
 * Configuration class for Swagger/OpenAPI documentation.
 * This class sets up the OpenAPI specification for the Pertindetu API, including security schemes for JWT bearer authentication.
 */
@Configuration
public class SwaggerConfig {

    @Value("${system.name}")
    private String systemName;

    @Value("${system.version}")
    private String systemVersion;

    @Value("${base.api.domain}")
    private String apiDomain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    private static final String API_DESCRIPTION = "REST API for quick and secure operations. Interactive documentation with details of available endpoints.";

    private SecurityScheme createBearerScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", createBearerScheme())
                        .addResponses("defaultError", createDefaultErrorResponse()))
                .info(new Info()
                        .title(systemName)
                        .version(systemVersion)
                        .description(API_DESCRIPTION))
                .servers(Collections.singletonList(
                        new Server().url(apiDomain + contextPath)
                ));
    }

    private ApiResponse createDefaultErrorResponse() {
        Map<String, Object> example = new HashMap<>();
        example.put("data", null);
        example.put("message", "Operation failed");
        example.put("error", Map.of(
                "status", "404 NOT_FOUND",
                "timestamp", "2025-01-22T22:31:16.123Z"
        ));

        return new ApiResponse()
                .description("Error response")
                .content(new Content()
                        .addMediaType("application/json",
                                new MediaType().example(example)));
    }

    @Bean
    public OpenApiCustomizer openApiCustomizer() {
        return openApi -> {
            openApi.getPaths().values().forEach(pathItem ->
                    pathItem.readOperations().forEach(operation -> {
                        if (operation.getResponses() != null) {
                            // Aplicar apenas respostas de erro padrão, deixando que cada endpoint
                            // defina suas próprias respostas de sucesso específicas
                            if (!operation.getResponses().containsKey("404")) {
                                operation.getResponses().addApiResponse("404",
                                        openApi.getComponents().getResponses().get("defaultError"));
                            }
                        }
                    })
            );
        };
    }
}

