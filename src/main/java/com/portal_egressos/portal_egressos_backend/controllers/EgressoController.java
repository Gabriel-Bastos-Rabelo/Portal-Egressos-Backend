package com.portal_egressos.portal_egressos_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portal_egressos.portal_egressos_backend.dto.EgressoDTO;
import com.portal_egressos.portal_egressos_backend.enums.UserRole;
import com.portal_egressos.portal_egressos_backend.models.Curso;
import com.portal_egressos.portal_egressos_backend.models.CursoEgresso;
import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.models.Usuario;
import com.portal_egressos.portal_egressos_backend.repositories.UsuarioRepository;
import com.portal_egressos.portal_egressos_backend.services.CursoService;
import com.portal_egressos.portal_egressos_backend.services.EgressoService;

@RestController
@RequestMapping("/api/egresso")
public class EgressoController {

    @Autowired
    private EgressoService egressoService;

    private CursoService cursoService;

    // Usuario service
    @Autowired
    private UsuarioRepository usuarioRepositorio;

    @PostMapping
    public ResponseEntity salvarEgresso(@RequestBody EgressoDTO egressoDTO) {
        try {
            Usuario usuario = Usuario.builder()
                    .email(egressoDTO.getEmailUsuario())
                    .senha(egressoDTO.getSenhaUsuario())
                    .role(UserRole.EGRESSO)
                    .build();

            usuarioRepositorio.save(usuario);
            
            Egresso egresso = converterParaModelo(egressoDTO, usuario);
            Egresso egressoRetornado = egressoService.salvarEgresso(egresso);
            Curso curso = cursoService.buscarPorId(egressoDTO.getIdCurso());
            salvarCursoEgresso(egressoDTO, egresso, curso);

            return ResponseEntity.ok(converterParaDTO(egressoRetornado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private Egresso converterParaModelo(EgressoDTO dto, Usuario usuario) {
        return Egresso.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .descricao(dto.getDescricao())
                .foto(dto.getFoto())
                .linkedin(dto.getLinkedin())
                .instagram(dto.getInstagram())
                .curriculo(dto.getCurriculo())
                .status(dto.getStatus())
                .usuario(usuario)
                .build();
    }

    private void salvarCursoEgresso(EgressoDTO dto, Egresso egressoSalvo, Curso cursoSalvo){
            CursoEgresso.builder()
            .egresso(egressoSalvo)
            .curso(cursoSalvo)
            .anoInicio(dto.getAnoInicio())
            .anoFim(dto.getAnoFim())
            .build();
    }

    private EgressoDTO converterParaDTO(Egresso egresso) {
        return EgressoDTO.builder()
                .id(egresso.getId())
                .nome(egresso.getNome())
                .descricao(egresso.getDescricao())
                .foto(egresso.getFoto())
                .linkedin(egresso.getLinkedin())
                .instagram(egresso.getInstagram())
                .curriculo(egresso.getCurriculo())
                .status(egresso.getStatus())
                .idUsuario(egresso.getUsuario().getId())
                .emailUsuario(egresso.getUsuario().getEmail())
                .build();
    }
}
