package com.portal_egressos.portal_egressos_backend.repositories;
import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.models.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("test")
public class EgressoRepositoryTest {
    @Autowired
    EgressoRepository repository;

    @Test
    public void deveSalvarEgresso() {
        //cenário
        Usuario usuario = Usuario.builder()
                                 .email("teste@teste.com")
                                 .senha("123456")
                                 .build();

        Egresso egresso = Egresso.builder().nome("teste")
                                        .descricao("lorem ipsum lore")  
                                        .foto("urlteste")      
                                        .linkedin("teste_linkedin")      
                                        .instagram("teste_instagram")      
                                        .curriculo("lorem ipsum lore")
                                        .usuario(usuario)      
                                        .build();
        //ação
        Egresso saved = repository.save(egresso);

        //rollback 
        repository.delete(saved);

        //verificação
        Assertions.assertNotNull(saved);
        Assertions.assertEquals(egresso.getNome(), saved.getNome());
        Assertions.assertEquals(egresso.getDescricao(), saved.getDescricao());
        Assertions.assertEquals(egresso.getFoto(), saved.getFoto());
        Assertions.assertEquals(egresso.getLinkedin(), saved.getLinkedin());
        Assertions.assertEquals(egresso.getInstagram(), saved.getInstagram());
        Assertions.assertEquals(egresso.getCurriculo(), saved.getCurriculo());
    }

    
    @Test
    public void deveSalvarVariosEgressos() {
        // cenário
        List<Usuario> users = new ArrayList<Usuario>();
        for (int i = 0; i < 3; i++) {
            users.add(
                    Usuario.builder().email("teste@teste.com" + i)
                                     .senha("123" + i)
                                     .build());
        }

        List<Egresso> egressos = new ArrayList<Egresso>();
        for (int i = 0; i < 3; i++) {
            egressos.add(
                    Egresso.builder().nome("teste")
                                     .descricao("lorem ipsum lore")  
                                     .foto("urlteste")      
                                     .linkedin("teste_linkedin")      
                                     .instagram("teste_instagram")      
                                     .curriculo("lorem ipsum lore")
                                     .usuario(users.get(i))      
                                     .build());
        }

        // ação
        List<Egresso> saved = repository.saveAll(egressos);

        //rollback 
        repository.deleteAll(saved);

        // verificação
        Assertions.assertNotNull(saved);
        Assertions.assertEquals(egressos.size(), saved.size());
        for (int i = 0; i < egressos.size(); i++) {
            Assertions.assertEquals(egressos.get(i).getDescricao(), saved.get(i).getDescricao());
            Assertions.assertEquals(egressos.get(i).getFoto(), saved.get(i).getFoto());
            Assertions.assertEquals(egressos.get(i).getLinkedin(), saved.get(i).getLinkedin());
            Assertions.assertEquals(egressos.get(i).getInstagram(), saved.get(i).getInstagram());
            Assertions.assertEquals(egressos.get(i).getCurriculo(), saved.get(i).getCurriculo());
        }
    }


    @Test
    public void deveAtualizarEgresso() {
        // cenário
        Usuario usuario = Usuario.builder()
                                 .email("teste@teste.com")
                                 .senha("123456")
                                 .build();

        Egresso egresso = Egresso.builder().nome("teste")
                                           .descricao("lorem ipsum lore")  
                                           .foto("urlteste")      
                                           .linkedin("teste_linkedin")      
                                           .instagram("teste_instagram")      
                                           .curriculo("lorem ipsum lore")
                                           .usuario(usuario)      
                                           .build();
        // ação
        Egresso saved = repository.save(egresso);
        saved.setNome("teste2@teste.com");
        saved.setDescricao("lorem lore lore ipsum ");
        saved.setFoto("testeUrl");
        saved.setLinkedin("linkedin_teste");
        saved.setInstagram("instagram_teste");
        saved.setCurriculo("lorem lore lore ipsum");
        Egresso returned = repository.save(saved);

        //rollback 
        repository.delete(saved);
        repository.delete(returned);

        // verificação
        Assertions.assertNotNull(saved);
        Assertions.assertEquals(saved.getNome(), returned.getNome());
        Assertions.assertEquals(saved.getDescricao(), returned.getDescricao());
        Assertions.assertEquals(saved.getFoto(), returned.getFoto());
        Assertions.assertEquals(saved.getLinkedin(), returned.getLinkedin());
        Assertions.assertEquals(saved.getInstagram(), returned.getInstagram());
        Assertions.assertEquals(saved.getCurriculo(), returned.getCurriculo());
    }


    @Test
    public void deveRemoverEgresso() {
        // cenário
        Usuario usuario = Usuario.builder()
                                 .email("teste@teste.com")
                                 .senha("123456")
                                 .build();

        Egresso egresso = Egresso.builder().nome("teste")
                                           .descricao("lorem ipsum lore")  
                                           .foto("urlteste")      
                                           .linkedin("teste_linkedin")      
                                           .instagram("teste_instagram")      
                                           .curriculo("lorem ipsum lore")
                                           .usuario(usuario)      
                                           .build();
        // ação
        Egresso saved = repository.save(egresso);
        Long id = saved.getId();
        repository.deleteById(id);
        Optional<Egresso> temp = repository.findById(id);

        //rollback
        repository.delete(saved);

        // verificação
        Assertions.assertFalse(temp.isPresent());
    }


    @Test
    public void deveObterEgressoPorNome() {
        // cenário
        Usuario usuario = Usuario.builder()
                                 .email("teste@teste.com")
                                 .senha("123")
                                 .build();

        Egresso egresso = Egresso.builder().nome("teste")
                                           .descricao("lorem ipsum lore")  
                                           .foto("urlteste")      
                                           .linkedin("teste_linkedin")      
                                           .instagram("teste_instagram")      
                                           .curriculo("lorem ipsum lore")
                                           .usuario(usuario)      
                                           .build();

        // ação
        Egresso saved = repository.save(egresso);
        Optional<Egresso> returned = repository.findByNome(saved.getNome());

        //rollback
        repository.delete(saved);

        // verificação
        Assertions.assertTrue(returned.isPresent());
        Assertions.assertEquals(egresso.getNome(), returned.get().getNome());
    }
}

