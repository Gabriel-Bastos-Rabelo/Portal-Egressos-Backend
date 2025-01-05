package com.portal_egressos.portal_egressos_backend.repositories;

import com.portal_egressos.portal_egressos_backend.enums.UserRole;
import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.models.Usuario;

import jakarta.transaction.Transactional;

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
        EgressoRepository repositorio;

        @Test
        @Transactional
        public void deveSalvarEgresso() {
                // cenário
                Usuario usuario = Usuario.builder()
                                .email("teste@teste.com")
                                .senha("123456")
                                .role(UserRole.EGRESSO)
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
                Egresso egressoSalvo = repositorio.save(egresso);

                // rollback
                repositorio.delete(egressoSalvo);

                // verificação
                Assertions.assertNotNull(egressoSalvo);
                Assertions.assertEquals(egresso.getNome(), egressoSalvo.getNome());
                Assertions.assertEquals(egresso.getDescricao(), egressoSalvo.getDescricao());
                Assertions.assertEquals(egresso.getFoto(), egressoSalvo.getFoto());
                Assertions.assertEquals(egresso.getLinkedin(), egressoSalvo.getLinkedin());
                Assertions.assertEquals(egresso.getInstagram(), egressoSalvo.getInstagram());
                Assertions.assertEquals(egresso.getCurriculo(), egressoSalvo.getCurriculo());
        }

        @Test
        @Transactional
        public void deveSalvarVariosEgressos() {
                // cenário
                List<Usuario> usuarios = new ArrayList<Usuario>();
                for (int i = 0; i < 3; i++) {
                        usuarios.add(
                                        Usuario.builder().email("teste@teste.com" + i)
                                                        .senha("123" + i)
                                                        .role(UserRole.EGRESSO)
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
                                                        .usuario(usuarios.get(i))
                                                        .build());
                }

                // ação
                List<Egresso> egressosSalvos = repositorio.saveAll(egressos);

                // rollback
                repositorio.deleteAll(egressosSalvos);

                // verificação
                Assertions.assertNotNull(egressosSalvos);
                Assertions.assertEquals(egressos.size(), egressosSalvos.size());
                for (int i = 0; i < egressos.size(); i++) {
                        Assertions.assertEquals(egressos.get(i).getDescricao(), egressosSalvos.get(i).getDescricao());
                        Assertions.assertEquals(egressos.get(i).getFoto(), egressosSalvos.get(i).getFoto());
                        Assertions.assertEquals(egressos.get(i).getLinkedin(), egressosSalvos.get(i).getLinkedin());
                        Assertions.assertEquals(egressos.get(i).getInstagram(), egressosSalvos.get(i).getInstagram());
                        Assertions.assertEquals(egressos.get(i).getCurriculo(), egressosSalvos.get(i).getCurriculo());
                }
        }

        @Test
        @Transactional
        public void deveAtualizarEgresso() {
                // cenário
                Usuario usuario = Usuario.builder()
                                .email("teste@teste.com")
                                .senha("123456")
                                .role(UserRole.EGRESSO)
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
                Egresso egressoSalvo = repositorio.save(egresso);
                egressoSalvo.setNome("teste2@teste.com");
                egressoSalvo.setDescricao("lorem lore lore ipsum ");
                egressoSalvo.setFoto("testeUrl");
                egressoSalvo.setLinkedin("linkedin_teste");
                egressoSalvo.setInstagram("instagram_teste");
                egressoSalvo.setCurriculo("lorem lore lore ipsum");
                Egresso egressoRetornado = repositorio.save(egressoSalvo);

                // rollback
                repositorio.delete(egressoSalvo);
                repositorio.delete(egressoRetornado);

                // verificação
                Assertions.assertNotNull(egressoSalvo);
                Assertions.assertEquals(egressoSalvo.getNome(), egressoRetornado.getNome());
                Assertions.assertEquals(egressoSalvo.getDescricao(), egressoRetornado.getDescricao());
                Assertions.assertEquals(egressoSalvo.getFoto(), egressoRetornado.getFoto());
                Assertions.assertEquals(egressoSalvo.getLinkedin(), egressoRetornado.getLinkedin());
                Assertions.assertEquals(egressoSalvo.getInstagram(), egressoRetornado.getInstagram());
                Assertions.assertEquals(egressoSalvo.getCurriculo(), egressoRetornado.getCurriculo());
        }

        @Test
        @Transactional
        public void deveRemoverEgresso() {
                // cenário
                Usuario usuario = Usuario.builder()
                                .email("teste@teste.com")
                                .senha("123456")
                                .role(UserRole.EGRESSO)
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
                Egresso egressoSalvo = repositorio.save(egresso);
                Long id = egressoSalvo.getId();
                repositorio.deleteById(id);
                Optional<Egresso> temp = repositorio.findById(id);

                // rollback
                repositorio.delete(egressoSalvo);

                // verificação
                Assertions.assertFalse(temp.isPresent());
        }

        @Test
        @Transactional
        public void deveObterEgressoPorNome() {
                // cenário
                Usuario usuario = Usuario.builder()
                                .email("teste@teste.com")
                                .senha("123")
                                .role(UserRole.EGRESSO)
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
                Egresso egressoSalvo = repositorio.save(egresso);
                Optional<Egresso> egressoRetornado = repositorio.findByNome(egressoSalvo.getNome());

                // rollback
                repositorio.delete(egressoSalvo);

                // verificação
                Assertions.assertTrue(egressoRetornado.isPresent());
                Assertions.assertEquals(egresso.getNome(), egressoRetornado.get().getNome());
        }
}
