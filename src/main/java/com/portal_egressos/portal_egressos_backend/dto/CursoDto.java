
package com.portal_egressos.portal_egressos_backend.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class CursoDto {
    private Long id;
    private String nome;
    private String nivel;
    private Long idEgresso;
    private String nomeEgresso;
}
