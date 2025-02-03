package com.portal_egressos.portal_egressos_backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.portal_egressos.portal_egressos_backend.config.auth.TokenProvider;
import com.portal_egressos.portal_egressos_backend.dto.OportunidadeDTO;
import com.portal_egressos.portal_egressos_backend.enums.Status;
import com.portal_egressos.portal_egressos_backend.models.Oportunidade;
import com.portal_egressos.portal_egressos_backend.repositories.EgressoRepository;
import com.portal_egressos.portal_egressos_backend.repositories.OportunidadeRepository;
import com.portal_egressos.portal_egressos_backend.repositories.UsuarioRepository;
import com.portal_egressos.portal_egressos_backend.services.EgressoService;
import com.portal_egressos.portal_egressos_backend.services.OportunidadeService;
import com.portal_egressos.portal_egressos_backend.models.Usuario;
import com.portal_egressos.portal_egressos_backend.repositories.UsuarioRepository;
import com.portal_egressos.portal_egressos_backend.enums.UserRole;
import com.portal_egressos.portal_egressos_backend.models.Egresso;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = OportunidadeController.class)
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
public class OportunidadeControllerTest {

        static final String API = "/api/oportunidade";

        @Autowired
        MockMvc mvc;

        @MockBean
        OportunidadeService oportunidadeService;

        @MockBean
        EgressoService egressoService;

        @MockBean
        private TokenProvider tokenProvider;

        @MockBean
        private UsuarioRepository usuarioRepository;

        @MockBean
        private OportunidadeRepository oportunidadeRepository;

        @MockBean
        private EgressoRepository egressoRepository;

        @Test
        public void deveSalvarOportunidade() throws Exception {
                // Cenário
                Usuario usuario = Usuario.builder()
                                .email("teste@teste.com")
                                .senha("senha123")
                                .role(UserRole.EGRESSO)
                                .build();

                Egresso egresso = Egresso.builder()
                                .id(1L)
                                .nome("Egresso")
                                .descricao("Descrição do Egresso")
                                .foto("urlFoto")
                                .linkedin("https://linkedin.com/in/egresso")
                                .instagram("https://instagram.com/egresso")
                                .curriculo("Currículo do Egresso")
                                .usuario(usuario)
                                .status(Status.PENDENTE)
                                .build();

                OportunidadeDTO oportunidadedto = OportunidadeDTO.builder()
                                .titulo("Nova Oportunidade")
                                .descricao("Descrição da oportunidade")
                                .local("Lcalização")
                                .tipo("Tipo da oportunidade")
                                .dataPublicacao(LocalDate.now())
                                .dataExpiracao(LocalDate.now().plusDays(30))
                                .salario(new BigDecimal("5000"))
                                .link("linkOportunidade.com")
                                .idEgresso(1L)
                                .nomeEgresso(null)
                                .build();

                Oportunidade oportunidade = Oportunidade.builder()
                                .id(1L)
                                .titulo(oportunidadedto.getTitulo())
                                .descricao(oportunidadedto.getDescricao())
                                .local(oportunidadedto.getLocal())
                                .tipo(oportunidadedto.getTipo())
                                .dataPublicacao(oportunidadedto.getDataPublicacao())
                                .dataExpiracao(oportunidadedto.getDataExpiracao())
                                .salario(oportunidadedto.getSalario())
                                .link(oportunidadedto.getLink())
                                .status(Status.PENDENTE)
                                .egresso(egresso)
                                .build();

                Mockito.when(egressoService.buscarEgresso(Mockito.any(Egresso.class)))
                                .thenReturn(List.of(egresso));

                Mockito.when(oportunidadeService.salvarOportunidade(Mockito.any(Oportunidade.class)))
                                .thenReturn(oportunidade);

                String json = new ObjectMapper()
                                .registerModule(new JavaTimeModule())
                                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                                .writeValueAsString(oportunidadedto);

                // Ação
                MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/oportunidade/salvar")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(json);

                // Verificação
                mvc.perform(request)
                                .andExpect(MockMvcResultMatchers.status().isCreated());
        }

        @Test
        public void deveAtualizarOportunidades() throws Exception {
                // Cenário
                Usuario usuario = Usuario.builder()
                                .email("teste@teste.com")
                                .senha("senha123")
                                .role(UserRole.EGRESSO)
                                .build();

                Egresso egresso = Egresso.builder()
                                .id(1L)
                                .nome("Egresso")
                                .descricao("Descrição do Egresso")
                                .foto("urlFoto")
                                .linkedin("https://linkedin.com/in/egresso")
                                .instagram("https://instagram.com/egresso")
                                .curriculo("Currículo do Egresso")
                                .usuario(usuario)
                                .status(Status.PENDENTE)
                                .build();

                Mockito.when(egressoRepository.save(Mockito.any(Egresso.class))).thenReturn(egresso);
                Egresso egressoSalvo = egressoRepository.save(egresso);

                Oportunidade oportunidadeAtualizada = Oportunidade.builder()
                                .id(1L)
                                .titulo("Oportunidade Atualizada")
                                .descricao("Descrição da oportunidade Atualizada")
                                .local("Localização")
                                .tipo("Tipo da oportunidade")
                                .dataPublicacao(LocalDate.now())
                                .dataExpiracao(LocalDate.now().plusDays(30))
                                .salario(new BigDecimal("7000"))
                                .link("linkOportunidade.com")
                                .status(Status.APROVADO)
                                .egresso(egressoSalvo)
                                .build();

                // Dto do depoimento a ser atualizado
                OportunidadeDTO oportunidadedto = OportunidadeDTO.builder()
                                .titulo("Oportunidade Atualizada")
                                .descricao("Descrição da oportunidade Atualizada")
                                .local("Localização")
                                .tipo("Tipo da oportunidade")
                                .dataPublicacao(LocalDate.now())
                                .dataExpiracao(LocalDate.now().plusDays(30))
                                .salario(new BigDecimal("7000"))
                                .link("linkOportunidade.com")
                                .idEgresso(egressoSalvo.getId())
                                .build();

                Mockito.when(egressoService.buscarEgresso(Mockito.any(Egresso.class)))
                                .thenReturn(Collections.singletonList(egressoSalvo));

                Mockito.when(oportunidadeService.atualizarOportunidade(Mockito.any(Oportunidade.class)))
                                .thenReturn(oportunidadeAtualizada);

                String json = new ObjectMapper()
                                .registerModule(new JavaTimeModule())
                                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                                .writeValueAsString(oportunidadedto);

                // Ação
                MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(API + "/atualizar/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json);

                // Verificação
                mvc.perform(request)
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.titulo").value("Oportunidade Atualizada"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.descricao")
                                                .value("Descrição da oportunidade Atualizada"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.local").value("Localização"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.tipo").value("Tipo da oportunidade"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.dataPublicacao")
                                                .value(LocalDate.now().toString()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.dataExpiracao")
                                                .value(LocalDate.now().plusDays(30).toString()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.salario").value(7000))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.link").value("linkOportunidade.com"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(Status.APROVADO.toString()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.idEgresso").value(egressoSalvo.getId()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.nomeEgresso")
                                                .value(egressoSalvo.getNome()));
        }

        @Test
        public void deveAtualizarStatusOportunidade() throws Exception {
                // Cenário
                Usuario usuario = Usuario.builder()
                                .email("teste@teste.com")
                                .senha("senha123")
                                .role(UserRole.EGRESSO)
                                .build();

                Egresso egresso = Egresso.builder()
                                .id(1L)
                                .nome("Egresso Teste")
                                .descricao("Estudante de Ciência da Computação")
                                .usuario(usuario)
                                .status(Status.PENDENTE)
                                .build();

                Mockito.when(egressoRepository.save(Mockito.any(Egresso.class))).thenReturn(egresso);
                Egresso egressoSalvo = egressoRepository.save(egresso);

                Long idOportunidade = 1L;
                Status novoStatus = Status.APROVADO;

                Oportunidade oportunidadeStatusAtaulizado = Oportunidade.builder()
                                .id(idOportunidade)
                                .titulo("Oportunidade Atualizada")
                                .descricao("Descrição da oportunidade Atualizada")
                                .local("Localização")
                                .tipo("Tipo da oportunidade")
                                .dataPublicacao(LocalDate.now())
                                .status(novoStatus)
                                .egresso(egressoSalvo)
                                .build();

                Mockito.when(oportunidadeService.atualizarStatusOportunidade(Mockito.any(Oportunidade.class)))
                                .thenReturn(oportunidadeStatusAtaulizado);

                // Ação
                MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                                .put(API + "/status/" + idOportunidade)
                                .accept(MediaType.APPLICATION_JSON);

                // Verificação
                mvc.perform(request)
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(idOportunidade))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(novoStatus.toString()));
        }

        @Test
        public void deveListarOportunidades() throws Exception {
                // Cenário
                Egresso egresso = Egresso.builder()
                                .id(1L)
                                .nome("Egresso Teste")
                                .build();

                Oportunidade oportunidade1 = Oportunidade.builder()
                                .id(1L)
                                .titulo("Oportunidade 1")
                                .descricao("Descrição da oportunidade")
                                .local("Localização")
                                .tipo("Tipo da oportunidade")
                                .dataPublicacao(LocalDate.now())
                                .dataExpiracao(LocalDate.now().plusDays(30))
                                .salario(new BigDecimal("7000"))
                                .link("linkOportunidade.com")
                                .status(Status.PENDENTE)
                                .egresso(egresso)
                                .build();

                Oportunidade oportunidade2 = Oportunidade.builder()
                                .id(2L)
                                .titulo("Oportunidade 2")
                                .descricao("Descrição da oportunidade")
                                .local("Localização")
                                .tipo("Tipo da oportunidade")
                                .dataPublicacao(LocalDate.now())
                                .dataExpiracao(LocalDate.now().plusDays(30))
                                .salario(new BigDecimal("7000"))
                                .link("linkOportunidade.com")
                                .status(Status.APROVADO)
                                .egresso(egresso)
                                .build();

                List<Oportunidade> oportunidadesMock = Arrays.asList(oportunidade1, oportunidade2);
                Mockito.when(oportunidadeService.listarTodasOportunidadesOrdenadasPorData())
                                .thenReturn(oportunidadesMock);

                // Ação
                MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                                .get(API + "/listar")
                                .accept(MediaType.APPLICATION_JSON);

                // Verificação
                mvc.perform(request)
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].idEgresso").value(1))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nomeEgresso").value("Egresso Teste"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].titulo").value("Oportunidade 1"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].descricao")
                                                .value("Descrição da oportunidade"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].local").value("Localização"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].tipo").value("Tipo da oportunidade"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].dataPublicacao")
                                                .value(LocalDate.now().toString()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].dataExpiracao")
                                                .value(LocalDate.now().plusDays(30).toString()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].salario").value(7000))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].link").value("linkOportunidade.com"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status")
                                                .value(Status.PENDENTE.toString()))

                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].titulo").value("Oportunidade 2"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].descricao")
                                                .value("Descrição da oportunidade"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].local").value("Localização"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].tipo").value("Tipo da oportunidade"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].dataPublicacao")
                                                .value(LocalDate.now().toString()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].dataExpiracao")
                                                .value(LocalDate.now().plusDays(30).toString()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].salario").value(7000))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].link").value("linkOportunidade.com"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].status")
                                                .value(Status.APROVADO.toString()));

        }

        @Test
        public void deveListarOportunidadesAprovadas() throws Exception {
                // Cenário
                Egresso egresso = Egresso.builder()
                                .id(1L)
                                .nome("Egresso Teste")
                                .build();

                Oportunidade oportunidade1 = Oportunidade.builder()
                                .id(1L)
                                .titulo("Oportunidade 1")
                                .descricao("Descrição da oportunidade")
                                .local("Localização")
                                .tipo("Tipo da oportunidade")
                                .dataPublicacao(LocalDate.now())
                                .dataExpiracao(LocalDate.now().plusDays(30))
                                .salario(new BigDecimal("7000"))
                                .link("linkOportunidade.com")
                                .status(Status.APROVADO)
                                .egresso(egresso)
                                .build();

                Oportunidade oportunidade2 = Oportunidade.builder()
                                .id(2L)
                                .titulo("Oportunidade 2")
                                .descricao("Descrição da oportunidade")
                                .local("Localização")
                                .tipo("Tipo da oportunidade")
                                .dataPublicacao(LocalDate.now())
                                .dataExpiracao(LocalDate.now().plusDays(30))
                                .salario(new BigDecimal("7000"))
                                .link("linkOportunidade.com")
                                .status(Status.APROVADO)
                                .egresso(egresso)
                                .build();

                List<Oportunidade> oportunidadesMock = Arrays.asList(oportunidade1, oportunidade2);
                Mockito.when(oportunidadeService.listarOportunidadesAprovadasOrdenadasPorData())
                                .thenReturn(oportunidadesMock);

                // Ação
                MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                                .get(API + "/aprovadas")
                                .accept(MediaType.APPLICATION_JSON);

                // Verificação
                mvc.perform(request)
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].idEgresso").value(1))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nomeEgresso").value("Egresso Teste"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].titulo").value("Oportunidade 1"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].descricao")
                                                .value("Descrição da oportunidade"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].local").value("Localização"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].tipo").value("Tipo da oportunidade"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].dataPublicacao")
                                                .value(LocalDate.now().toString()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].dataExpiracao")
                                                .value(LocalDate.now().plusDays(30).toString()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].salario").value(7000))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].link").value("linkOportunidade.com"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status")
                                                .value(Status.APROVADO.toString()))

                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].titulo").value("Oportunidade 2"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].descricao")
                                                .value("Descrição da oportunidade"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].local").value("Localização"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].tipo").value("Tipo da oportunidade"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].dataPublicacao")
                                                .value(LocalDate.now().toString()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].dataExpiracao")
                                                .value(LocalDate.now().plusDays(30).toString()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].salario").value(7000))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].link").value("linkOportunidade.com"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].status")
                                                .value(Status.APROVADO.toString()));

        }

        @Test
        public void deveRemoverOportunidade() throws Exception {
                // Cenário
                Mockito.doNothing().when(oportunidadeService).removerOportunidade(Mockito.any(Oportunidade.class));

                // Ação
                MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                                .delete(API + "/remover/1")
                                .accept(MediaType.APPLICATION_JSON);

                // Verificação
                mvc.perform(request)
                                .andExpect(MockMvcResultMatchers.status().isNoContent());
        }

}
