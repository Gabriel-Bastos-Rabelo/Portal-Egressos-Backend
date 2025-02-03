package com.portal_egressos.portal_egressos_backend.dto;

import java.time.LocalDate;

import com.portal_egressos.portal_egressos_backend.enums.Status;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter 
@Setter
@Builder
public class DepoimentoDto {
    private Long id;
    private String texto;
    private LocalDate data;
    private Status status;
    private Long idEgresso;
    private String nomeEgresso;
}
