package com.portal_egressos.portal_egressos_backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.portal_egressos.portal_egressos_backend.enums.UserRole;
import com.portal_egressos.portal_egressos_backend.exceptions.RegraNegocioRunTime;

import com.portal_egressos.portal_egressos_backend.models.Oportunidade;
import com.portal_egressos.portal_egressos_backend.models.Usuario;
import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.repositories.EgressoRepository;
import com.portal_egressos.portal_egressos_backend.repositories.OportunidadeRepository;
import com.portal_egressos.portal_egressos_backend.services.OportunidadeService;

@SpringBootTest
@ActiveProfiles("test")
public class OportunidadeServiceTest {
    @Autowired
    OportunidadeService oportunidadeService;

    @Autowired
    OportunidadeRepository oportunidadeRepositorio;

    @Autowired
    EgressoRepository egressoRepositorio;

    @Test
    public void deveSalvarOportunidade() {
        // Cenário
        Usuario usuario = Usuario.builder()
                .email("teste@teste.com")
                .senha("123456")
                .role(UserRole.EGRESSO)
                .build();

        Egresso egresso = Egresso.builder()
                .nome("Egresso")
                .descricao("estudante de ciencia da computacao")
                .usuario(usuario)
                .build();

        Egresso egressoSalvo = egressoRepositorio.save(egresso);

        Oportunidade oportunidade = Oportunidade.builder()
                .egresso(egressoSalvo)
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
        Oportunidade oportunidadeSalva = oportunidadeService.salvarOportunidade(oportunidade);

        // Rollback
        oportunidadeRepositorio.delete(oportunidadeSalva);
        egressoRepositorio.delete(egressoSalvo);

        // Verificação
        Assertions.assertNotNull(oportunidadeSalva);
        Assertions.assertNotNull(oportunidadeSalva.getId());
    }

    @Test
    public void deveGerarErroAoTentarSalvarOportunidadeNulo() {
        // Verificação
        Assertions.assertThrows(RegraNegocioRunTime.class, () -> {
            oportunidadeService.salvarOportunidade(null);
        }, "A oportunidade não pode ser nula");
    }

    @Test
    public void deveGerarErroAoSalvarSemTitulo() {
        // Cenário
        Usuario usuario = Usuario.builder()
                .email("teste@teste.com")
                .senha("123456")
                .role(UserRole.EGRESSO)
                .build();

        Egresso egresso = Egresso.builder()
                .nome("Egresso")
                .descricao("estudante de ciencia da computacao")
                .usuario(usuario)
                .build();

        Egresso egressoSalvo = egressoRepositorio.save(egresso);

        Oportunidade oportunidade = Oportunidade.builder()
                .egresso(egressoSalvo)
                .descricao("Desenvolvimento em Java")
                .local("Remoto")
                .tipo("CLT")
                .dataPublicacao(LocalDate.now())
                .dataExpiracao(LocalDate.now().plusDays(30))
                .salario(BigDecimal.valueOf(6000))
                .link("link vaga")
                .status("Ativa")
                .build();
        // Verificação
        Assertions.assertThrows(RegraNegocioRunTime.class, () -> oportunidadeService.salvarOportunidade(oportunidade),
                "O Título da oportunidade desse ser Infomado.");
    }

    @Test
    public void deveGerarErroAoSalvarSemDescricao() {
        // Cenário
        Usuario usuario = Usuario.builder()
                .email("teste@teste.com")
                .senha("123456")
                .role(UserRole.EGRESSO)
                .build();

        Egresso egresso = Egresso.builder()
                .nome("Egresso")
                .descricao("estudante de ciencia da computacao")
                .usuario(usuario)
                .build();

        Egresso egressoSalvo = egressoRepositorio.save(egresso);

        Oportunidade oportunidade = Oportunidade.builder()
                .egresso(egressoSalvo)
                .titulo("Desenvolvedor Backend")
                .local("Remoto")
                .tipo("CLT")
                .dataPublicacao(LocalDate.now())
                .dataExpiracao(LocalDate.now().plusDays(30))
                .salario(BigDecimal.valueOf(6000))
                .link("link vaga")
                .status("Ativa")
                .build();

        // Verificação
        Assertions.assertThrows(RegraNegocioRunTime.class, () -> oportunidadeService.salvarOportunidade(oportunidade),
                "A Descrição da oportunidade desse ser Infomada.");
    }

    @Test
    public void deveGerarErroAoSalvarSemLocal() {
        // Cenário
        Usuario usuario = Usuario.builder()
                .email("teste@teste.com")
                .senha("123456")
                .role(UserRole.EGRESSO)
                .build();

        Egresso egresso = Egresso.builder()
                .nome("Egresso")
                .descricao("estudante de ciencia da computacao")
                .usuario(usuario)
                .build();

        Egresso egressoSalvo = egressoRepositorio.save(egresso);

        Oportunidade oportunidade = Oportunidade.builder()
                .egresso(egressoSalvo)
                .titulo("Desenvolvedor Backend")
                .descricao("Desenvolvimento em Java")
                .tipo("CLT")
                .dataPublicacao(LocalDate.now())
                .dataExpiracao(LocalDate.now().plusDays(30))
                .salario(BigDecimal.valueOf(6000))
                .link("link vaga")
                .status("Ativa")
                .build();

        // Verificação
        Assertions.assertThrows(RegraNegocioRunTime.class, () -> oportunidadeService.salvarOportunidade(oportunidade),
                "O local da oportunidade desse ser Infomado.");
    }

    @Test
    public void deveGerarErroAoSalvarSemTipo() {
        // Cenário
        Usuario usuario = Usuario.builder()
                .email("teste@teste.com")
                .senha("123456")
                .role(UserRole.EGRESSO)
                .build();

        Egresso egresso = Egresso.builder()
                .nome("Egresso")
                .descricao("estudante de ciencia da computacao")
                .usuario(usuario)
                .build();

        Egresso egressoSalvo = egressoRepositorio.save(egresso);

        Oportunidade oportunidade = Oportunidade.builder()
                .egresso(egressoSalvo)
                .titulo("Desenvolvedor Backend")
                .descricao("Desenvolvimento em Java")
                .local("Remoto")
                .dataPublicacao(LocalDate.now())
                .dataExpiracao(LocalDate.now().plusDays(30))
                .salario(BigDecimal.valueOf(6000))
                .link("link vaga")
                .status("Ativa")
                .build();

        // Verificação
        Assertions.assertThrows(RegraNegocioRunTime.class, () -> oportunidadeService.salvarOportunidade(oportunidade),
                "O Tipo da oportunidade desse ser Infomado.");
    }

    @Test
    public void deveGerarErroAoSalvarSemDataPublicacao() {
        // Cenário
        Usuario usuario = Usuario.builder()
                .email("teste@teste.com")
                .senha("123456")
                .role(UserRole.EGRESSO)
                .build();

        Egresso egresso = Egresso.builder()
                .nome("Egresso")
                .descricao("estudante de ciencia da computacao")
                .usuario(usuario)
                .build();

        Egresso egressoSalvo = egressoRepositorio.save(egresso);

        Oportunidade oportunidade = Oportunidade.builder()
                .egresso(egressoSalvo)
                .titulo("Desenvolvedor Backend")
                .descricao("Desenvolvimento em Java")
                .local("Remoto")
                .tipo("CLT")
                .dataExpiracao(LocalDate.now().plusDays(30))
                .salario(BigDecimal.valueOf(6000))
                .link("link vaga")
                .status("Ativa")
                .build();
        // Verificação
        Assertions.assertThrows(RegraNegocioRunTime.class, () -> oportunidadeService.salvarOportunidade(oportunidade),
                "A data da oportunidade desse ser Infomada.");
    }

    @Test
    public void deveGerarErroAoSalvarSemStatus() {
        // Cenário
        Usuario usuario = Usuario.builder()
                .email("teste@teste.com")
                .senha("123456")
                .role(UserRole.EGRESSO)
                .build();

        Egresso egresso = Egresso.builder()
                .nome("Egresso")
                .descricao("estudante de ciencia da computacao")
                .usuario(usuario)
                .build();

        Egresso egressoSalvo = egressoRepositorio.save(egresso);

        Oportunidade oportunidade = Oportunidade.builder()
                .egresso(egressoSalvo)
                .titulo("Desenvolvedor Backend")
                .descricao("Desenvolvimento em Java")
                .local("Remoto")
                .tipo("CLT")
                .dataPublicacao(LocalDate.now())
                .dataExpiracao(LocalDate.now().plusDays(30))
                .salario(BigDecimal.valueOf(6000))
                .link("link vaga")
                .build();

        // Verificação
        Assertions.assertThrows(RegraNegocioRunTime.class, () -> oportunidadeService.salvarOportunidade(oportunidade),
                "O Status da oportunidade desse ser Infomado.");
    }

    @Test
    public void deveAtualizarOportunidade() {
        // Cenário
        Usuario usuario = Usuario.builder()
                .email("teste@teste.com")
                .senha("123456")
                .role(UserRole.EGRESSO)
                .build();

        Egresso egresso = Egresso.builder()
                .nome("Egresso")
                .descricao("estudante de ciencia da computacao")
                .usuario(usuario)
                .build();

        Egresso egressoSalvo = egressoRepositorio.save(egresso);

        Oportunidade oportunidade = Oportunidade.builder()
                .egresso(egressoSalvo)
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
        Oportunidade oportunidadeSalva = oportunidadeService.salvarOportunidade(oportunidade);

        // Ação
        oportunidadeSalva.setTitulo("Desenvolvedor Java");
        oportunidadeSalva.setDescricao("Atualização da vaga");
        oportunidadeSalva.setLocal("São Luís");
        oportunidadeSalva.setDataPublicacao(LocalDate.now());
        oportunidadeSalva.setDataExpiracao(LocalDate.now().plusDays(30));
        oportunidadeSalva.setSalario(BigDecimal.valueOf(2500));
        oportunidadeSalva.setLink("Link.atualizado.com");
        oportunidadeSalva.setStatus("Inativo");

        Oportunidade oportunidadeAtualizada = oportunidadeService.atualizarOportunidade(oportunidadeSalva);

        // Rollback
        oportunidadeRepositorio.delete(oportunidadeSalva);
        egressoRepositorio.delete(egressoSalvo);

        // Verificação
        Assertions.assertNotNull(oportunidadeSalva);
        Assertions.assertEquals(oportunidadeSalva.getTitulo(), oportunidadeAtualizada.getTitulo());
        Assertions.assertEquals(oportunidadeSalva.getTitulo(), oportunidadeAtualizada.getTitulo());
        Assertions.assertEquals(oportunidadeSalva.getDescricao(), oportunidadeAtualizada.getDescricao());
        Assertions.assertEquals(oportunidadeSalva.getLocal(), oportunidadeAtualizada.getLocal());
        Assertions.assertEquals(oportunidadeSalva.getDataPublicacao(), oportunidadeAtualizada.getDataPublicacao());
        Assertions.assertEquals(oportunidadeSalva.getDataExpiracao(), oportunidadeAtualizada.getDataExpiracao());
        Assertions.assertEquals(oportunidadeSalva.getSalario(), oportunidadeAtualizada.getSalario());
        Assertions.assertEquals(oportunidadeSalva.getLink(), oportunidadeAtualizada.getLink());
        Assertions.assertEquals(oportunidadeSalva.getStatus(), oportunidadeAtualizada.getStatus());

    }

    @Test
    public void deveRemoverOportunidade() {
        // Cenário
        Usuario usuario = Usuario.builder()
                .email("teste@teste.com")
                .senha("123456")
                .role(UserRole.EGRESSO)
                .build();

        Egresso egresso = Egresso.builder()
                .nome("Egresso")
                .descricao("estudante de ciencia da computacao")
                .usuario(usuario)
                .build();

        Egresso egressoSalvo = egressoRepositorio.save(egresso);

        Oportunidade oportunidade = Oportunidade.builder()
                .egresso(egressoSalvo)
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
        Oportunidade oportunidadeSalva = oportunidadeService.salvarOportunidade(oportunidade);

        //ação
        oportunidadeService.removerOportunidade(oportunidadeSalva);
        Optional<Oportunidade> oportunidadeRemovida = oportunidadeRepositorio.findById(oportunidadeSalva.getId());
        
        // Verificação
        Assertions.assertFalse(oportunidadeRemovida.isPresent());

        // Rollback
        egressoRepositorio.delete(egressoSalvo);
    }

    @Test
    public void deveBuscarPorTitulo() {
        // Cenário
        Usuario usuario = Usuario.builder()
                .email("teste@teste.com")
                .senha("123456")
                .role(UserRole.EGRESSO)
                .build();

        Egresso egresso = Egresso.builder()
                .nome("Egresso")
                .descricao("estudante de ciencia da computacao")
                .usuario(usuario)
                .build();

        Egresso egressoSalvo = egressoRepositorio.save(egresso);

        Oportunidade oportunidade = Oportunidade.builder()
                .egresso(egressoSalvo)
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
        Oportunidade oportunidadeSalva = oportunidadeService.salvarOportunidade(oportunidade);

        //ação
         List<Oportunidade> oportunidades = oportunidadeService.buscarPorTitulo("Desenvolvedor Backend");

        // Rollback
        oportunidadeRepositorio.delete(oportunidadeSalva);
        egressoRepositorio.delete(egressoSalvo);

        // Verificação
        Assertions.assertFalse(oportunidades.isEmpty());
        Assertions.assertTrue(oportunidades.stream().anyMatch(o -> o.getTitulo().equals("Desenvolvedor Backend")));
    }

    @Test
    public void deveListarOportunidades() {
        // Cenário
        Usuario usuario = Usuario.builder()
                .email("teste@teste.com")
                .senha("123456")
                .role(UserRole.EGRESSO)
                .build();

        Egresso egresso = Egresso.builder()
                .nome("Egresso")
                .descricao("estudante de ciencia da computacao")
                .usuario(usuario)
                .build();

        Egresso egressoSalvo = egressoRepositorio.save(egresso);

        Oportunidade oportunidade1 = Oportunidade.builder()
                .egresso(egressoSalvo)
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

            Oportunidade oportunidade2 = Oportunidade.builder()
                .egresso(egressoSalvo)
                .titulo("Desenvolvedor Frontend")
                .descricao("Desenvolvimento em next.js")
                .local("Remoto")
                .tipo("CLT")
                .dataPublicacao(LocalDate.now())
                .dataExpiracao(LocalDate.now().plusDays(30))
                .salario(BigDecimal.valueOf(5000))
                .link("link vaga")
                .status("Ativa")
                .build();
        oportunidadeRepositorio.save(oportunidade1);
        oportunidadeRepositorio.save(oportunidade2);

        //Ação
        List<Oportunidade> oportunidades = oportunidadeService.listarOportunidadesOrdenadasPorData();


        // Rollback
         oportunidadeRepositorio.delete(oportunidade1);
         oportunidadeRepositorio.delete(oportunidade2);
         egressoRepositorio.delete(egressoSalvo);
 
        // Verificação
         Assertions.assertNotNull(oportunidades);
         Assertions.assertFalse(oportunidades.isEmpty());
         Assertions.assertEquals(2, oportunidades.size());
     }




}