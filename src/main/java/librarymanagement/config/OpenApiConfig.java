package librarymanagement.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Library Management API",
                version = "v1",
                description = "REST API for managing books, authors, and members.",
                contact = @Contact(name = "Library Management Team"),
                license = @License(name = "Internal use only")
        )
)
public class OpenApiConfig {
}
