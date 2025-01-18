package com.portal_egressos.portal_egressos_backend.config.auth;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.portal_egressos.portal_egressos_backend.repositories.UsuarioRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {
  @Autowired
  TokenProvider tokenService;
  @Autowired
  UsuarioRepository userRepository;

  @SuppressWarnings("null")
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    // Ignorar endpoints públicos
    String path = request.getRequestURI();
    if (isPublicEndpoint(path)) {
      filterChain.doFilter(request, response);
      return;
    }

    // Autenticação para endpoints protegidos
    var token = this.recoverToken(request);
    if (token != null) {
      var login = tokenService.validateToken(token);
      var user = userRepository.findByEmail(login);
      if (user != null) {
        var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    }
    filterChain.doFilter(request, response);
  }

  private boolean isPublicEndpoint(String path) {
    return path.equals("/api/auth/signin") ||
           path.equals("/api/oportunidade/aprovadas") ||
           path.equals("/api/depoimento/aprovados") ||
           path.equals("/api/noticia/aprovadas") ||
           path.equals("/api/cursos/listar") ||
           path.matches("^/api/egresso/atualizar/\\d+$") || // Lidar com IDs
           path.matches("^/api/egresso/deletar/\\d+$") ||  // Lidar com IDs
           path.equals("/api/egresso/salvar") ||
           path.equals("/api/egresso/buscarPorNome") ||
           path.equals("/api/egresso/buscarAprovados") ||
           path.equals("/api/coordenador/buscar") ||
           path.matches("^/api/cursos/listar_egressos_por_curso/\\d+$") || // Lidar com IDs
           path.matches("^/api/coordenador/listar_quantidade_egressos_por_curso/\\d+$") || // Lidar com IDs
           path.matches("^/api/cargo/egresso/\\d+$"); // Lidar com IDs
}


  // Recuperar token do cabeçalho Authorization
  private String recoverToken(HttpServletRequest request) {
    var authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return null;
    }
    return authHeader.replace("Bearer ", "");
  }
}
