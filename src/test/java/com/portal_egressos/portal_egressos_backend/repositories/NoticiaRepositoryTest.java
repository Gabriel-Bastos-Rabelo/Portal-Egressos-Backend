package com.portal_egressos.portal_egressos_backend.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.models.Noticia;
import com.portal_egressos.portal_egressos_backend.models.Usuario;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
public class NoticiaRepositoryTest {

    @Autowired
    NoticiaRepository noticiaRepository;

    @Autowired
    EgressoRepository egressoRepository;

    @Test
    public void deveVerificarSalvarNoticia() {
        Usuario usuario = Usuario.builder()
                .email("teste@teste.com")
                .senha("123456")
                .build();

        Egresso egresso = Egresso.builder()
                .nome("Gabriel Bastos")
                .descricao("estudante de ciencia da computacao")
                .usuario(usuario)
                .build();

        Egresso egressoSalvo = egressoRepository.save(egresso);

        Noticia noticia = Noticia.builder()
                .egresso(egressoSalvo)
                .titulo("um titulo tql")
                .descricao("descricao massa")
                .dataPublicacao(LocalDate.of(2024, 12, 10))
                .dataExtracao(LocalDate.of(2024, 12, 10))
                .linkNoticia("link da noticia")
                .imagemCapa("url imagem capa")
                .build();

        Noticia noticiaSalva = noticiaRepository.save(noticia);

        Assertions.assertNotNull(noticiaSalva);
        Assertions.assertNotNull(noticiaSalva.getId());
        Assertions.assertEquals("um titulo tql", noticiaSalva.getTitulo());
        Assertions.assertEquals("descricao massa", noticiaSalva.getDescricao());
        Assertions.assertEquals("link da noticia", noticiaSalva.getLinkNoticia());
        Assertions.assertEquals("url imagem capa", noticiaSalva.getImagemCapa());
        Assertions.assertEquals(LocalDate.of(2024, 12, 10), noticiaSalva.getDataPublicacao());
        Assertions.assertEquals(LocalDate.of(2024, 12, 10), noticiaSalva.getDataExtracao());

        
        noticiaRepository.delete(noticiaSalva);
        egressoRepository.delete(egressoSalvo);
    }

    @Test
    public void deveVerificarLeituraNoticia() {
        Usuario usuario = Usuario.builder()
                .email("teste@teste.com")
                .senha("123456")
                .build();

        Egresso egresso = Egresso.builder()
                .nome("Gabriel Bastos")
                .descricao("estudante de ciencia da computacao")
                .usuario(usuario)
                .build();

        Egresso egressoSalvo = egressoRepository.save(egresso);

        Noticia noticia = Noticia.builder()
            .egresso(egressoSalvo)
            .titulo("um titulo tql")
            .descricao("descricao massa")
            .dataPublicacao(LocalDate.of(2024, 12, 10))
            .dataExtracao(LocalDate.of(2024, 12, 10))
            .linkNoticia("link da noticia")
            .imagemCapa("url imagem capa")
            .build();

        Noticia noticiaSalva = noticiaRepository.save(noticia);
        Optional<Noticia> noticiaLidaOpt = noticiaRepository.findById(noticiaSalva.getId());

        Assertions.assertTrue(noticiaLidaOpt.isPresent());
        Noticia noticiaLida = noticiaLidaOpt.get();
        
       
        Assertions.assertNotNull(noticiaLida);
        Assertions.assertEquals("um titulo tql", noticiaLida.getTitulo());
        Assertions.assertEquals("descricao massa", noticiaLida.getDescricao());
        Assertions.assertEquals("link da noticia", noticiaLida.getLinkNoticia());
        Assertions.assertEquals("url imagem capa", noticiaLida.getImagemCapa());
        Assertions.assertEquals(LocalDate.of(2024, 12, 10), noticiaLida.getDataPublicacao());
        Assertions.assertEquals(LocalDate.of(2024, 12, 10), noticiaLida.getDataExtracao());

        noticiaRepository.delete(noticiaSalva);
        egressoRepository.delete(egressoSalvo);
    }

    @Test
    public void deveVerificarAtualizarNoticia() {
        Usuario usuario = Usuario.builder()
                .email("teste@teste.com")
                .senha("123456")
                .build();

        Egresso egresso = Egresso.builder()
                .nome("Gabriel Bastos")
                .descricao("estudante de ciencia da computacao")
                .usuario(usuario)
                .build();

        Egresso egressoSalvo = egressoRepository.save(egresso);

        Noticia noticia = Noticia.builder()
            .egresso(egressoSalvo)
            .titulo("um titulo tql")
            .descricao("descricao massa")
            .dataPublicacao(LocalDate.of(2024, 12, 10))
            .dataExtracao(LocalDate.of(2024, 12, 10))
            .linkNoticia("link da noticia")
            .imagemCapa("url imagem capa")
            .build();

        Noticia noticiaSalva = noticiaRepository.save(noticia);
        noticiaSalva.setTitulo("Titulo atualizado");
        noticiaSalva.setDescricao("Descricao atualizada");

        Noticia noticiaAtualizada = noticiaRepository.save(noticiaSalva);
        
        Assertions.assertEquals("Titulo atualizado", noticiaAtualizada.getTitulo());
        Assertions.assertEquals("Descricao atualizada", noticiaAtualizada.getDescricao());

        noticiaRepository.delete(noticiaAtualizada);
        egressoRepository.delete(egressoSalvo);
    }

    @Test
    public void deveVerificarRemoverNoticia() {
        Usuario usuario = Usuario.builder()
                .email("teste@teste.com")
                .senha("123456")
                .build();

        Egresso egresso = Egresso.builder()
                .nome("Gabriel Bastos")
                .descricao("estudante de ciencia da computacao")
                .usuario(usuario)
                .build();

        Egresso egressoSalvo = egressoRepository.save(egresso);

        Noticia noticia = Noticia.builder()
            .egresso(egressoSalvo)
            .titulo("um titulo tql")
            .descricao("descricao massa")
            .dataPublicacao(LocalDate.of(2024, 12, 10))
            .dataExtracao(LocalDate.of(2024, 12, 10))
            .linkNoticia("link da noticia")
            .imagemCapa("url imagem capa")
            .build();

        Noticia noticiaSalva = noticiaRepository.save(noticia);
        Long id = noticiaSalva.getId();
        noticiaRepository.deleteById(id);

        Optional<Noticia> noticiaRemovida = noticiaRepository.findById(id);
        Assertions.assertFalse(noticiaRemovida.isPresent());

        egressoRepository.delete(egressoSalvo);
    }
}
