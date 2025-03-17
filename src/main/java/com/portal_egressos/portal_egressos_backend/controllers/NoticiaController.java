package com.portal_egressos.portal_egressos_backend.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.portal_egressos.portal_egressos_backend.dto.NoticiaDTO;
import com.portal_egressos.portal_egressos_backend.enums.Status;
import com.portal_egressos.portal_egressos_backend.models.Noticia;
import com.portal_egressos.portal_egressos_backend.services.NoticiaService;

@RestController
@RequestMapping("/api/noticia")
public class NoticiaController {

    @Autowired
    private NoticiaService noticiaService;

    
    @GetMapping
    public ResponseEntity<?> listarNoticias() {
        try {
            List<Noticia> noticias = noticiaService.listarNoticias();
            List<NoticiaDTO> noticiasDTO = noticias.stream().map(this::converterParaDTO).collect(Collectors.toList());
            return ResponseEntity.ok(noticiasDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatusNoticia(@PathVariable("id") Long id, @RequestParam Status status) {

        try {
            Noticia noticia = Noticia.builder().id(id).status(status).build();
            Noticia noticiaAtualizada = noticiaService.atualizarStatusNoticia(noticia);
            return ResponseEntity.ok(converterParaDTO(noticiaAtualizada));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/aprovadas")
    public ResponseEntity<?> listarNoticiasAprovadas() { 
        try {
            System.out.println("aquiii");
            List<Noticia> noticias = noticiaService.listarNoticiasAprovadas();
            System.out.println(noticias);
            List<NoticiaDTO> noticiasDTO = noticias.stream().map(this::converterParaDTO).collect(Collectors.toList());
            return ResponseEntity.ok(noticiasDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/pendentes")
    public ResponseEntity<?> listarNoticiasPendentes() {
        try {
            List<Noticia> noticias = noticiaService.listarNoticiasPendentes();
            List<NoticiaDTO> noticiasDTO = noticias.stream().map(this::converterParaDTO).collect(Collectors.toList());
            return ResponseEntity.ok(noticiasDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarNoticia(@PathVariable Long id) {
        try {
            Noticia noticia = noticiaService.buscarPorNoticiaId(id);
            noticiaService.removerNoticia(noticia);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }


    private NoticiaDTO converterParaDTO(Noticia noticia) {
        return NoticiaDTO.builder()
                .id(noticia.getId())
                .descricao(noticia.getDescricao())
                .data(noticia.getData())
                .status(noticia.getStatus())
                .autor(noticia.getAutor())
                .linkNoticia(noticia.getLinkNoticia())
                .imagemUrl(noticia.getImagemUrl())
                .build();
    }

}
