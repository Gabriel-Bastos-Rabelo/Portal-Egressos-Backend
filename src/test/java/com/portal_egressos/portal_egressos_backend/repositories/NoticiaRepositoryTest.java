package com.portal_egressos.portal_egressos_backend.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.portal_egressos.portal_egressos_backend.enums.Status;
import com.portal_egressos.portal_egressos_backend.models.Noticia;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
public class NoticiaRepositoryTest {

        @Autowired
        NoticiaRepository noticiaRepositorio;

        @Test
        @Transactional
        public void deveVerificarLeituraNoticia() {
                

                Noticia noticia = Noticia.builder()
                                .descricao("descricao massa")
                                .data("6 months ago")
                                .linkNoticia("link da noticia")
                                .imagemUrl("url imagem capa")
                                .status(Status.PENDENTE)
                                .autor("UFMA")
                                .build();

                Noticia noticiaSalva = noticiaRepositorio.save(noticia);
                Optional<Noticia> noticiaLidaOpt = noticiaRepositorio.findById(noticiaSalva.getId());

                Assertions.assertTrue(noticiaLidaOpt.isPresent());
                Noticia noticiaLida = noticiaLidaOpt.get();

                Assertions.assertNotNull(noticiaLida);
                Assertions.assertEquals("descricao massa", noticiaLida.getDescricao());
                Assertions.assertEquals("link da noticia", noticiaLida.getLinkNoticia());
                Assertions.assertEquals("url imagem capa", noticiaLida.getImagemUrl());
                Assertions.assertEquals(Status.PENDENTE, noticiaLida.getStatus());
                Assertions.assertEquals("6 months ago", noticiaLida.getData());

                noticiaRepositorio.delete(noticiaSalva);
        }

        @Test
        @Transactional
        public void deveVerificarAtualizarNoticia() {
               
                Noticia noticia = Noticia.builder()
                        .descricao("descricao massa")
                        .data("6 months ago")
                        .linkNoticia("link da noticia")
                        .imagemUrl("url imagem capa")
                        .status(Status.PENDENTE)
                        .autor("UFMA")
                        .build();

                Noticia noticiaSalva = noticiaRepositorio.save(noticia);
                noticiaSalva.setDescricao("Descricao atualizada");
                noticiaSalva.setStatus(Status.APROVADO);

                Noticia noticiaAtualizada = noticiaRepositorio.save(noticiaSalva);

                Assertions.assertEquals("Descricao atualizada", noticiaAtualizada.getDescricao());
                Assertions.assertEquals(Status.APROVADO, noticiaAtualizada.getStatus());

                noticiaRepositorio.delete(noticiaAtualizada);
        }

        @Test
        @Transactional
        public void deveVerificarRemoverNoticia() {
               

                Noticia noticia = Noticia.builder()
                        .descricao("descricao massa")
                        .data("6 months ago")
                        .linkNoticia("link da noticia")
                        .imagemUrl("url imagem capa")
                        .status(Status.PENDENTE)
                        .autor("UFMA")
                        .build();

                Noticia noticiaSalva = noticiaRepositorio.save(noticia);
                Long id = noticiaSalva.getId();
                noticiaRepositorio.deleteById(id);

                Optional<Noticia> noticiaRemovida = noticiaRepositorio.findById(id);
                Assertions.assertFalse(noticiaRemovida.isPresent());

        }
}
