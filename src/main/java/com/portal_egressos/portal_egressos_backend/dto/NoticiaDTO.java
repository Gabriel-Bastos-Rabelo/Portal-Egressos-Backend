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
    private String descricao;
    private String data;
    private String linkNoticia;
    private Status status;
    private String autor;
    private String imagemUrl;

}
