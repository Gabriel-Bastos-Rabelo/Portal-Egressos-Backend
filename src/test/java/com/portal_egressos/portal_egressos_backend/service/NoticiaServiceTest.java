package com.portal_egressos.portal_egressos_backend.service;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.portal_egressos.portal_egressos_backend.enums.Status;
import com.portal_egressos.portal_egressos_backend.enums.UserRole;
import com.portal_egressos.portal_egressos_backend.exceptions.RegraNegocioRunTime;
import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.models.Noticia;
import com.portal_egressos.portal_egressos_backend.models.Usuario;
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
    public void deveSalvarNoticia() {
        Usuario usuario = Usuario.builder()
                .email("teste@teste.com")
                .senha("123456")
                .role(UserRole.EGRESSO)
                .build();

        Egresso egresso = Egresso.builder()
                .nome("Gabriel Bastos")
                .descricao("estudante de ciencia da computacao")
                .usuario(usuario)
                .build();

        Egresso egressoSalvo = egressoRepositorio.save(egresso);

        Noticia noticia = Noticia.builder()
                .egresso(egressoSalvo)
                .titulo("um titulo tql")
                .descricao("descricao massa")
                .dataPublicacao(LocalDate.of(2024, 12, 10))
                .dataExtracao(LocalDate.of(2024, 12, 10))
                .linkNoticia("link da noticia")
                .imagemCapa("url imagem capa")
                .status(Status.NOT_APPROVED)
                .build();

        // ação
        Noticia noticiaSalva = noticiaService.salvarNoticia(noticia);
        Assertions.assertNotNull(noticiaSalva);
        Assertions.assertNotNull(noticiaSalva.getId());

        // rollback
        noticiaRepositorio.delete(noticiaSalva);
        egressoRepositorio.delete(egressoSalvo);

    }

    @Test
    @Transactional
    public void deveGerarErroAoTentarSalvarNoticiaNula() {

        Assertions.assertThrows(RegraNegocioRunTime.class, () -> noticiaService.salvarNoticia(null),
                "A notícia não pode ser nula.");
    }

    @Test
    @Transactional
    public void deveGerarErroAoTentarSalvarNoticiaSemTitulo() {
        Noticia noticia = Noticia.builder()
                .descricao("Descricao da noticia")
                .dataPublicacao(LocalDate.of(2024, 12, 10))
                .dataExtracao(LocalDate.of(2024, 12, 10))
                .linkNoticia("http://link.com")
                .status(Status.NOT_APPROVED)
                .build();

        Assertions.assertThrows(RegraNegocioRunTime.class, () -> noticiaService.salvarNoticia(noticia),
                "O título da notícia deve ser informado.");
    }

    @Test
    @Transactional
    public void deveGerarErroAoTentarSalvarNoticiaSemDescricao() {
        Noticia noticia = Noticia.builder()
                .titulo("Titulo da noticia")
                .dataPublicacao(LocalDate.of(2024, 12, 10))
                .dataExtracao(LocalDate.of(2024, 12, 10))
                .linkNoticia("http://link.com")
                .status(Status.NOT_APPROVED)
                .build();

        Assertions.assertThrows(RegraNegocioRunTime.class, () -> noticiaService.salvarNoticia(noticia),
                "A descrição da notícia deve ser informada.");
    }

    @Test
    @Transactional
    public void deveGerarErroAoTentarSalvarNoticiaSemDataPublicacao() {
        Noticia noticia = Noticia.builder()
                .titulo("Titulo da noticia")
                .descricao("Descricao da noticia")
                .dataExtracao(LocalDate.of(2024, 12, 10))
                .linkNoticia("http://link.com")
                .status(Status.NOT_APPROVED)
                .build();

        Assertions.assertThrows(RegraNegocioRunTime.class, () -> noticiaService.salvarNoticia(noticia),
                "A data de publicação deve ser informada.");
    }

    @Test
    @Transactional
    public void deveGerarErroAoTentarSalvarNoticiaSemDataExtracao() {
        Noticia noticia = Noticia.builder()
                .titulo("Titulo da noticia")
                .descricao("Descricao da noticia")
                .dataPublicacao(LocalDate.of(2024, 12, 10))
                .linkNoticia("http://link.com")
                .status(Status.NOT_APPROVED)
                .build();

        Assertions.assertThrows(RegraNegocioRunTime.class, () -> noticiaService.salvarNoticia(noticia),
                "A data de extração deve ser informada.");
    }

    @Test
    @Transactional
    public void deveGerarErroAoTentarSalvarNoticiaSemLinkNoticia() {
        Noticia noticia = Noticia.builder()
                .titulo("Titulo da noticia")
                .descricao("Descricao da noticia")
                .dataPublicacao(LocalDate.of(2024, 12, 10))
                .dataExtracao(LocalDate.of(2024, 12, 10))
                .status(Status.NOT_APPROVED)
                .build();

        Assertions.assertThrows(RegraNegocioRunTime.class, () -> noticiaService.salvarNoticia(noticia),
                "O link da notícia deve ser informado.");
    }

    @Test
    @Transactional
    public void deveGerarErroAoTentarSalvarNoticiaSemStatus() {
        Noticia noticia = Noticia.builder()
                .titulo("Titulo da noticia")
                .descricao("Descricao da noticia")
                .dataPublicacao(LocalDate.of(2024, 12, 10))
                .dataExtracao(LocalDate.of(2024, 12, 10))
                .linkNoticia("http://link.com")
                .build();

        Assertions.assertThrows(RegraNegocioRunTime.class, () -> noticiaService.salvarNoticia(noticia),
                "O status da notícia deve ser informado.");
    }

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
    @Transactional
    public void deveObterListaNoticiasAprovadas() {
        Usuario usuario = Usuario.builder()
                .email("teste@teste.com")
                .senha("123456")
                .role(UserRole.EGRESSO)
                .build();

        Egresso egresso = Egresso.builder()
                .nome("Gabriel Bastos")
                .descricao("estudante de ciencia da computacao")
                .usuario(usuario)
                .build();

        Egresso egressoSalvo = egressoRepositorio.save(egresso);

        Noticia noticia = Noticia.builder()
                .egresso(egressoSalvo)
                .titulo("Noticia Aprovada")
                .descricao("Descricao da noticia")
                .dataPublicacao(LocalDate.of(2024, 12, 10))
                .dataExtracao(LocalDate.of(2024, 12, 10))
                .linkNoticia("http://link.com")
                .status(Status.APPROVED)
                .build();
        Noticia noticiaSalva = noticiaRepositorio.save(noticia);

        List<Noticia> noticias = noticiaService.listarNoticiasAprovadas();
        Assertions.assertFalse(noticias.isEmpty());
        noticias.forEach(n -> Assertions.assertEquals(Status.APPROVED, n.getStatus()));

        // rollback
        noticiaRepositorio.delete(noticiaSalva);
        egressoRepositorio.delete(egressoSalvo);
    }

    @Test
    @Transactional
    public void deveObterListaNoticiasAprovadasVaziaQuandoNaoHouver() {
        Usuario usuario = Usuario.builder()
                .email("teste@teste.com")
                .senha("123456")
                .role(UserRole.EGRESSO)
                .build();

        Egresso egresso = Egresso.builder()
                .nome("Gabriel Bastos")
                .descricao("estudante de ciencia da computacao")
                .usuario(usuario)
                .build();

        Egresso egressoSalvo = egressoRepositorio.save(egresso);

        Noticia noticia = Noticia.builder()
                .egresso(egressoSalvo)
                .titulo("Noticia Aprovada")
                .descricao("Descricao da noticia")
                .dataPublicacao(LocalDate.of(2024, 12, 10))
                .dataExtracao(LocalDate.of(2024, 12, 10))
                .linkNoticia("http://link.com")
                .status(Status.NOT_APPROVED)
                .build();
        Noticia noticiaSalva = noticiaRepositorio.save(noticia);
        List<Noticia> noticias = noticiaService.listarNoticiasAprovadas();
        Assertions.assertTrue(noticias.isEmpty());
        // rollback
        noticiaRepositorio.delete(noticiaSalva);
        egressoRepositorio.delete(egressoSalvo);
    }

    @Test
    @Transactional
    public void deveAtualizarStatusNoticiaParaAprovado() {
        Usuario usuario = Usuario.builder()
                .email("teste@teste.com")
                .senha("123456")
                .role(UserRole.EGRESSO)
                .build();

        Egresso egresso = Egresso.builder()
                .nome("Gabriel Bastos")
                .descricao("estudante de ciencia da computacao")
                .usuario(usuario)
                .build();

        Egresso egressoSalvo = egressoRepositorio.save(egresso);
        Noticia noticia = Noticia.builder()
                .egresso(egressoSalvo)
                .titulo("Titulo")
                .descricao("Descricao")
                .dataPublicacao(LocalDate.now())
                .dataExtracao(LocalDate.now())
                .linkNoticia("http://link.com")
                .status(Status.NOT_APPROVED)
                .build();

        Noticia noticiaSalva = noticiaRepositorio.save(noticia);

        noticiaSalva.setStatus(Status.APPROVED);
        Noticia noticiaAtualizada = noticiaService.atualizarStatusAprovada(noticiaSalva);

        Assertions.assertNotNull(noticiaAtualizada);
        Assertions.assertEquals(Status.APPROVED, noticiaAtualizada.getStatus());

        // rollback
        noticiaRepositorio.delete(noticiaSalva);
        egressoRepositorio.delete(egressoSalvo);
    }

    public void deveGerarErroAoTentarAtualizarStatusDeNoticiaNula() {
        Assertions.assertThrows(RegraNegocioRunTime.class, () -> noticiaService.atualizarStatusAprovada(null),
                "A notícia não pode ser nula.");
    }

    @Test
    @Transactional
    public void deveObterListaNoticiasUltimos30dias() {
        Usuario usuario = Usuario.builder()
                .email("teste@teste.com")
                .senha("123456")
                .role(UserRole.EGRESSO)
                .build();

        Egresso egresso = Egresso.builder()
                .nome("Gabriel Bastos")
                .descricao("estudante de ciencia da computacao")
                .usuario(usuario)
                .build();

        Egresso egressoSalvo = egressoRepositorio.save(egresso);

        Noticia noticiaAntiga = Noticia.builder()
                .egresso(egressoSalvo)
                .titulo("Noticia Antiga")
                .descricao("Descricao antiga")
                .dataPublicacao(LocalDate.now().minusDays(31))
                .dataExtracao(LocalDate.now().minusDays(31))
                .linkNoticia("http://linkantigo.com")
                .status(Status.APPROVED)
                .build();
        Noticia noticiaRecente = Noticia.builder()
                .egresso(egressoSalvo)
                .titulo("Noticia Recente")
                .descricao("Descricao recente")
                .dataPublicacao(LocalDate.now().minusDays(5))
                .dataExtracao(LocalDate.now().minusDays(5))
                .linkNoticia("http://linkrecente.com")
                .status(Status.APPROVED)
                .build();

        noticiaRepositorio.save(noticiaAntiga);
        noticiaRepositorio.save(noticiaRecente);

        List<Noticia> noticias = noticiaService.listarNoticiasUltimos30Dias();
        Assertions.assertFalse(noticias.isEmpty());
        Assertions.assertEquals(1, noticias.size());
        Assertions.assertEquals("Noticia Recente", noticias.get(0).getTitulo());

        // rollback
        noticiaRepositorio.delete(noticiaAntiga);
        noticiaRepositorio.delete(noticiaRecente);
        egressoRepositorio.delete(egressoSalvo);

    }

    @Test
    @Transactional
    public void deveObterListaNoticiasUltimos30diasVaziaQuandoNaoHouver() {
        Usuario usuario = Usuario.builder()
                .email("teste@teste.com")
                .senha("123456")
                .role(UserRole.EGRESSO)
                .build();

        Egresso egresso = Egresso.builder()
                .nome("Gabriel Bastos")
                .descricao("estudante de ciencia da computacao")
                .usuario(usuario)
                .build();

        Egresso egressoSalvo = egressoRepositorio.save(egresso);
        Noticia noticiaAntiga = Noticia.builder()
                .egresso(egressoSalvo)
                .titulo("Noticia Antiga")
                .descricao("Descricao antiga")
                .dataPublicacao(LocalDate.now().minusDays(31))
                .dataExtracao(LocalDate.now().minusDays(31))
                .linkNoticia("http://linkantigo.com")
                .status(Status.APPROVED)
                .build();

        noticiaRepositorio.save(noticiaAntiga);

        List<Noticia> noticias = noticiaService.listarNoticiasUltimos30Dias();
        Assertions.assertTrue(noticias.isEmpty());

        // rollback
        noticiaRepositorio.delete(noticiaAntiga);
        egressoRepositorio.delete(egressoSalvo);

    }

    @Test
    @Transactional
    public void deveRemoverNoticia() {
        Usuario usuario = Usuario.builder()
                .email("teste@teste.com")
                .senha("123456")
                .role(UserRole.EGRESSO)
                .build();

        Egresso egresso = Egresso.builder()
                .nome("Gabriel Bastos")
                .descricao("estudante de ciencia da computacao")
                .usuario(usuario)
                .build();

        Egresso egressoSalvo = egressoRepositorio.save(egresso);
        Noticia noticia = Noticia.builder()
                .egresso(egressoSalvo)
                .titulo("Titulo")
                .descricao("Descricao")
                .dataPublicacao(LocalDate.now())
                .dataExtracao(LocalDate.now())
                .linkNoticia("http://link.com")
                .status(Status.APPROVED)
                .build();

        Noticia noticiaSalva = noticiaRepositorio.save(noticia);
        noticiaService.removerNoticia(noticiaSalva);

        Assertions.assertFalse(noticiaRepositorio.existsById(noticiaSalva.getId()));

        // rollback
        noticiaRepositorio.delete(noticiaSalva);
        egressoRepositorio.delete(egressoSalvo);
    }

    @Test
    @Transactional
    public void deveGerarErroAoTentarRemoverNoticiaNula() {
        Assertions.assertThrows(RegraNegocioRunTime.class, () -> noticiaService.removerNoticia(null),
                "ID da noticia é inválido.");
    }

}
