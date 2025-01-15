package com.portal_egressos.portal_egressos_backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.portal_egressos.portal_egressos_backend.enums.Status;
import com.portal_egressos.portal_egressos_backend.exceptions.RegraNegocioRunTime;
import com.portal_egressos.portal_egressos_backend.models.Noticia;
import com.portal_egressos.portal_egressos_backend.repositories.NoticiaRepository;

import jakarta.transaction.Transactional;

@Service
public class NoticiaService {

    @Autowired
    NoticiaRepository noticiaRepositorio;
    
    private void verificarNoticia(Noticia noticia){
        if(noticia == null){
            throw new RegraNegocioRunTime("A notícia não pode ser nula.");
        }
        if(noticia.getTitulo() == null || noticia.getTitulo().isEmpty()){
            throw new RegraNegocioRunTime("O título da notícia deve ser informado.");
        }
        if(noticia.getDescricao() == null || noticia.getDescricao().isEmpty()){
            throw new RegraNegocioRunTime("A descrição da notícia deve ser informada.");
        }
        if(noticia.getDataPublicacao() == null){
            throw new RegraNegocioRunTime("A data de publicação deve ser informada.");
        }
        if(noticia.getDataExtracao() == null){
            throw new RegraNegocioRunTime("A data de extração deve ser informada.");
        }
        if(noticia.getLinkNoticia() == null || noticia.getLinkNoticia().isEmpty()){
            throw new RegraNegocioRunTime("O link da notícia deve ser informado.");
        }
        if(noticia.getStatus() == null){
            throw new RegraNegocioRunTime("O status da notícia deve ser informado.");
        }
    }
    
    @Transactional
    public Noticia salvarNoticia(Noticia noticia) {
        verificarNoticia(noticia);
        return noticiaRepositorio.save(noticia);
    }

    public List<Noticia> listarNoticias(){
        return noticiaRepositorio.findAllByOrderByDataPublicacaoDesc();
    }

    public List<Noticia> listarNoticiasAprovadas() {
        return noticiaRepositorio.findAllByStatusOrderByDataPublicacaoDesc(Status.APROVADO);
    }

    public List<Noticia> listarNoticiasPendentes() {
        return noticiaRepositorio.findAllByStatusOrderByDataPublicacaoDesc(Status.PENDENTE);
    }
    
    @Transactional
    public Noticia atualizarStatusNoticia(Noticia noticiaAtualizada) {
        if(noticiaAtualizada.getId() == null){
            throw new RegraNegocioRunTime("O id da notícia é obrigatório.");
        }
        if(noticiaAtualizada.getStatus() == null){
            throw new RegraNegocioRunTime("O status da notícia é obrigatório.");
        }
        Noticia noticia = buscarPorNoticiaId(noticiaAtualizada.getId());
        noticia.setStatus(noticiaAtualizada.getStatus());     
        return noticiaRepositorio.save(noticia);  
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
