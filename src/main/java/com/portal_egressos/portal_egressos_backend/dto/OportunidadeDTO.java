package com.portal_egressos.portal_egressos_backend.dto;

import com.portal_egressos.portal_egressos_backend.models.Oportunidade;
import com.portal_egressos.portal_egressos_backend.models.Egresso;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class OportunidadeDTO {
   
    private String titulo;
    
    private String descricao;

    private String local;

    private String tipo;

    private LocalDate dataPublicacao;

    private LocalDate dataExpiracao;

    private BigDecimal salario;

    private String link;

    private Long idEgresso;

    public Oportunidade toEntity() {
        return Oportunidade.builder()
                .titulo(this.titulo)
                .descricao(this.descricao)
                .local(this.local)
                .tipo(this.tipo)
                .dataPublicacao(this.dataPublicacao)
                .dataExpiracao(this.dataExpiracao)
                .salario(this.salario)
                .link(this.link)
                .build();
    }
}

