package com.portal_egressos.portal_egressos_backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.portal_egressos.portal_egressos_backend.enums.Status;
import com.portal_egressos.portal_egressos_backend.exceptions.RegraNegocioRunTime;
import com.portal_egressos.portal_egressos_backend.models.Depoimento;
import com.portal_egressos.portal_egressos_backend.models.Noticia;
import com.portal_egressos.portal_egressos_backend.repositories.NoticiaRepository;

import jakarta.transaction.Transactional;

@Service
public class NoticiaService {

    @Autowired
    NoticiaRepository noticiaRepositorio;

    private void verificarNoticia(Noticia noticia) {
        if (noticia == null) {
            throw new RegraNegocioRunTime("A notícia não pode ser nula.");
        }
        if (noticia.getDescricao() == null || noticia.getDescricao().isEmpty()) {
            throw new RegraNegocioRunTime("A descrição da notícia deve ser informada.");
        }
        if (noticia.getData() == null) {
            throw new RegraNegocioRunTime("A data de publicação deve ser informada.");
        }
        if (noticia.getStatus() == null) {
            throw new RegraNegocioRunTime("O status da notícia deve ser informado.");
        }
    }

    public List<Noticia> listarNoticias() {
        return noticiaRepositorio.findAll();
    }

    public List<Noticia> listarNoticiasAprovadas() {
        return noticiaRepositorio.findAllByStatus(Status.APROVADO);
    }

    public List<Noticia> listarNoticiasPendentes() {
        return noticiaRepositorio.findAllByStatus(Status.PENDENTE);
    }

    @Transactional
    public Noticia atualizarStatusNoticia(Noticia noticiaAtualizada) {
        if (noticiaAtualizada.getId() == null) {
            throw new RegraNegocioRunTime("O id da notícia é obrigatório.");
        }
        if (noticiaAtualizada.getStatus() == null) {
            throw new RegraNegocioRunTime("O status da notícia é obrigatório.");
        }
        Noticia noticia = buscarPorNoticiaId(noticiaAtualizada.getId());
        noticia.setStatus(noticiaAtualizada.getStatus());
        return noticiaRepositorio.save(noticia);
    }

    @Transactional
    public void aprovarNoticias(List<Long> ids) {
        for (Long id : ids) {
            Noticia noticia = noticiaRepositorio.findById(id)
                    .orElseThrow(() -> new RuntimeException("Notícia não encontrada com ID: " + id));

            noticia.setStatus(Status.APROVADO);
            noticiaRepositorio.save(noticia);
        }
    }

    @Transactional
    public void reprovarNoticias(List<Long> ids) {
        for (Long id : ids) {
            Noticia noticia = noticiaRepositorio.findById(id)
                    .orElseThrow(() -> new RuntimeException("Notícia não encontrada com ID: " + id));

            noticia.setStatus(Status.NAO_APROVADO);
            noticiaRepositorio.save(noticia);
        }
    }

    public void verificarNoticiaId(Noticia noticia) {
        if ((noticia == null) || (noticia.getId() == null)
                || !(noticiaRepositorio.existsById(noticia.getId()))) {
            throw new RegraNegocioRunTime("ID da noticia é inválido.");
        }
    }

    public Noticia buscarPorNoticiaId(Long id) {
        return noticiaRepositorio.findById(id)
                .orElseThrow(() -> new RegraNegocioRunTime("Notícia não encontrada para o ID: " + id));
    }

    @Transactional
    public void removerNoticia(Noticia noticia) {
        verificarNoticiaId(noticia);
        noticiaRepositorio.delete(noticia);
    }

}
