package com.portal_egressos.portal_egressos_backend.service;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.portal_egressos.portal_egressos_backend.enums.UserRole;
import com.portal_egressos.portal_egressos_backend.exceptions.RegraNegocioRunTime;
import com.portal_egressos.portal_egressos_backend.models.Cargo;
import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.models.Usuario;
import com.portal_egressos.portal_egressos_backend.repositories.CargoRepository;
import com.portal_egressos.portal_egressos_backend.repositories.EgressoRepository;
import com.portal_egressos.portal_egressos_backend.services.CargoService;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
public class CargoServiceTest {

    @Autowired
    CargoRepository cargoRepositorio;

    @Autowired
    CargoService cargoService;

    @Autowired
    EgressoRepository egressoRepositorio;

    @Test
    @Transactional
    public void deveSalvarCargo() {
        Usuario usuario = Usuario.builder()
                .email("teste@teste.com")
                .senha("123456")
                .role(UserRole.EGRESSO)
                .build();

        Egresso egresso = Egresso.builder()
                .nome("Gabriel Bastos")
                .descricao("estudante de ciencia da computacao")
                .foto("url foto")
                .linkedin("url linkedin")
                .instagram("url instagram")
                .curriculo("curriculo")
                .usuario(usuario)
                .build();

        Egresso egressoSalvo = egressoRepositorio.save(egresso);

        Cargo cargo = Cargo.builder()
                .egresso(egressoSalvo)
                .descricao("Desenvolvedor de Software")
                .local("Empresa X")
                .anoInicio(2020)
                .anoFim(2023)
                .build();

        Cargo cargoSalvo = cargoService.salvarCargo(cargo);

        // verificação
        Assertions.assertNotNull(cargoSalvo);
        Assertions.assertNotNull(cargoSalvo.getId());

        // rollback
        cargoRepositorio.delete(cargoSalvo);
        egressoRepositorio.delete(egressoSalvo);

    }

    @Test
    @Transactional
    public void deveGerarErroAoTentarSalvarCargoNulo() {
        Assertions.assertThrows(RegraNegocioRunTime.class, () -> cargoService.salvarCargo(null), "Cargo inválido.");
    }

    @Test
    @Transactional
    public void deveGerarErroAoTentarSalvarCargoSemDescricao() {
        Cargo cargo = Cargo.builder()
                .local("Empresa X")
                .anoInicio(2020)
                .anoFim(2023)
                .build();

        Assertions.assertThrows(RegraNegocioRunTime.class, () -> cargoService.salvarCargo(cargo),
                "A descrição do cargo deve ser informada.");
    }

    @Test
    @Transactional
    public void deveGerarErroAoTentarSalvarCargoSemLocal() {
        Cargo cargo = Cargo.builder()
                .descricao("Desenvolvedor de Software")
                .anoInicio(2020)
                .anoFim(2023)
                .build();

        Assertions.assertThrows(RegraNegocioRunTime.class, () -> cargoService.salvarCargo(cargo),
                "O local do cargo deve ser informado.");
    }

    @Test
    @Transactional
    public void deveGerarErroAoTentarSalvarCargoSemAnoInicio() {
        Cargo cargo = Cargo.builder()
                .descricao("Desenvolvedor de Software")
                .local("Empresa X")
                .anoFim(2023)
                .build();

        Assertions.assertThrows(RegraNegocioRunTime.class, () -> cargoService.salvarCargo(cargo),
                "O ano de início do cargo deve ser informado.");
    }

    @Test
    @Transactional
    public void deveObterListaCargosPorIdEgresso() {
        Usuario usuario = Usuario.builder()
                .email("teste2@teste.com")
                .senha("123456")
                .role(UserRole.EGRESSO)
                .build();

        Egresso egresso = Egresso.builder()
                .nome("Gabriel Bastos")
                .descricao("Analista de Sistemas")
                .usuario(usuario)
                .build();

        Egresso egressoSalvo = egressoRepositorio.save(egresso);

        Cargo cargo = Cargo.builder()
                .egresso(egressoSalvo)
                .descricao("Analista de Sistemas")
                .local("Empresa Y")
                .anoInicio(2018)
                .anoFim(2022)
                .build();

        Cargo cargoSalvo = cargoRepositorio.save(cargo);

        List<Cargo> cargos = cargoService.listarCargoPorEgressoId(egressoSalvo.getId());
        Assertions.assertFalse(cargos.isEmpty());
        Assertions.assertEquals(1, cargos.size());

        cargoRepositorio.delete(cargoSalvo);
        egressoRepositorio.delete(egressoSalvo);
    }

    @Test
    @Transactional
    public void deveObterListaCargosVaziaQuandoNaoHouverEgressoComId() {
        List<Cargo> cargos = cargoService.listarCargoPorEgressoId(999L);
        Assertions.assertTrue(cargos.isEmpty());
    }

    @Test
    @Transactional
    public void deveAtualizarCargo() {
        Usuario usuario = Usuario.builder()
                .email("teste3@teste.com")
                .senha("123456")
                .role(UserRole.EGRESSO)
                .build();

        Egresso egresso = Egresso.builder()
                .nome("Gabriel Bastos")
                .descricao("Gerente de Projetos")
                .usuario(usuario)
                .build();

        Egresso egressoSalvo = egressoRepositorio.save(egresso);

        Cargo cargo = Cargo.builder()
                .egresso(egressoSalvo)
                .descricao("Gerente de Projetos")
                .local("Empresa Z")
                .anoInicio(2015)
                .anoFim(2020)
                .build();

        Cargo cargoSalvo = cargoRepositorio.save(cargo);

        cargoSalvo.setDescricao("Gerente Sênior de Projetos");
        Cargo cargoAtualizado = cargoService.atualizarCargo(cargoSalvo);

        Assertions.assertNotNull(cargoAtualizado);
        Assertions.assertEquals("Gerente Sênior de Projetos", cargoAtualizado.getDescricao());

        cargoRepositorio.delete(cargoSalvo);
        egressoRepositorio.delete(egressoSalvo);
    }

    @Test
    @Transactional
    public void deveGerarErroAoTentarAtualizarCargoNulo() {
        Assertions.assertThrows(RegraNegocioRunTime.class, () -> cargoService.atualizarCargo(null), "Cargo inválido.");
    }

    @Test
    @Transactional
    public void deveRemoverCargo() {
        Usuario usuario = Usuario.builder()
                .email("teste4@teste.com")
                .senha("123456")
                .role(UserRole.EGRESSO)
                .build();

        Egresso egresso = Egresso.builder()
                .nome("Gabriel Bastos")
                .descricao("Desenvolvedor")
                .usuario(usuario)
                .build();

        Egresso egressoSalvo = egressoRepositorio.save(egresso);

        Cargo cargo = Cargo.builder()
                .egresso(egressoSalvo)
                .descricao("Desenvolvedor")
                .local("Empresa W")
                .anoInicio(2019)
                .anoFim(2023)
                .build();

        Cargo cargoSalvo = cargoRepositorio.save(cargo);
        cargoService.removerCargo(cargoSalvo);

        Assertions.assertFalse(cargoRepositorio.existsById(cargoSalvo.getId()));

        egressoRepositorio.delete(egressoSalvo);
    }

    @Test
    @Transactional
    public void deveGerarErroAoTentarRemoverCargoNulo() {
        Assertions.assertThrows(RegraNegocioRunTime.class, () -> cargoService.removerCargo(null),
                "ID do cargo é inválido.");
    }

}
