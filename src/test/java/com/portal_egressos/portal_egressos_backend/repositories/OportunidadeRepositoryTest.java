package com.portal_egressos.portal_egressos_backend.repositories;

import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.models.Oportunidade;
import com.portal_egressos.portal_egressos_backend.models.Usuario;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
public class OportunidadeRepositoryTest {

    @Autowired
    OportunidadeRepository repository;

    @Autowired
    EgressoRepository egressoRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Test
    public void deveVerificarSalvarOportunidade() {
        // Construção
        Usuario usuario = Usuario.builder()
                .email("teste@teste.com")
                .senha("123456")
                .build();
        usuario = usuarioRepository.save(usuario);

        Egresso egresso = Egresso.builder()
                        .nome("Sabryna")
                        .descricao("estudante de ciencia da computacao")
                        .foto("url foto")
                        .linkedin("url linkedin")
                        .instagram("url instagram")
                        .curriculo("curriculo")
                        .usuario(usuario)
                        .build();

        egresso = egressoRepository.save(egresso);

        Oportunidade oportunidade = Oportunidade.builder()
                                .egresso(egresso)
                                .titulo("Desenvolvedor Backend")
                                .descricao("Desenvolvimento em Java")
                                .local("Remoto")
                                .tipo("CLT")
                                .dataPublicacao(LocalDate.now())
                                .dataExpiracao(LocalDate.now().plusDays(30))
                                .salario(BigDecimal.valueOf(6000))
                                .link("link vaga")
                                .status("Ativa")
                                .build();

        // Ação
        Oportunidade oportunidadeSalva = repository.save(oportunidade);

        // Rollback
        repository.delete(oportunidadeSalva);
        egressoRepository.delete(egresso);
        usuarioRepository.delete(usuario);

        // Verificação
        Assertions.assertNotNull(oportunidadeSalva);
        Assertions.assertEquals(oportunidade.getTitulo(), oportunidadeSalva.getTitulo());
        Assertions.assertEquals(oportunidade.getDescricao(), oportunidadeSalva.getDescricao());
        Assertions.assertEquals(oportunidade.getLocal(), oportunidadeSalva.getLocal());
        Assertions.assertEquals(oportunidade.getTipo(), oportunidadeSalva.getTipo());
        Assertions.assertEquals(oportunidade.getDataPublicacao(), oportunidadeSalva.getDataPublicacao());
        Assertions.assertEquals(oportunidade.getDataExpiracao(), oportunidadeSalva.getDataExpiracao());
        Assertions.assertEquals(oportunidade.getSalario(), oportunidadeSalva.getSalario());
        Assertions.assertEquals(oportunidade.getLink(), oportunidadeSalva.getLink());
        Assertions.assertEquals(oportunidade.getStatus(), oportunidadeSalva.getStatus());
        Assertions.assertEquals(oportunidade.getEgresso().getId(), oportunidadeSalva.getEgresso().getId());
    }

    @Test
    public void deveAtualizarOportunidade() {
        // Construção
        Usuario usuario = Usuario.builder()
                .email("teste@teste.com")
                .senha("123456")
                .build();
        usuario = usuarioRepository.save(usuario);

        Egresso egresso = Egresso.builder()
                        .nome("Sabryna")
                        .descricao("estudante de ciencia da computacao")
                        .foto("url foto")
                        .linkedin("url linkedin")
                        .instagram("url instagram")
                        .curriculo("curriculo")
                        .usuario(usuario)
                        .build();

        egresso = egressoRepository.save(egresso);

        Oportunidade oportunidade = Oportunidade.builder()
                                .egresso(egresso)
                                .titulo("Desenvolvedor Backend")
                                .descricao("Desenvolvimento em Java")
                                .local("Remoto")
                                .tipo("CLT")
                                .dataPublicacao(LocalDate.now())
                                .salario(BigDecimal.valueOf(6000))
                                .status("Ativa")
                                .build();
        Oportunidade oportunidadeSalva = repository.save(oportunidade);

        // Ação
        oportunidadeSalva.setTitulo("Desenvolvedor Full Stack");
        oportunidadeSalva.setSalario(BigDecimal.valueOf(7000));
        Oportunidade updated = repository.save(oportunidadeSalva);

        // Rollback
        repository.delete(updated);
        egressoRepository.delete(egresso);
        usuarioRepository.delete(usuario);

        // Verificação
        Assertions.assertNotNull(updated);
        Assertions.assertEquals("Desenvolvedor Full Stack", updated.getTitulo());
        Assertions.assertEquals(BigDecimal.valueOf(7000), updated.getSalario());

        // Verificar que os outros campos permanecem inalterados
        Assertions.assertEquals(oportunidade.getDescricao(), updated.getDescricao());
        Assertions.assertEquals(oportunidade.getLocal(), updated.getLocal());
        Assertions.assertEquals(oportunidade.getTipo(), updated.getTipo());
        Assertions.assertEquals(oportunidade.getDataPublicacao(), updated.getDataPublicacao());
        Assertions.assertEquals(oportunidade.getStatus(), updated.getStatus());
        Assertions.assertEquals(oportunidade.getEgresso().getId(), updated.getEgresso().getId());
    }

    @Test
    public void deveListarUsuariosEOportunidades() {
        // Construção
        List<Usuario> usuarios = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            usuarios.add(Usuario.builder()
                    .email("usuario" + i + "@teste.com")
                    .senha("123456")
                    .build());
        }
        usuarios = usuarioRepository.saveAll(usuarios);

        List<Egresso> egressos = new ArrayList<>();
        List<Oportunidade> oportunidades = new ArrayList<>();

        for (int i = 0; i < usuarios.size(); i++) {
                        Egresso egresso = Egresso.builder()
                        .nome("Egresso"+i)
                        .descricao("estudante de ciencia da computacao")
                        .foto("url foto")
                        .linkedin("url linkedin")
                        .instagram("url instagram")
                        .curriculo("curriculo")
                        .usuario(usuarios.get(i))
                        .build();

                    
            egresso = egressoRepository.save(egresso);
            egressos.add(egresso);

            for (int j = 0; j < 2; j++) {
                Oportunidade oportunidade = Oportunidade.builder()
                        .egresso(egresso)
                        .titulo("Oportunidade " + j + " do egresso " + i)
                        .descricao("Descrição da oportunidade " + j)
                        .local("Local " + j)
                        .tipo("Tipo " + j)
                        .dataPublicacao(LocalDate.now().minusDays(j))
                        .salario(BigDecimal.valueOf(3000 + j * 1000))
                        .status("Ativa")
                        .build();
                oportunidade = repository.save(oportunidade);
                oportunidades.add(oportunidade);
            }
        }

        // Ação
        List<Oportunidade> fetchedOportunidades = repository.findAll();

        // Rollback
        repository.deleteAll(oportunidades);
        egressoRepository.deleteAll(egressos);
        usuarioRepository.deleteAll(usuarios);

        // Verificação
        Assertions.assertNotNull(fetchedOportunidades);
        Assertions.assertEquals(6, fetchedOportunidades.size());
    }

    @Test
    public void deveRemoverOportunidade() {
        // Construção
        Usuario usuario = Usuario.builder()
                .email("teste@teste.com")
                .senha("123456")
                .build();
        usuario = usuarioRepository.save(usuario);

        Egresso egresso = Egresso.builder()
                        .nome("Sabryna")
                        .descricao("estudante de ciencia da computacao")
                        .foto("url foto")
                        .linkedin("url linkedin")
                        .instagram("url instagram")
                        .curriculo("curriculo")
                        .usuario(usuario)
                        .build();
        egresso = egressoRepository.save(egresso);

        Oportunidade oportunidade = Oportunidade.builder()
                .egresso(egresso)
                .titulo("Desenvolvedor Backend")
                .descricao("Desenvolvimento em Java")
                .local("Remoto")
                .tipo("CLT")
                .dataPublicacao(LocalDate.now())
                .salario(BigDecimal.valueOf(6000))
                .status("Ativa")
                .build();
        Oportunidade oportunidadeSalva = repository.save(oportunidade);

        // Ação
        repository.deleteById(oportunidadeSalva.getId());
        Optional<Oportunidade> fetched = repository.findById(oportunidadeSalva.getId());

        // Rollback
        egressoRepository.delete(egresso);
        usuarioRepository.delete(usuario);

        // Verificação
        Assertions.assertFalse(fetched.isPresent());
    }
@Test
    public void deveBuscarPorTituloContendoPalavraChave() {
        // Construção
        Usuario usuario = Usuario.builder()
                .email("teste@teste.com")
                .senha("123456")
                .build();
        usuario = usuarioRepository.save(usuario);

        Egresso egresso = Egresso.builder()
        .nome("Sabryna")
                        .descricao("estudante de ciencia da computacao")
                        .foto("url foto")
                        .linkedin("url linkedin")
                        .instagram("url instagram")
                        .curriculo("curriculo")
                        .usuario(usuario)
                        .build();
        egresso = egressoRepository.save(egresso);

        List<Oportunidade> oportunidades = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            oportunidades.add(Oportunidade.builder()
                    .egresso(egresso)
                    .titulo("Oportunidade " + i)
                    .descricao("Descrição da oportunidade " + i)
                    .local("Local " + i)
                    .tipo("Tipo " + i)
                    .dataPublicacao(LocalDate.now().minusDays(i))
                    .salario(BigDecimal.valueOf(5000 + i * 1000))
                    .status("Ativa")
                    .build());
        }
        repository.saveAll(oportunidades);

        // Ação
        List<Oportunidade> fetched = repository.findByTituloContaining("Oportunidade");

        // Rollback
        repository.deleteAll(oportunidades);
        egressoRepository.delete(egresso);
        usuarioRepository.delete(usuario);

        // Verificação
        Assertions.assertNotNull(fetched);
        Assertions.assertEquals(3, fetched.size());
    }

    
    @Test
    public void deveListarOportunidadesOrdenadasPorDataPublicacao() {
        // Construção
        Usuario usuario = Usuario.builder()
                .email("teste@teste.com")
                .senha("123456")
                .build();
        usuario = usuarioRepository.save(usuario);

        Egresso egresso = Egresso.builder()
                        .nome("Sabryna")
                        .descricao("estudante de ciencia da computacao")
                        .foto("url foto")
                        .linkedin("url linkedin")
                        .instagram("url instagram")
                        .curriculo("curriculo")
                        .usuario(usuario)
                        .build();
        egresso = egressoRepository.save(egresso);

        List<Oportunidade> oportunidades = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            oportunidades.add(Oportunidade.builder()
                    .egresso(egresso)
                    .titulo("Oportunidade " + i)
                    .descricao("Descrição da oportunidade " + i)
                    .local("Local " + i)
                    .tipo("Tipo " + i)
                    .dataPublicacao(LocalDate.now().minusDays(i))
                    .salario(BigDecimal.valueOf(5000 + i * 1000))
                    .status("Ativa")
                    .build());
        }
        repository.saveAll(oportunidades);

        // Ação
        List<Oportunidade> fetched = repository.findAllByOrderByDataPublicacaoDesc();

        // Rollback
        repository.deleteAll(oportunidades);
        egressoRepository.delete(egresso);
        usuarioRepository.delete(usuario);

        // Verificação
        Assertions.assertNotNull(fetched);
        Assertions.assertEquals(3, fetched.size());
        Assertions.assertTrue(fetched.get(0).getDataPublicacao().isAfter(fetched.get(1).getDataPublicacao()));
    }
}

