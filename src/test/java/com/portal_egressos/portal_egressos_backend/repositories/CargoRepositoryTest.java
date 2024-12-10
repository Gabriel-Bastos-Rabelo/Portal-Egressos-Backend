package com.portal_egressos.portal_egressos_backend.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.portal_egressos.portal_egressos_backend.models.Cargo;
import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.models.Usuario;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
public class CargoRepositoryTest {

    @Autowired
    CargoRepository cargoRepository;

    @Autowired
    EgressoRepository egressoRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Test
    public void deveVerificarSalvarCargo() {
        //cenário
        Usuario usuario = Usuario.builder()
                .email("teste@teste.com")
                .senha("123456")
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

        Egresso egressoSalvo = egressoRepository.save(egresso);

        Cargo cargo = Cargo.builder()
                .egresso(egressoSalvo)
                .descricao("Desenvolvedor de Software")
                .local("Empresa X")
                .anoInicio(2020)
                .anoFim(2023)
                .build();

        //ação
        Cargo cargoSalvo = cargoRepository.save(cargo);

        //verificação
        Assertions.assertNotNull(cargoSalvo);
        Assertions.assertNotNull(cargoSalvo.getId());
        Assertions.assertEquals(cargoSalvo.getEgresso(), egresso);
        Assertions.assertEquals("Desenvolvedor de Software", cargoSalvo.getDescricao());
        Assertions.assertEquals("Empresa X", cargoSalvo.getLocal());
        Assertions.assertEquals(2020, cargoSalvo.getAnoInicio().intValue());
        Assertions.assertEquals(2023, cargoSalvo.getAnoFim().intValue());


    }

    @Test
    public void deveVerificarLeituraCargo() {
        //cenário
        Usuario usuario = Usuario.builder()
                .email("teste@teste.com")
                .senha("123456")
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

        Egresso egressoSalvo = egressoRepository.save(egresso);

        Cargo cargo = Cargo.builder()
                .egresso(egressoSalvo)
                .descricao("Desenvolvedor de Software")
                .local("Empresa X")
                .anoInicio(2020)
                .anoFim(2023)
                .build();

        cargoRepository.save(cargo);

        //ação
        Optional<Cargo> cargoLidoOpt = cargoRepository.findById(cargo.getId());

        if (cargoLidoOpt.isEmpty()) {
            throw new IllegalArgumentException("Cargo não encontrado");
        }

        Cargo cargoLido = cargoLidoOpt.get();

        //verificação
        Assertions.assertNotNull(cargoLido);
        Assertions.assertEquals("Desenvolvedor de Software", cargoLido.getDescricao());
        Assertions.assertEquals("Empresa X", cargoLido.getLocal());
        Assertions.assertEquals(2020, cargoLido.getAnoInicio().intValue());
        Assertions.assertEquals(2023, cargoLido.getAnoFim().intValue());

    }

    @Test
    public void deveVerificarAtualizarCargo() {
        // cenário
        Usuario usuario = Usuario.builder()
                .email("teste@teste.com")
                .senha("123456")
                .build();

        Egresso egresso = Egresso.builder()
                .nome("Gabriel Bastos")
                .descricao("estundante de ciencia da computacao")
                .foto("url foto")
                .linkedin("url linkedin")
                .instagram("url instagram")
                .curriculo("curriculo")
                .usuario(usuario)
                .build();
        Egresso egressoSalvo = egressoRepository.save(egresso);

        Cargo cargo = Cargo.builder()
                .egresso(egressoSalvo)
                .descricao("Desenvolvedor de Software")
                .local("Empresa X")
                .anoInicio(2020)
                .anoFim(2023)
                .build();

        Cargo cargoSalvo = cargoRepository.save(cargo);

        // ação
        cargoSalvo.setDescricao("Arquiteto de Software");
        cargoSalvo.setLocal("Empresa Y");
        cargoSalvo.setAnoInicio(2019);
        cargoSalvo.setAnoFim(2024);

        Cargo cargoAtualizado = cargoRepository.save(cargoSalvo);

        // Verificação
        Assertions.assertNotNull(cargoAtualizado);
        Assertions.assertEquals(cargoSalvo.getId(), cargoAtualizado.getId());
        Assertions.assertEquals("Arquiteto de Software", cargoAtualizado.getDescricao());
        Assertions.assertEquals("Empresa Y", cargoAtualizado.getLocal());
        Assertions.assertEquals(2019, cargoAtualizado.getAnoInicio().intValue());
        Assertions.assertEquals(2024, cargoAtualizado.getAnoFim().intValue());
    }


    @Test
    public void deveVerificarRemoverCargo() {
        //cenário
        Usuario usuario = Usuario.builder()
                .email("teste@teste.com")
                .senha("123456")
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

        Egresso egressoSalvo = egressoRepository.save(egresso);

        Cargo cargo = Cargo.builder()
                .egresso(egressoSalvo)
                .descricao("Desenvolvedor de Software")
                .local("Empresa X")
                .anoInicio(2020)
                .anoFim(2023)
                .build();

        
        //ação
        Cargo cargoSalvo = cargoRepository.save(cargo);
        Long id = cargoSalvo.getId();
        cargoRepository.deleteById(id);
        //verificação
        Optional<Cargo> temp = cargoRepository.findById(id);
        Assertions.assertFalse(temp.isPresent());
        
    }
}
