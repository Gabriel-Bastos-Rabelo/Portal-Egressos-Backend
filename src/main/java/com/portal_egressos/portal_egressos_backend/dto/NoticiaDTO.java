package com.portal_egressos.portal_egressos_backend.dto;

import java.time.LocalDate;

import com.portal_egressos.portal_egressos_backend.enums.Status;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
@Builder
public class NoticiaDTO {

    private Long id;
    private String titulo;
    private String descricao;
    private LocalDate dataPublicacao;
    private LocalDate dataExtracao;
    private String linkNoticia;
    private Status status;
    private Long egressoId;
    private String nomeEgresso;
    private String fotoEgresso;

}
