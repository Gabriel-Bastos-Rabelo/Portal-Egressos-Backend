package com.portal_egressos.portal_egressos_backend.dto;

public record JwtDto(
        String accessToken,
        String role,
        Long egressoId,
        String email) {
}