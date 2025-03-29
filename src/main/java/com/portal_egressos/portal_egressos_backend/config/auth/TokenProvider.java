package com.portal_egressos.portal_egressos_backend.config.auth;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.portal_egressos.portal_egressos_backend.models.Usuario;

@Service
public class TokenProvider {
  @Value("${security.jwt.token.secret-key}")
  private String JWT_SECRET;

  public String generateAccessToken(Usuario user) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
      return JWT.create()
          .withSubject(user.getEmail())
          .withClaim("email", user.getEmail())
          .withClaim("role", user.getRole().name())
          .withExpiresAt(genAccessExpirationDate())
          .sign(algorithm);
    } catch (JWTCreationException exception) {
      throw new JWTCreationException("Error while generating token", exception);
    }
  }

  public String validateToken(String token) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
      return JWT.require(algorithm)
          .build()
          .verify(token)
          .getSubject();
    } catch (JWTVerificationException exception) {
      throw new JWTVerificationException("Error while validating token", exception);
    }
  }

  public String extrairEmailDoToken(String token) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
      JWTVerifier verifier = JWT.require(algorithm).build();
      DecodedJWT decodedJWT = verifier.verify(token);
      return decodedJWT.getClaim("email").asString();
    } catch (JWTVerificationException e) {
      throw new RuntimeException("Token inválido ou expirado", e);
    }
  }

  private Instant genAccessExpirationDate() {
    return LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.of("-03:00"));
  }

  public String extrairRoleDoToken(String token) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
      JWTVerifier verifier = JWT.require(algorithm).build();
      DecodedJWT decodedJWT = verifier.verify(token);
      return decodedJWT.getClaim("role").asString();
    } catch (JWTVerificationException e) {
      throw new RuntimeException("Token inválido ou expirado", e);
    }
  }
}