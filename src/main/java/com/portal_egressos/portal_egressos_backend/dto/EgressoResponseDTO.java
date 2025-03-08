package com.portal_egressos.portal_egressos_backend.dto;

import com.portal_egressos.portal_egressos_backend.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EgressoResponseDTO {
    private Long id;
    private String nome;
    private String descricao;
    private String foto;
    private String linkedin;
    private String instagram;
    private String curriculo;
    private Status status;
    private String emailUsuario;
    private List<CursoEgressoDTO> cursos;
}
