package com.portal_egressos.portal_egressos_backend.controllers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.portal_egressos.portal_egressos_backend.config.auth.TokenProvider;
import com.portal_egressos.portal_egressos_backend.dto.CursoEgressoDTO;
import com.portal_egressos.portal_egressos_backend.dto.EgressoDTO;
import com.portal_egressos.portal_egressos_backend.dto.EgressoResponseDTO;
import com.portal_egressos.portal_egressos_backend.enums.Status;
import com.portal_egressos.portal_egressos_backend.enums.UserRole;
import com.portal_egressos.portal_egressos_backend.exceptions.RegraNegocioRunTime;
import com.portal_egressos.portal_egressos_backend.models.Curso;
import com.portal_egressos.portal_egressos_backend.models.CursoEgresso;
import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.models.Usuario;
import com.portal_egressos.portal_egressos_backend.services.CursoEgressoService;
import com.portal_egressos.portal_egressos_backend.services.CursoService;
import com.portal_egressos.portal_egressos_backend.services.EgressoService;
import com.portal_egressos.portal_egressos_backend.services.UsuarioService;


@RestController
@RequestMapping("/api/egresso")
public class EgressoController {

    @Autowired
    private EgressoService egressoService;

    @Autowired
    private CursoService cursoService;

    @Autowired
    private CursoEgressoService cursoEgressoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TokenProvider tokenService;

    @PostMapping(value = "/salvar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> salvarEgresso(@RequestPart(value="dto", required=false) EgressoDTO dto,
        @RequestPart(value="imagem", required=false) MultipartFile imagem) {
        try {
            Usuario usuario = Usuario.builder()
                    .email(dto.getEmailUsuario())
                    .senha(dto.getSenhaUsuario())
                    .role(UserRole.EGRESSO)
                    .build();

            Egresso egresso = converterParaModelo(dto);
            egresso.setStatus(Status.PENDENTE);
            egresso.setUsuario(usuario);
            Curso curso = cursoService.buscarPorId(dto.getIdCurso());
            Egresso egressoRetornado = egressoService.salvarEgresso(egresso, imagem);
            CursoEgresso cursoEgresso = salvarCursoEgresso(dto, egressoRetornado, curso);
            cursoEgressoService.salvar(cursoEgresso);

            return ResponseEntity.status(HttpStatus.CREATED).body(converterParaDTO(egressoRetornado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping(value = "/atualizar/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> atualizarEgresso(
        @PathVariable Long id,
        @RequestPart("dto") EgressoDTO dto,
        @RequestPart(value = "imagem", required = false) MultipartFile imagem,
        @RequestHeader("Authorization") String authorization) {
        try {
            Usuario usuario = obterUsuarioDoToken(authorization.substring(7));
            Egresso egressoAtualizado;
            if (usuario.getRole().equals(UserRole.COORDENADOR)) {
                Egresso egressoRetornado = egressoService.buscarPorId(id);
                Egresso egresso = converterParaModelo(dto);
                egresso.setId(id);
                egresso.setUsuario(egressoRetornado.getUsuario());
                egressoAtualizado = egressoService.atualizarEgresso(egresso, imagem);
            }
            else{
                Egresso egressoRequest = obterEgressoDoUsuario(usuario);
                if(!egressoRequest.getId().equals(id)){
                    throw new RegraNegocioRunTime("Você não tem permissão para atualizar este egresso.");
                }
                Egresso egresso = converterParaModelo(dto);
                egresso.setId(id);
                egresso.setUsuario(egressoRequest.getUsuario());
                egressoAtualizado = egressoService.atualizarEgresso(egresso, imagem);

            }
            
            return ResponseEntity.ok(converterParaDTO(egressoAtualizado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/aprovar")
    public ResponseEntity<?> aprovarEgressos(@RequestBody List<Long> ids) {

        try{
            egressoService.aprovarEgressos(ids);
            return ResponseEntity.noContent().build();
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deletarEgresso(@PathVariable Long id,  @RequestHeader("Authorization") String authorization) {
        try {
            Usuario usuario = obterUsuarioDoToken(authorization.substring(7));
            if (!usuario.getRole().equals(UserRole.COORDENADOR)) {
                Egresso egressoRequest = obterEgressoDoUsuario(usuario);
                if(!egressoRequest.getId().equals(id)){
                    throw new RegraNegocioRunTime("Você não tem permissão para deletar este egresso.");
                }
            }
            egressoService.removerEgresso(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/buscarPorNome")
    public ResponseEntity<?> buscarPorNome(@RequestParam("nome") String nome) {
        try {
            List<Egresso> egressosRetornado = egressoService.buscarEgressoPorNome(nome);
            List<EgressoResponseDTO> egressosDTO = egressosRetornado.stream().map(this::converterParaDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(egressosDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/buscarAprovados")
    public ResponseEntity<?> buscarEgressoAprovados(
        @RequestParam(defaultValue = "0") int pagina
    ) {
        try {
            List<Egresso> egressosRetornado = egressoService.listarEgressosAprovados(pagina);
            List<EgressoResponseDTO> egressosDTO = egressosRetornado.stream().map(this::converterParaDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(egressosDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<?> listarEgressos(
        @RequestParam(defaultValue = "0") int pagina
    ) {
        try {
            List<Egresso> egressosRetornado = egressoService.listarEgressos(pagina);
            List<EgressoResponseDTO> egressosDTO = egressosRetornado.stream().map(this::converterParaDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(egressosDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private Egresso converterParaModelo(EgressoDTO dto) {
        return Egresso.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .descricao(dto.getDescricao())
                .foto(dto.getFoto())
                .linkedin(dto.getLinkedin())
                .instagram(dto.getInstagram())
                .curriculo(dto.getCurriculo())
                .build();
    }

    private CursoEgresso salvarCursoEgresso(EgressoDTO dto, Egresso egressoSalvo, Curso cursoSalvo) {
        return CursoEgresso.builder()
                .egresso(egressoSalvo)
                .curso(cursoSalvo)
                .anoInicio(dto.getAnoInicio())
                .anoFim(dto.getAnoFim())
                .build();
    }

    private EgressoResponseDTO converterParaDTO(Egresso egresso) {
        return EgressoResponseDTO.builder()
                .id(egresso.getId())
                .nome(egresso.getNome())
                .descricao(egresso.getDescricao())
                .foto(egresso.getFoto())
                .linkedin(egresso.getLinkedin())
                .instagram(egresso.getInstagram())
                .curriculo(egresso.getCurriculo())
                .status(egresso.getStatus())
                .emailUsuario(egresso.getUsuario().getEmail())
                .cursos(egresso.getEgressoCursos() != null ? 
                    egresso.getEgressoCursos().stream()
                    .map(egressoCurso -> new CursoEgressoDTO(
                            egressoCurso.getCurso().getNome(), 
                            egressoCurso.getAnoInicio(),
                            egressoCurso.getAnoFim()))
                    .collect(Collectors.toList())
                    : Collections.emptyList())
                .build();
    }

    private Usuario obterUsuarioDoToken(String token) {
        String email = tokenService.extrairEmailDoToken(token);
        return usuarioService.buscarUsuarioPorEmail(email);
    }

    private Egresso obterEgressoDoUsuario(Usuario usuario) {
        List<Egresso> egressos = egressoService.buscarEgresso(Egresso.builder().usuario(usuario).build());
        if (egressos.isEmpty()) {
            throw new RegraNegocioRunTime("Egresso não encontrado para o usuário associado.");
        }
        return egressos.get(0);
    }
}
