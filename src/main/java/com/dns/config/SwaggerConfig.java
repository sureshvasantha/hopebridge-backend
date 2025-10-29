package com.dns.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
	@Bean
	OpenAPI purrfectFoodsAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("HopeBridge")
						.description("Online Donation Platforms APIs")
						.version("1.0"))
				.addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
				.components(new io.swagger.v3.oas.models.Components()
						.addSecuritySchemes("Bearer Authentication",
								new SecurityScheme()
										.name("Authorization")
										.type(SecurityScheme.Type.HTTP)
										.scheme("bearer")
										.bearerFormat("JWT")
										.description("Enter JWT token in the format: Bearer <token>")));
	}
}
