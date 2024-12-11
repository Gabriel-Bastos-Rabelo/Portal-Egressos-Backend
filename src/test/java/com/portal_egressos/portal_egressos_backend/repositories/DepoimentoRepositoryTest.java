package com.portal_egressos.portal_egressos_backend.repositories;
import java.util.List;



import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.portal_egressos.portal_egressos_backend.models.Depoimento;
import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.models.Usuario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
public class DepoimentoRepositoryTest {

    @Autowired
    DepoimentoRepository repository;

    @Autowired
    EgressoRepository egressoRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Test
    public void deveVerificarSalvarDepoimento() {
        // construção
        Usuario usuario = Usuario.builder()
                        .email("teste@teste.com")
                        .senha("123456")
                        .build();
        
        Egresso egresso = Egresso.builder()
                        .nome("Sabryna")
                        .descricao("estudante de ciencia da computacao")
                        .foto("url foto")
                        .linkedin("url linkedin")
                        .instagram("url instagram")
                        .curriculo("curriculo")
                        .usuario(usuario)
                        .build();

        Egresso egressoSalvo = egressoRepository.save(egresso);

        Depoimento depoimento = Depoimento.builder()
                .egresso(egressoSalvo)
                .texto("Depoimento Teste")
                .data(LocalDate.now())
                .build();

        // ação
        Depoimento depoimentoSalvo = repository.save(depoimento);

        // rollback
        repository.delete(depoimentoSalvo);
        egressoRepository.delete(egresso);

        // Verificação
        Assertions.assertNotNull(depoimentoSalvo);
        Assertions.assertEquals(depoimento.getTexto(), depoimentoSalvo.getTexto());
        Assertions.assertEquals(depoimento.getData(), depoimentoSalvo.getData());
        Assertions.assertEquals(depoimento.getEgresso().getId(), depoimentoSalvo.getEgresso().getId());

        // Verificar os dados do Egresso associado
        Assertions.assertEquals(egresso.getNome(), depoimentoSalvo.getEgresso().getNome());
        Assertions.assertEquals(egresso.getDescricao(), depoimentoSalvo.getEgresso().getDescricao());
        Assertions.assertEquals(egresso.getFoto(), depoimentoSalvo.getEgresso().getFoto());
        Assertions.assertEquals(egresso.getLinkedin(), depoimentoSalvo.getEgresso().getLinkedin());
        Assertions.assertEquals(egresso.getInstagram(), depoimentoSalvo.getEgresso().getInstagram());
        Assertions.assertEquals(egresso.getCurriculo(), depoimentoSalvo.getEgresso().getCurriculo());

    }

    @Test
    public void deveAtualizarDepoimento() {
        // construção
        Usuario usuario = Usuario.builder()
                        .email("teste@teste.com")
                        .senha("123456")
                        .build();

        Egresso egresso = Egresso.builder()
                        .nome("Sabryna")
                        .descricao("estudante de ciencia da computacao")
                        .foto("url foto")
                        .linkedin("url linkedin")
                        .instagram("url instagram")
                        .curriculo("curriculo")
                        .usuario(usuario)
                        .build();
       Egresso egressoSalvo = egressoRepository.save(egresso);

        Depoimento depoimento = Depoimento.builder()
                        .egresso(egressoSalvo)
                        .texto("Depoimento Teste")
                        .data(LocalDate.now())
                        .build();
        Depoimento depoimentoSalvo = repository.save(depoimento);

        // ação
        depoimentoSalvo.setTexto("Depoimento Atualizado");
        Depoimento updated = repository.save(depoimentoSalvo);

        // rollback
        repository.delete(updated);
        egressoRepository.delete(egressoSalvo);
        usuarioRepository.delete(usuario);

        // verificação
        Assertions.assertNotNull(updated);
        Assertions.assertEquals("Depoimento Atualizado", updated.getTexto());
        Assertions.assertEquals(depoimento.getData(), updated.getData()); 
        Assertions.assertEquals(depoimento.getEgresso().getId(), updated.getEgresso().getId()); 
    }

    @Test
    public void deveListarUsuariosESeusDepoimentos() {
        // Construção
        List<Usuario> usuarios = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            usuarios.add(Usuario.builder()
                    .email("usuario" + i + "@teste.com")
                    .senha("123456")
                    .build());
        }
    
        List<Egresso> egressos = new ArrayList<>();
        List<Depoimento> depoimentos = new ArrayList<>();
    
        for (int i = 0; i < usuarios.size(); i++) {
            Egresso egresso = Egresso.builder()
                            .nome("Sabryna"+i)
                            .descricao("estudante de ciencia da computacao")
                            .foto("url foto")
                            .linkedin("url linkedin")
                            .instagram("url instagram")
                            .curriculo("curriculo")
                            .usuario(usuarios.get(i))
                            .build();   
            egresso = egressoRepository.save(egresso);
            egressos.add(egresso);
    
            Depoimento depoimento = Depoimento.builder()
                    .egresso(egresso)
                    .texto("Depoimento do egresso " + i)
                    .data(LocalDate.now().minusDays(i))
                    .build();
            depoimento = repository.save(depoimento);
            depoimentos.add(depoimento);
        }
    
        // Ação
        List<Depoimento> fetchedDepoimentos = repository.findAllByOrderByDataDesc();
    
        // Rollback
        repository.deleteAll(depoimentos);
        egressoRepository.deleteAll(egressos);
    
        // Verificação
        Assertions.assertNotNull(fetchedDepoimentos);
        Assertions.assertEquals(3, fetchedDepoimentos.size());
    }

    @Test
    public void deveRemoverDepoimento() {
        // construção
        Usuario usuario = Usuario.builder()
                        .email("teste@teste.com")
                        .senha("123456")
                        .build();

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

        Depoimento depoimento = Depoimento.builder()
                        .egresso(egresso)
                        .texto("Depoimento Teste")
                        .data(LocalDate.now())
                        .build();
        Depoimento depoimentoSalvo = repository.save(depoimento);

        // ação
        repository.deleteById(depoimentoSalvo.getId());
        Optional<Depoimento> fetched = repository.findById(depoimentoSalvo.getId());

        // rollback
        egressoRepository.delete(egresso);

        // verificação
        Assertions.assertFalse(fetched.isPresent());
    }

    @Test
    public void deveListarDepoimentosOrdenadosPeloMaisRecente() {
        // construção
        List<Usuario> usuarios = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            usuarios.add(Usuario.builder()
                    .email("usuario" + i + "@teste.com")
                    .senha("123456")
                    .build());
        }
    
        List<Egresso> egressos = new ArrayList<>();
        List<Depoimento> depoimentos = new ArrayList<>();
    
        for (int i = 0; i < usuarios.size(); i++) {
            Egresso egresso = Egresso.builder()
                    .nome("Egresso " + i)
                    .descricao("Descrição do egresso " + i)
                    .foto("url foto")
                    .linkedin("url linkedin")
                    .instagram("url instagram")
                    .curriculo("curriculo")
                    .usuario(usuarios.get(i))
                    .build();
            egresso = egressoRepository.save(egresso);
            egressos.add(egresso);
    
            Depoimento depoimento = Depoimento.builder()
                    .egresso(egresso)
                    .texto("Depoimento do egresso " + i)
                    .data(LocalDate.now().minusDays(i))
                    .build();
            depoimento = repository.save(depoimento);
            depoimentos.add(depoimento);
        }
    
        // ação
        List<Depoimento> fetched = repository.findAllByOrderByDataDesc();
    
        // rollback
        repository.deleteAll(depoimentos);
        egressoRepository.deleteAll(egressos);
    
        // verificação
        Assertions.assertNotNull(fetched);
        Assertions.assertEquals(3, fetched.size());
        Assertions.assertTrue(fetched.get(0).getData().isAfter(fetched.get(1).getData()));
    
    }
}
