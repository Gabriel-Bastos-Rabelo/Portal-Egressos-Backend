package com.portal_egressos.portal_egressos_backend.dto;

import java.time.LocalDate;
import com.portal_egressos.portal_egressos_backend.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepoimentoResponseDTO {
    private Long id;
    private String descricao;
    private LocalDate data;
    private Status status;
    private Long idEgresso;
    private String nomeEgresso;
    private String curso;
    private Integer anoConclusao;
    private String foto;
}
