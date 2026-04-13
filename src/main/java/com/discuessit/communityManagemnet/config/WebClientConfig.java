package com.discuessit.communityManagemnet.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Configuration
public class WebClientConfig {
    @Value("${user-management.base-url}")
    String baseUrl;
    @Value("${spring.security.user.name}")
    String username;
    @Value("${spring.security.user.password}")
    String password;

    @Bean
    public WebClient userManagementWebClient() {
        String credentials = username + ":" + password; // Don't encode the password!
        String basicAuth = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());

        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, basicAuth)
                .build();
    }



}
