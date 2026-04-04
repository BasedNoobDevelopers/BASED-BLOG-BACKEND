package com.noobsmoke.basedblogbackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${image.service.base-url}")
    private String imageServiceBaseURL;

    @Bean
    public WebClient.Builder getWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient webClient() {
        return getWebClientBuilder().baseUrl(imageServiceBaseURL).build();
    }
}
