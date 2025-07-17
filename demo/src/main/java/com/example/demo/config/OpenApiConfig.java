package com.example.demo.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

/**
 * This is a configuration class for OpenAPI (Swagger) documentation.
 * It defines the general information about the API and the security scheme for JWT.
 */
@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Your Name",
                        email = "your.email@provider.com",
                        url = "[https://your-portfolio.com](https://your-portfolio.com)"
                ),
                description = "OpenApi documentation for Telemedicine Platform",
                title = "Telemedicine API - Your Name",
                version = "1.0",
                license = @License(
                        name = "Licence name",
                        url = "[https://some-url.com](https://some-url.com)"
                ),
                termsOfService = "Terms of service"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "PROD ENV",
                        url = "[https://your-production-url.com](https://your-production-url.com)" // Replace with your actual production URL later
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
