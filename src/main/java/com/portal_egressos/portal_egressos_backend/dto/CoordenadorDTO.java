package com.portal_egressos.portal_egressos_backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CoordenadorDTO {
    private Long id;
    private String nome;
    private java.time.LocalDateTime dataCriacao;
    private Boolean ativo;
    private Long idCurso;
    private String nomeCurso;
    private Long idUsuario;
    private String emailUsuario;
}