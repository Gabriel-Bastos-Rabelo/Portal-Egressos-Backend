package com.portal_egressos.portal_egressos_backend.dto;

import com.portal_egressos.portal_egressos_backend.enums.Status;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EgressoDTO {
    private Long id;
    private String nome;
    private String descricao;
    private String foto;
    private String linkedin;
    private String instagram;
    private String curriculo;
    private Status status;
    private Long idUsuario;
    private String emailUsuario;
    private String senhaUsuario;
    private Long idCurso;
    private Integer anoInicio;
    private Integer anoFim;

}