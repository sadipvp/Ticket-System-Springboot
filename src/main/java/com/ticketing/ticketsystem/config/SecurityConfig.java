package com.ticketing.ticketsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity // 👈 1. Importante para anular la seguridad por defecto
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 2. Deshabilitar CSRF (necesario para probar POST/PUT desde Postman o tu
                // frontend sin tokens)
                .csrf(AbstractHttpConfigurer::disable)

                // 3. Configurar rutas públicas
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll() // Permite acceso a TODO (archivos estáticos, API, etc.)
                        .anyRequest().permitAll());

        return http.build();
    }
}