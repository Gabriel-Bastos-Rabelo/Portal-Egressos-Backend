package com.portal_egressos.portal_egressos_backend.controllers;

import java.util.Arrays;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.portal_egressos.portal_egressos_backend.config.auth.TokenProvider;
import com.portal_egressos.portal_egressos_backend.dto.EgressoDTO;
import com.portal_egressos.portal_egressos_backend.enums.Status;
import com.portal_egressos.portal_egressos_backend.enums.UserRole;
import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.models.Usuario;
import com.portal_egressos.portal_egressos_backend.repositories.EgressoRepository;
import com.portal_egressos.portal_egressos_backend.repositories.UsuarioRepository;
import com.portal_egressos.portal_egressos_backend.services.CursoEgressoService;
import com.portal_egressos.portal_egressos_backend.services.CursoService;
import com.portal_egressos.portal_egressos_backend.services.EgressoService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@WebMvcTest(controllers = EgressoController.class)
@AutoConfigureMockMvc
public class EgressoControllerTest {
        static final String API = "/api/egresso";

        @Autowired
        private MockMvc mvc;

        @MockBean
        private EgressoService egressoService;

        @MockBean
        private CursoService cursoService;

        @MockBean
        private CursoEgressoService cursoEgressoService;

        @MockBean
        private UsuarioRepository userRepository;

        @MockBean
        private EgressoRepository egressoRepositorio;

        @MockBean
        private TokenProvider tokenProvider;

        @Test
        public void deveSalvarUsuario() throws Exception {
                // cenário
                // dto para virar json
                EgressoDTO egressoDTO = EgressoDTO.builder()
                                .nome("Anderson Lopes")
                                .descricao("Cientista da Computação.")
                                .foto("https://example.com/foto2.jpg")
                                .linkedin("https://www.linkedin.com/in/anderson-lopes")
                                .instagram("https://www.instagram.com/anderson_silva")
                                .curriculo("https://example.com/anderson.pdf")
                                .status(Status.PENDENTE)
                                .emailUsuario("anderson@example.com")
                                .idCurso(600L)
                                .anoInicio(2022)
                                .anoFim(2025)
                                .build();

                Usuario usuario = Usuario.builder()
                                .email("anderson@example.com")
                                .senha("senha123")
                                .role(UserRole.EGRESSO)
                                .build();

                Egresso egresso = Egresso.builder()
                                .id(11L)
                                .nome("Anderson Lopes")
                                .descricao("Cientista da Computação.")
                                .foto("https://example.com/foto2.jpg")
                                .linkedin("https://www.linkedin.com/in/anderson-lopes")
                                .instagram("https://www.instagram.com/anderson_silva")
                                .curriculo("https://example.com/anderson.pdf")
                                .status(Status.PENDENTE)
                                .usuario(usuario)
                                .build();

                // Simulações
                Mockito.when(egressoService.salvarEgresso(Mockito.any(Egresso.class))).thenReturn(egresso);
                String egressoJson = new ObjectMapper().writeValueAsString(egressoDTO);

                // ação: contrói requisição post
                MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API + "/salvar") // na URI
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(egressoJson); // mandando o DTO

                // ação e verificação
                mvc.perform(request).andExpect(MockMvcResultMatchers.status().isCreated());
        }

        @Test
        public void deveRemoverEgresso() throws Exception {
                // Configuração dos Mocks
                Mockito.doNothing().when(egressoService).removerEgresso(1L);

                // Ação
                MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(API + "/deletar/1")
                                .accept(MediaType.APPLICATION_JSON);

                // Verificação
                mvc.perform(request)
                                .andExpect(MockMvcResultMatchers.status().isNoContent()); // Verifica o status 204
        }

        @Test
        public void deveListarEgressos() throws Exception {
                Usuario usuario1 = Usuario.builder()
                                .email("anderson@example.com")
                                .senha("senha123")
                                .role(UserRole.EGRESSO)
                                .build();

                Usuario usuario2 = Usuario.builder()
                                .email("anderson2@example.com")
                                .senha("senha123")
                                .role(UserRole.EGRESSO)
                                .build();

                Egresso egresso1 = Egresso.builder()
                                .id(1L)
                                .nome("Anderson Lopes")
                                .descricao("Cientista da Computação.")
                                .foto("https://example.com/foto2.jpg")
                                .linkedin("https://www.linkedin.com/in/anderson-lopes")
                                .instagram("https://www.instagram.com/anderson_silva")
                                .curriculo("https://example.com/anderson.pdf")
                                .status(Status.PENDENTE)
                                .usuario(usuario1)
                                .build();

                Egresso egresso2 = Egresso.builder()
                                .id(2L)
                                .nome("Anderson Silva")
                                .descricao("Cientista da Computação.")
                                .foto("https://example.com/foto2.jpg")
                                .linkedin("https://www.linkedin.com/in/anderson-lopes")
                                .instagram("https://www.instagram.com/anderson_silva")
                                .curriculo("https://example.com/anderson.pdf")
                                .status(Status.PENDENTE)
                                .usuario(usuario2)
                                .build();

                List<Egresso> egressosMock = Arrays.asList(egresso1, egresso2);

                Mockito.when(egressoService.listarEgressos()).thenReturn(egressosMock);

                // Ação
                MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(API + "/listar")
                                .accept(MediaType.APPLICATION_JSON);

                // Verificação
                mvc.perform(request)
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nome").value("Anderson Lopes"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].descricao")
                                                .value("Cientista da Computação."))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status")
                                                .value(Status.PENDENTE.toString()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].foto")
                                                .value("https://example.com/foto2.jpg"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].instagram")
                                                .value("https://www.instagram.com/anderson_silva"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].linkedin")
                                                .value("https://www.linkedin.com/in/anderson-lopes"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].curriculo")
                                                .value("https://example.com/anderson.pdf"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].nome").value("Anderson Silva"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].descricao")
                                                .value("Cientista da Computação."))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].status")
                                                .value(Status.PENDENTE.toString()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].foto")
                                                .value("https://example.com/foto2.jpg"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].instagram")
                                                .value("https://www.instagram.com/anderson_silva"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].linkedin")
                                                .value("https://www.linkedin.com/in/anderson-lopes"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].curriculo")
                                                .value("https://example.com/anderson.pdf"));

        }

        @Test
        public void deveBuscarEgressosAprovados() throws Exception {
                Usuario usuario = Usuario.builder()
                                .email("anderson@example.com")
                                .senha("senha123")
                                .role(UserRole.EGRESSO)
                                .build();

                Egresso egresso = Egresso.builder()
                                .id(1L)
                                .nome("Anderson Lopes")
                                .descricao("Cientista da Computação.")
                                .foto("https://example.com/foto2.jpg")
                                .linkedin("https://www.linkedin.com/in/anderson-lopes")
                                .instagram("https://www.instagram.com/anderson_silva")
                                .curriculo("https://example.com/anderson.pdf")
                                .status(Status.APROVADO)
                                .usuario(usuario)
                                .build();

                List<Egresso> egressosMock = Arrays.asList(egresso);

                Mockito.when(egressoService.listarEgressosAprovados()).thenReturn(egressosMock);

                // Ação
                MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(API + "/buscarAprovados")
                                .accept(MediaType.APPLICATION_JSON);

                // Verificação
                mvc.perform(request)
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nome").value("Anderson Lopes"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].descricao")
                                                .value("Cientista da Computação."))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status")
                                                .value(Status.APROVADO.toString()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].foto")
                                                .value("https://example.com/foto2.jpg"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].instagram")
                                                .value("https://www.instagram.com/anderson_silva"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].linkedin")
                                                .value("https://www.linkedin.com/in/anderson-lopes"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].curriculo")
                                                .value("https://example.com/anderson.pdf"));
        }

        @Test
        public void deveBuscarEgressosPorNome() throws Exception {
                Usuario usuario = Usuario.builder()
                                .email("anderson@example.com")
                                .senha("senha123")
                                .role(UserRole.EGRESSO)
                                .build();

                Egresso egresso = Egresso.builder()
                                .id(1L)
                                .nome("Anderson Lopes")
                                .descricao("Cientista da Computação.")
                                .foto("https://example.com/foto2.jpg")
                                .linkedin("https://www.linkedin.com/in/anderson-lopes")
                                .instagram("https://www.instagram.com/anderson_silva")
                                .curriculo("https://example.com/anderson.pdf")
                                .status(Status.APROVADO)
                                .usuario(usuario)
                                .build();

                List<Egresso> egressosMock = Arrays.asList(egresso);

                Mockito.when(egressoService.buscarEgresso("Anderson"))
                                .thenReturn(egressosMock);

                MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(API + "/buscarPorNome")
                                .param("nome", "Anderson")
                                .accept(MediaType.APPLICATION_JSON);

                mvc.perform(request)
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nome").value("Anderson Lopes"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].descricao")
                                                .value("Cientista da Computação."))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status")
                                                .value(Status.APROVADO.toString()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].foto")
                                                .value("https://example.com/foto2.jpg"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].instagram")
                                                .value("https://www.instagram.com/anderson_silva"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].linkedin")
                                                .value("https://www.linkedin.com/in/anderson-lopes"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].curriculo")
                                                .value("https://example.com/anderson.pdf"));
        }

        @Test
        public void deveAtualizarEgresso() throws Exception {
                // cenario
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

                EgressoDTO egressoDTO = EgressoDTO.builder()
                                .id(1L)
                                .nome("Anderson Silva")
                                .descricao("Egresso Descrição")
                                .foto("https://example.com/foto2.jpg")
                                .instagram("https://instagram.com/egresso")
                                .linkedin("https://linkedin.com/in/egresso")
                                .status(Status.APROVADO)
                                .emailUsuario("teste@teste.com")
                                .build();

                Usuario usuario = Usuario.builder()
                                .email("teste@teste.com")
                                .senha("senha123")
                                .role(UserRole.EGRESSO)
                                .build();

                Egresso egresso = Egresso.builder()
                                .id(egressoDTO.getId())
                                .nome("Anderson Lopes")
                                .descricao("Cientista da Computação.")
                                .foto("https://example.com/foto2.jpg")
                                .status(Status.APROVADO)
                                .usuario(usuario)
                                .build();

                Egresso egressoAtualizado = Egresso.builder()
                                .id(1L)
                                .nome(egressoDTO.getNome())
                                .descricao(egressoDTO.getDescricao())
                                .foto(egressoDTO.getFoto())
                                .instagram(egressoDTO.getInstagram())
                                .linkedin(egressoDTO.getLinkedin())
                                .status(egressoDTO.getStatus())
                                .usuario(usuario)
                                .build();

                Mockito.when(egressoService.buscarPorId(1L))
                                .thenReturn(egresso);

                Mockito.when(egressoService.atualizarEgresso(Mockito.any(Egresso.class)))
                                .thenReturn(egressoAtualizado);

                String json = objectMapper.writeValueAsString(egressoDTO);

                // acao
                MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(API + "/atualizar/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json);

                mvc.perform(request)
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value(egressoDTO.getNome()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.descricao")
                                                .value(egressoDTO.getDescricao()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.status")
                                                .value(egressoDTO.getStatus().toString()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.foto")
                                                .value(egressoDTO.getFoto()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.instagram")
                                                .value(egressoDTO.getInstagram()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.linkedin")
                                                .value(egressoDTO.getLinkedin()));
        }
}
