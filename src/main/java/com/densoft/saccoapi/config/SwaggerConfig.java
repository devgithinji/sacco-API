package com.densoft.saccoapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${springdoc.api-docs.app-version}")
    private String apiVersion;

    @Value("${springdoc.api-docs.title}")
    private String apiTitle;

    @Value("${springdoc.api-docs.description}")
    private String apiDescription;

    @Value("${springdoc.api-docs.contact.name}")
    private String contactName;

    @Value("${springdoc.api-docs.contact.email}")
    private String contactEmail;

    @Value("${springdoc.api-docs.contact.url}")
    private String contactUrl;

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title(apiTitle)
                        .version(apiVersion)
                        .description(apiDescription)
                        .contact(new Contact()
                                .name(contactName)
                                .email(contactEmail)
                                .url(contactUrl)));
    }
}
