package com.portal_egressos.portal_egressos_backend.controllers;

import com.portal_egressos.portal_egressos_backend.dto.OportunidadeDTO;
import com.portal_egressos.portal_egressos_backend.enums.Status;
import com.portal_egressos.portal_egressos_backend.models.Oportunidade;
import com.portal_egressos.portal_egressos_backend.services.OportunidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/oportunidades")
public class OportunidadeController {

    @Autowired
    private OportunidadeService oportunidadeService;

    // Criar nova oportunidade
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Oportunidade> criarOportunidade(@RequestBody OportunidadeDTO oportunidadeDTO) {
        try {
            // Converter DTO para entidade
            Oportunidade oportunidade = oportunidadeDTO.toEntity();
            oportunidade.setStatus(Status.PENDENTE); 
            Oportunidade oportunidadeCriada = oportunidadeService.salvarOportunidade(oportunidade);
            return ResponseEntity.status(HttpStatus.CREATED).body(oportunidadeCriada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Atualizar uma oportunidade existente
    @PutMapping("/{id}")
    public ResponseEntity<Oportunidade> atualizarOportunidade(
            @PathVariable Long id, @RequestBody OportunidadeDTO oportunidadeDTO) {
        try {
            // Converter DTO para entidade
            Oportunidade oportunidade = oportunidadeDTO.toEntity();
            oportunidade.setId(id); // Setar o ID para atualizar a oportunidade existente
            Oportunidade oportunidadeAtualizada = oportunidadeService.atualizarOportunidade(oportunidade);
            return ResponseEntity.ok(oportunidadeAtualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Alterar o status de uma oportunidade
    @PatchMapping("/{id}/status")
    public ResponseEntity<Oportunidade> alterarStatusOportunidade(
            @PathVariable Long id, @RequestParam Status novoStatus) {
        try {
            Oportunidade oportunidade = oportunidadeService.buscarOportunidadePorId(id);
            oportunidade.setStatus(novoStatus); // Atualizar o status
            Oportunidade oportunidadeAtualizada = oportunidadeService.salvarOportunidade(oportunidade);
            return ResponseEntity.ok(oportunidadeAtualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Remover uma oportunidade
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerOportunidade(@PathVariable Long id) {
        Oportunidade oportunidade = new Oportunidade();
        oportunidade.setId(id);
        oportunidadeService.removerOportunidade(oportunidade);
    }

    // Listar todas as oportunidades públicas (status APROVADO)
    @GetMapping
    public ResponseEntity<List<Oportunidade>> listarOportunidadesPublicas() {
        try {
            List<Oportunidade> oportunidades = oportunidadeService.listarOportunidadesAprovadasOrdenadasPorData();
            return ResponseEntity.ok(oportunidades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Listar todas as oportunidades pendentes
    @GetMapping("/pendentes")
    public ResponseEntity<List<Oportunidade>> listarOportunidadesPendentes() {
        try {
            List<Oportunidade> oportunidades = oportunidadeService.listarOportunidadesPendentesOrdenadasPorData();
            return ResponseEntity.ok(oportunidades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Buscar oportunidades pelo título
    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<List<Oportunidade>> buscarPorTitulo(@PathVariable String titulo) {
        try {
            List<Oportunidade> oportunidades = oportunidadeService.buscarPorTitulo(titulo);
            return ResponseEntity.ok(oportunidades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Buscar oportunidades pelo nome do egresso
    @GetMapping("/egresso/{nome}")
    public ResponseEntity<List<Oportunidade>> buscarPorNomeEgresso(@PathVariable String nome) {
        try {
            List<Oportunidade> oportunidades = oportunidadeService.buscarPorNomeEgresso(nome);
            return ResponseEntity.ok(oportunidades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
