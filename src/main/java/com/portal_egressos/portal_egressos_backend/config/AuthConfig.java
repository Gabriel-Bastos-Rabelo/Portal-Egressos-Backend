package com.portal_egressos.portal_egressos_backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
            .requestMatchers(HttpMethod.GET, "/api/oportunidade/aprovadas").permitAll() // Público
            .requestMatchers(HttpMethod.GET, "/api/oportunidade/listar").hasRole("COORDENADOR") //listar
            .requestMatchers(HttpMethod.POST, "/api/oportunidade/salvar").hasAnyRole("COORDENADOR", "EGRESSO") // Criar
            .requestMatchers(HttpMethod.PUT, "/api/oportunidade/atualizar/{id}").hasRole("COORDENADOR") // Atualizar
            .requestMatchers(HttpMethod.PUT, "/api/oportunidade/status/{id}").hasRole("COORDENADOR") // Atualizar status
            .requestMatchers(HttpMethod.DELETE, "/api/oportunidade/remover/{id}").hasRole("COORDENADOR") // Deletar
            .requestMatchers(HttpMethod.POST, "/api/depoimento/salvar").hasAnyRole("COORDENADOR", "EGRESSO") // Criar
            .requestMatchers(HttpMethod.PUT, "/api/depoimento/atualizar/{id}").hasAnyRole("COORDENADOR", "EGRESSO") // Atualizar
            .requestMatchers(HttpMethod.PUT, "/api/depoimento/status/{id}").hasAnyRole("COORDENADOR") // Atualizar status
            .requestMatchers(HttpMethod.GET, "/api/depoimento/listar").hasAnyRole("COORDENADOR") // listar
            .requestMatchers(HttpMethod.GET, "/api/depoimento/aprovados").permitAll()
            .requestMatchers(HttpMethod.DELETE, "/api/depoimento/remover/{id}").hasAnyRole("COORDENADOR", "EGRESSO") // remover
            .requestMatchers(HttpMethod.GET, "/api/noticia/aprovadas").permitAll()
            .requestMatchers("/api/noticia/**").hasRole("COORDENADOR")
            .requestMatchers(HttpMethod.POST, "/api/egresso/salvar").permitAll()
            .requestMatchers(HttpMethod.PUT, "/api/egresso/atualizar/{id}").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/egresso/deletar/{id}").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/egresso/buscarPorNome").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/egresso/listar").hasRole("COORDENADOR")
            .requestMatchers(HttpMethod.GET, "/api/egresso/buscarAprovados").permitAll()
            .requestMatchers(HttpMethod.PUT, "/api/coordenador/atualizar/{id}").hasRole("COORDENADOR")
            .requestMatchers(HttpMethod.GET, "/api/coordenador/listar").hasRole("COORDENADOR")
            .requestMatchers(HttpMethod.GET, "/api/coordenador/buscar").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/cursos/listar").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/cursos/listar_egressos_por_curso/{id}").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/coordenador/listar_quantidade_egressos_por_curso/{id}").permitAll()

            .anyRequest().authenticated())
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
