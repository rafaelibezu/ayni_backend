package com.ayni.backend.config;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiGroupsConfig {

    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                .group("01-auth-publico")
                .pathsToMatch(
                        "/api/auth/**",
                        "/api/health",
                        "/api/newsletter/**",
                        "/api/admin/**"
                )
                .addOpenApiCustomizer(openApi ->
                        openApi.info(new Info()
                                .title("Ayni Auth y Público API")
                                .description("Endpoints públicos: login, registro, health check y newsletter.")
                                .version("1.0.0"))
                )
                .build();
    }

    @Bean
    public GroupedOpenApi clienteApi() {
        return GroupedOpenApi.builder()
                .group("02-cliente-user")
                .pathsToMatch(
                        "/api/users/**",
                        "/api/categories/**",
                        "/api/products/**",
                        "/api/favorites/**",
                        "/api/cart/**",
                        "/api/orders/**"
                )
                .addOpenApiCustomizer(openApi ->
                        openApi.info(new Info()
                                .title("Ayni Cliente API")
                                .description("Endpoints para cliente autenticado y navegación de tienda.")
                                .version("1.0.0"))
                )
                .build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("03-admin")
                .pathsToMatch(
                        "/api/admin/**"
                )
                .addOpenApiCustomizer(openApi ->
                        openApi.info(new Info()
                                .title("Ayni Admin API")
                                .description("Endpoints administrativos para productos, categorías, pedidos y pagos.")
                                .version("1.0.0"))
                )
                .build();
    }
}