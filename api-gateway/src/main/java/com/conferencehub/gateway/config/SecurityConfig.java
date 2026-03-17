package com.conferencehub.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

  @Bean
  SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, Environment env) {

    String issuerUri = env.getProperty("spring.security.oauth2.resourceserver.jwt.issuer-uri");

    http.csrf(ServerHttpSecurity.CsrfSpec::disable);

    // If no issuer-uri is configured, keep the gateway open to simplify local development.
    if (issuerUri == null || issuerUri.isBlank()) {
      return http.authorizeExchange(ex -> ex.anyExchange().permitAll()).build();
    }

    return http
        .authorizeExchange(
            ex ->
                ex.pathMatchers("/actuator/**").permitAll()
                    .pathMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
                    .permitAll()
                    .anyExchange()
                    .authenticated())
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
        .build();
  }
}

