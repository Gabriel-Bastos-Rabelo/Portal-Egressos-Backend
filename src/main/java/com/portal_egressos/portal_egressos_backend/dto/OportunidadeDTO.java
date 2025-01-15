package com.portal_egressos.portal_egressos_backend.dto;
import com.portal_egressos.portal_egressos_backend.enums.Status;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Getter 
@Setter
@Builder
public class OportunidadeDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private String local;
    private String tipo;
    private LocalDate dataPublicacao;
    private LocalDate dataExpiracao;
    private BigDecimal salario;
    private String link;
    private Status status;
    private Long idEgresso;
    private String nomeEgresso;

}

