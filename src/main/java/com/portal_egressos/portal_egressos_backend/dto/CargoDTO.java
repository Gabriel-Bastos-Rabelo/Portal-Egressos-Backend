package com.portal_egressos.portal_egressos_backend.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CargoDTO {
    private Long id;
    private Long egressoId;
    private String descricao;
    private String local;
    private Integer anoInicio;
    private Integer anoFim;
    private String nomeEgresso;
    private String fotoEgresso;
}
