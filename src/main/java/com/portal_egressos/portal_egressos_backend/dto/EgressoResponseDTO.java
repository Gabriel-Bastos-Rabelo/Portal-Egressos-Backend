package com.portal_egressos.portal_egressos_backend.dto;

import com.portal_egressos.portal_egressos_backend.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EgressoResponseDTO {
    private Long id;
    private String nomeEgresso;
    private String descricao;
    private String foto;
    private String linkedin;
    private String instagram;
    private String curriculo;
    private Status status;
    private String emailUsuario;
    private String curso;
    private Integer anoConclusao;
    private Long idCurso;
    private String cargo;
    private Integer anoInicio;
    private Integer anoFim;
}
