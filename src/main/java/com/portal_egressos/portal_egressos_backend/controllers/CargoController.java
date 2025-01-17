package com.portal_egressos.portal_egressos_backend.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portal_egressos.portal_egressos_backend.config.auth.TokenProvider;
import com.portal_egressos.portal_egressos_backend.dto.CargoDTO;
import com.portal_egressos.portal_egressos_backend.enums.UserRole;
import com.portal_egressos.portal_egressos_backend.exceptions.RegraNegocioRunTime;
import com.portal_egressos.portal_egressos_backend.models.Cargo;
import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.models.Usuario;
import com.portal_egressos.portal_egressos_backend.services.CargoService;
import com.portal_egressos.portal_egressos_backend.services.EgressoService;
import com.portal_egressos.portal_egressos_backend.services.UsuarioService;

@RestController
@RequestMapping("/api/cargo")
public class CargoController {

    @Autowired
    private CargoService cargoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EgressoService egressoService;

    @Autowired
    private TokenProvider tokenService;

    @PostMapping
    public ResponseEntity<?> salvarCargo(@RequestBody CargoDTO cargoDTO,
            @RequestHeader("Authorization") String authorization) {
        try {
            Usuario usuario = obterUsuarioDoToken(authorization.substring(7));
            Egresso egresso = obterEgressoDoUsuario(usuario);

            Cargo cargo = converterParaModelo(cargoDTO);
            cargo.setEgresso(egresso);

            Cargo cargoSalvo = cargoService.salvarCargo(cargo);
            return ResponseEntity.ok(converterParaDTO(cargoSalvo));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/egresso/{egressoId}")
    public ResponseEntity<?> listarCargosPorEgresso(@PathVariable Long egressoId) {
        try {
            List<Cargo> cargos = cargoService.listarCargoPorEgressoId(egressoId);
            List<CargoDTO> cargosDTO = cargos.stream()
                    .map(this::converterParaDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(cargosDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarCargo(@PathVariable Long id, @RequestBody CargoDTO cargoDTO,
            @RequestHeader("Authorization") String authorization) {
        try {
            Usuario usuario = obterUsuarioDoToken(authorization.substring(7));
            Egresso egresso = obterEgressoDoUsuario(usuario);

            Cargo cargoExistente = cargoService.verificarCargoPorEgresso(id, egresso.getId());

            cargoDTO.setId(id);
            Cargo cargo = converterParaModelo(cargoDTO);
            cargo.setEgresso(egresso);

            Cargo cargoAtualizado = cargoService.atualizarCargo(cargo);
            return ResponseEntity.ok(converterParaDTO(cargoAtualizado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarCargo(@PathVariable Long id, @RequestHeader("Authorization") String authorization) {
        try {
            Usuario usuario = obterUsuarioDoToken(authorization.substring(7));

            if (usuario.getRole().equals(UserRole.COORDENADOR)) {
                Cargo cargo = Cargo.builder().id(id).build();
                cargoService.removerCargo(cargo);
            } else {
                Egresso egresso = obterEgressoDoUsuario(usuario);

                Cargo cargoExistente = cargoService.verificarCargoPorEgresso(id, egresso.getId());

                cargoService.removerCargo(cargoExistente);
            }

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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

    private Cargo converterParaModelo(CargoDTO dto) {
        return Cargo.builder()
                .id(dto.getId())
                .descricao(dto.getDescricao())
                .local(dto.getLocal())
                .anoInicio(dto.getAnoInicio())
                .anoFim(dto.getAnoFim())
                .build();
    }

    private CargoDTO converterParaDTO(Cargo cargo) {
        return CargoDTO.builder()
                .id(cargo.getId())
                .descricao(cargo.getDescricao())
                .local(cargo.getLocal())
                .anoInicio(cargo.getAnoInicio())
                .anoFim(cargo.getAnoFim())
                .egressoId(cargo.getEgresso().getId())
                .nomeEgresso(cargo.getEgresso().getNome())
                .fotoEgresso(cargo.getEgresso().getFoto())
                .build();
    }
}
