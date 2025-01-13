package com.portal_egressos.portal_egressos_backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.portal_egressos.portal_egressos_backend.config.auth.SecurityFilter;

@Configuration
@EnableWebSecurity
public class AuthConfig {
  @Autowired
  SecurityFilter securityFilter;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/api/auth/signin").permitAll()
            .requestMatchers("/api/oportunidades/salvar").permitAll()
            .requestMatchers("/api/oportunidades/atualizar/{id}").permitAll()
            .requestMatchers("/api/oportunidades/status/{id}").hasRole("COORDENADOR")
            .requestMatchers("/api/oportunidades/aprovadas").permitAll()
            .requestMatchers("/api/oportunidades/pendentes").hasRole("COORDENADOR")
            .requestMatchers("/api/oportunidades/remover/{id}").hasRole("COORDENADOR")
            .requestMatchers("/api/oportunidades/titulo/{titulo}").permitAll()
            .requestMatchers("/api/oportunidades/egresso/{nome}").permitAll()

            )
        .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  @Bean
  AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
      throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}