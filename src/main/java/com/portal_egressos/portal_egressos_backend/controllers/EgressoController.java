package com.portal_egressos.portal_egressos_backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portal_egressos.portal_egressos_backend.dto.CoordenadorDTO;
import com.portal_egressos.portal_egressos_backend.dto.EgressoDTO;
import com.portal_egressos.portal_egressos_backend.dto.NoticiaDTO;
import com.portal_egressos.portal_egressos_backend.enums.Status;
import com.portal_egressos.portal_egressos_backend.enums.UserRole;
import com.portal_egressos.portal_egressos_backend.exceptions.RegraNegocioRunTime;
import com.portal_egressos.portal_egressos_backend.models.Coordenador;
import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.models.Noticia;
import com.portal_egressos.portal_egressos_backend.models.Usuario;
import com.portal_egressos.portal_egressos_backend.repositories.UsuarioRepository;
import com.portal_egressos.portal_egressos_backend.services.EgressoService;

@RestController
@RequestMapping("/api/egresso")
public class EgressoController {

    @Autowired
    private EgressoService egressoService;

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

            Egresso egresso = converterParaModelo(egressoDTO, usuario);
            Egresso egressoRetornado = egressoService.salvarEgresso(egresso);
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
