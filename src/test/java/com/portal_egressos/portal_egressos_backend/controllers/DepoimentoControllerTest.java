package com.portal_egressos.portal_egressos_backend.controllers;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.portal_egressos.portal_egressos_backend.config.auth.TokenProvider;
import com.portal_egressos.portal_egressos_backend.dto.DepoimentoDto;
import com.portal_egressos.portal_egressos_backend.enums.Status;
import com.portal_egressos.portal_egressos_backend.enums.UserRole;
import com.portal_egressos.portal_egressos_backend.models.Depoimento;
import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.models.Usuario;
import com.portal_egressos.portal_egressos_backend.repositories.DepoimentoRepository;
import com.portal_egressos.portal_egressos_backend.repositories.EgressoRepository;
import com.portal_egressos.portal_egressos_backend.repositories.UsuarioRepository;
import com.portal_egressos.portal_egressos_backend.services.CursoEgressoService;
import com.portal_egressos.portal_egressos_backend.services.DepoimentoService;
import com.portal_egressos.portal_egressos_backend.services.EgressoService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = DepoimentoController.class)
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class) 
public class DepoimentoControllerTest {

        static final String API = "/api/depoimento";

        @Autowired
        MockMvc mvc;

        @MockBean
        private CursoEgressoService cursoEgressoService;
        
        @MockBean
        DepoimentoService depoimentoService;

        @MockBean
        EgressoService egressoService;

        @MockBean
        private TokenProvider tokenProvider;

        @MockBean
        private UsuarioRepository userRepository;

        @MockBean
        private DepoimentoRepository depoimentoRepository;

        @MockBean
        private EgressoRepository egressoRepository;

        @Test
        public void deveSalvarDepoimento() throws Exception {
                // Cenário
                Usuario usuario = Usuario.builder()
                                .email("teste@teste.com")
                                .senha("senha123")
                                .role(UserRole.EGRESSO)
                                .build();

                Egresso egresso = Egresso.builder()
                                .id(1L)
                                .nome("Egresso Teste")
                                .descricao("Descrição do Egresso")
                                .foto("urlFoto")
                                .linkedin("https://linkedin.com/in/egresso")
                                .instagram("https://instagram.com/egresso")
                                .curriculo("Currículo do Egresso")
                                .usuario(usuario)
                                .status(Status.PENDENTE)
                                .build();

                DepoimentoDto depoimentoDTO = DepoimentoDto.builder()
                                .id(null)
                                .texto("Depoimento Teste")
                                .data(null)
                                .status(null)
                                .idEgresso(egresso.getId())
                                .nomeEgresso(null)
                                .build();

                Depoimento depoimento = Depoimento.builder()
                                .id(1L)
                                .texto(depoimentoDTO.getTexto())
                                .data(LocalDate.now())
                                .status(Status.PENDENTE)
                                .egresso(egresso)
                                .build();

                Mockito.when(egressoService.buscarEgresso(Mockito.any(Egresso.class)))
                                .thenReturn(List.of(egresso));

                Mockito.when(depoimentoService.salvarDepoimento(Mockito.any(Depoimento.class)))
                                .thenReturn(depoimento);

                String json = new ObjectMapper()
                                .registerModule(new JavaTimeModule())
                                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                                .writeValueAsString(depoimentoDTO);

                // Ação
                MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API + "/salvar")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(json);

                // Verificação
                mvc.perform(request)
                                .andExpect(MockMvcResultMatchers.status().isCreated());
        }

        @Test
        public void deveAtualizarDepoimento() throws Exception {
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

                // Depoimento atualizado
                Depoimento depoimentoAtualizado = Depoimento.builder()
                                .id(1L)
                                .texto("Texto Atualizado")
                                .data(LocalDate.now())
                                .status(Status.APROVADO)
                                .egresso(egressoSalvo)
                                .build();

                DepoimentoDto depoimentoDTO = DepoimentoDto.builder()
                                .texto("Texto Atualizado")
                                .status(Status.APROVADO)
                                .data(LocalDate.now())
                                .idEgresso(egressoSalvo.getId()) 
                                .build();

                Mockito.when(egressoService.buscarEgresso(Mockito.any(Egresso.class)))
                                .thenReturn(Collections.singletonList(egressoSalvo));

                Mockito.when(depoimentoService.atualizarDepoimento(Mockito.any(Depoimento.class)))
                                .thenReturn(depoimentoAtualizado);

                String json = new ObjectMapper()
                                .registerModule(new JavaTimeModule())
                                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                                .writeValueAsString(depoimentoDTO);

                // ação
                MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(API + "/atualizar/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json);

                // Verificando
                mvc.perform(request)
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.descricao").value("Texto Atualizado"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(Status.APROVADO.toString()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(LocalDate.now().toString()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.idEgresso").value(egressoSalvo.getId()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.nomeEgresso")
                                                .value(egressoSalvo.getNome()));
        }

        @Test
        void deveAtualizarStatusDepoimento() throws Exception {
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

                Long idDepoimento = 1L;

                Status novoStatus = Status.APROVADO;

                Depoimento depoimentoAtualizado = Depoimento.builder()
                                .id(idDepoimento)
                                .texto("Depoimento Original")
                                .status(novoStatus)
                                .egresso(egressoSalvo)
                                .build();

                Mockito.when(depoimentoService.atualizarStatusDepoimento(Mockito.any(Depoimento.class)))
                                .thenReturn(depoimentoAtualizado);

                // Ação
                MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                                .put(API + "/status/" + idDepoimento)
                                .accept(MediaType.APPLICATION_JSON);

                // Verificação
                mvc.perform(request)
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(idDepoimento))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(novoStatus.toString()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.descricao").value("Depoimento Original"));
        }

        @Test
        void deveListarDepoimentos() throws Exception {
                // Cenário
                Egresso egresso1 = Egresso.builder()
                                .id(1L)
                                .nome("Egresso Teste 1")
                                .build();

                Egresso egresso2 = Egresso.builder()
                                .id(2L)
                                .nome("Egresso Teste 2")
                                .build();

                Depoimento depoimento1 = Depoimento.builder()
                                .id(1L)
                                .texto("Depoimento Teste 1")
                                .status(Status.APROVADO)
                                .egresso(egresso1)
                                .build();

                Depoimento depoimento2 = Depoimento.builder()
                                .id(2L)
                                .texto("Depoimento Teste 2")
                                .status(Status.PENDENTE)
                                .egresso(egresso2)
                                .build();

                List<Depoimento> depoimentosMock = Arrays.asList(depoimento1, depoimento2);

                Mockito.when(depoimentoService.listarDepoimentos()).thenReturn(depoimentosMock);

                // Ação
                MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(API + "/listar")
                                .accept(MediaType.APPLICATION_JSON);

                // Verificação
                mvc.perform(request)
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].descricao").value("Depoimento Teste 1"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status")
                                                .value(Status.APROVADO.toString()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].idEgresso").value(1))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nomeEgresso").value("Egresso Teste 1"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].descricao").value("Depoimento Teste 2"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].status")
                                                .value(Status.PENDENTE.toString()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].idEgresso").value(2))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].nomeEgresso").value("Egresso Teste 2"));
        }

        @Test
        void deveListarDepoimentosAprovados() throws Exception {
                // Cenário
                Egresso egresso1 = Egresso.builder()
                                .id(1L)
                                .nome("Egresso Teste 1")
                                .build();

                Depoimento depoimento1 = Depoimento.builder()
                                .id(1L)
                                .texto("Depoimento Teste 1")
                                .status(Status.APROVADO)
                                .egresso(egresso1)
                                .build();

                Egresso egresso2 = Egresso.builder()
                                .id(2L)
                                .nome("Egresso Teste 2")
                                .build();

                Depoimento depoimento2 = Depoimento.builder()
                                .id(2L)
                                .texto("Depoimento Teste 2")
                                .status(Status.APROVADO)
                                .egresso(egresso2)
                                .build();

                List<Depoimento> depoimentosMock = Arrays.asList(depoimento1, depoimento2);

                Mockito.when(depoimentoService.listarDepoimentosAprovados()).thenReturn(depoimentosMock);

                // Ação
                MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(API + "/aprovados")
                                .accept(MediaType.APPLICATION_JSON);

                // Verificação
                mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].descricao").value("Depoimento Teste 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status")
                                .value(Status.APROVADO.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].idEgresso").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nomeEgresso").value("Egresso Teste 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].descricao").value("Depoimento Teste 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].status")
                                .value(Status.APROVADO.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].idEgresso").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].nomeEgresso").value("Egresso Teste 2"));
        }

        @Test
        void deveRemoverDepoimento() throws Exception {

                Mockito.doNothing().when(depoimentoService).removerDepoimento(1L);

                // Ação
                MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                                .delete(API + "/remover/1")
                                .accept(MediaType.APPLICATION_JSON);

                // Verificação
                mvc.perform(request)
                                .andExpect(MockMvcResultMatchers.status().isNoContent());

        }

}
