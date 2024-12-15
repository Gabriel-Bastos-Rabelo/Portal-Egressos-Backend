package com.portal_egressos.portal_egressos_backend.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.portal_egressos.portal_egressos_backend.exceptions.RegraNegocioRunTime;
import com.portal_egressos.portal_egressos_backend.models.Noticia;
import com.portal_egressos.portal_egressos_backend.repositories.NoticiaRepository;

import jakarta.transaction.Transactional;

@Service
public class NoticiaService {

    @Autowired
    NoticiaRepository noticiaRepository;
    
    private void verificarNoticia(Noticia noticia){
        if(noticia == null){
            throw new RegraNegocioRunTime("A notícia não pode ser nula.");
        }
        if(noticia.getTitulo() == null || noticia.getTitulo().isEmpty()){
            throw new RegraNegocioRunTime("O título da notícia é obrigatório.");
        }
        if(noticia.getDescricao() == null || noticia.getDescricao().isEmpty()){
            throw new RegraNegocioRunTime("A descrição da notícia é obrigatória.");
        }
        if(noticia.getDataPublicacao() == null){
            throw new RegraNegocioRunTime("A data de publicação é obrigatória.");
        }
        if(noticia.getDataExtracao() == null){
            throw new RegraNegocioRunTime("A data de extração é obrigatória.");
        }
        if(noticia.getLinkNoticia() == null || noticia.getLinkNoticia().isEmpty()){
            throw new RegraNegocioRunTime("O link da notícia é obrigatório.");
        }
    }
    
    @Transactional
    public Noticia salvar(Noticia noticia) {
        verificarNoticia(noticia);
        return noticiaRepository.save(noticia);
    }

    public List<Noticia> listarTodos() {
        return noticiaRepository.findAllByOrderByDataPublicacaoDesc();
    }

    public List<Noticia> listarNoticiasUltimos30Dias() {
        LocalDate dataLimite = LocalDate.now().minusDays(30);
        return noticiaRepository.findByDataPublicacaoAfterOrderByDataPublicacaoDesc(dataLimite);
    }

    public Noticia buscarPorId(Long id) {
        return noticiaRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioRunTime("Notícia não encontrada para o ID: " + id));
    }

    @Transactional
    public void remover(Long id) {
        Noticia noticia = buscarPorId(id);
        noticiaRepository.delete(noticia);
    }

}
