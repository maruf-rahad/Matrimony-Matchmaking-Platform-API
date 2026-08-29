package com.eu.matrimonybackend.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customConfig() {
        return new OpenAPI().info(
                new Info()
                        .title("DemoMatrimony Project")
                        .description("All APIs for the DemoMatrimony project")
        );
    }
}
