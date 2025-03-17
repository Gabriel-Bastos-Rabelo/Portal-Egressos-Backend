package com.portal_egressos.portal_egressos_backend.service;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.portal_egressos.portal_egressos_backend.enums.Status;
import com.portal_egressos.portal_egressos_backend.exceptions.RegraNegocioRunTime;
import com.portal_egressos.portal_egressos_backend.models.Noticia;
import com.portal_egressos.portal_egressos_backend.repositories.EgressoRepository;
import com.portal_egressos.portal_egressos_backend.repositories.NoticiaRepository;
import com.portal_egressos.portal_egressos_backend.services.NoticiaService;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
public class NoticiaServiceTest {

    @Autowired
    NoticiaRepository noticiaRepositorio;

    @Autowired
    NoticiaService noticiaService;

    @Autowired
    EgressoRepository egressoRepositorio;



    @Test
    @Transactional
    public void deveObterListaNoticias() {
        List<Noticia> noticias = noticiaService.listarNoticias();
        Assertions.assertNotNull(noticias);
        Assertions.assertTrue(noticias.size() >= 0);
    }

    @Test
    @Transactional
    public void deveObterListaNoticiasVaziaQuandoNaoHouver() {
        noticiaRepositorio.deleteAll();
        List<Noticia> noticias = noticiaService.listarNoticias();
        Assertions.assertTrue(noticias.isEmpty());
    }


    @Test
    public void deveObterListaNoticiasPendentes() {

        Noticia noticia = Noticia.builder()
                .descricao("Descricao da noticia")
                .data("6 months ago")
                .linkNoticia("http://link.com")
                .imagemUrl("http://link.com")
                .status(Status.PENDENTE)
                .autor("UFMA")
                .build();
        Noticia noticiaSalva = noticiaRepositorio.save(noticia);

        List<Noticia> noticias = noticiaService.listarNoticiasPendentes();
        Assertions.assertFalse(noticias.isEmpty());
        noticias.forEach(n -> Assertions.assertEquals(Status.PENDENTE, n.getStatus()));

        //rollback
        noticiaRepositorio.delete(noticiaSalva);
    }

    @Test
    @Transactional
    public void deveObterListaNoticiasAprovadas() {
       

        Noticia noticia = Noticia.builder()
                .descricao("Descricao da noticia")
                .data("6 months ago")
                .linkNoticia("http://link.com")
                .imagemUrl("http://link.com")
                .status(Status.APROVADO)
                .autor("UFMA")
                .build();
        Noticia noticiaSalva = noticiaRepositorio.save(noticia);

        List<Noticia> noticias = noticiaService.listarNoticiasAprovadas();
        Assertions.assertFalse(noticias.isEmpty());
        noticias.forEach(n -> Assertions.assertEquals(Status.APROVADO, n.getStatus()));

        // rollback
        noticiaRepositorio.delete(noticiaSalva);
    }

    @Test
    @Transactional
    public void deveObterListaNoticiasAprovadasVaziaQuandoNaoHouver() {
        

        Noticia noticia = Noticia.builder()
                .descricao("Descricao da noticia")
                .data("6 months ago")
                .linkNoticia("http://link.com")
                .imagemUrl("http://link.com")
                .status(Status.PENDENTE)
                .autor("UFMA")
                .build();
        Noticia noticiaSalva = noticiaRepositorio.save(noticia);
        List<Noticia> noticias = noticiaService.listarNoticiasAprovadas();
        Assertions.assertTrue(noticias.isEmpty());
        // rollback
        noticiaRepositorio.delete(noticiaSalva);
    }

    @Test
    @Transactional
    public void deveAtualizarStatusNoticiaParaAprovado() {
        

        Noticia noticia = Noticia.builder()
                .descricao("Descricao da noticia")
                .data("6 months ago")
                .linkNoticia("http://link.com")
                .imagemUrl("http://link.com")
                .status(Status.PENDENTE)
                .autor("UFMA")
                .build();

        Noticia noticiaSalva = noticiaRepositorio.save(noticia);

        noticiaSalva.setStatus(Status.APROVADO);
        Noticia noticiaAtualizada = noticiaService.atualizarStatusNoticia(noticiaSalva);

        Assertions.assertNotNull(noticiaAtualizada);
        Assertions.assertEquals(Status.APROVADO, noticiaAtualizada.getStatus());

        // rollback
        noticiaRepositorio.delete(noticiaSalva);
    }

    public void deveGerarErroAoTentarAtualizarStatusDeNoticiaNula() {
        Assertions.assertThrows(RegraNegocioRunTime.class, () -> noticiaService.atualizarStatusNoticia(null),
                "A notícia não pode ser nula.");
    }

    
    @Test
    @Transactional
    public void deveRemoverNoticia() {
        
        Noticia noticia = Noticia.builder()
                .descricao("Descricao da noticia")
                .data("6 months ago")
                .linkNoticia("http://link.com")
                .imagemUrl("http://link.com")
                .status(Status.APROVADO)
                .autor("UFMA")
                .build();

        Noticia noticiaSalva = noticiaRepositorio.save(noticia);
        noticiaService.removerNoticia(noticiaSalva);

        Assertions.assertFalse(noticiaRepositorio.existsById(noticiaSalva.getId()));

        // rollback
        noticiaRepositorio.delete(noticiaSalva);
    }

    @Test
    @Transactional
    public void deveGerarErroAoTentarRemoverNoticiaNula() {
        Assertions.assertThrows(RegraNegocioRunTime.class, () -> noticiaService.removerNoticia(null),
                "ID da noticia é inválido.");
    }

}
