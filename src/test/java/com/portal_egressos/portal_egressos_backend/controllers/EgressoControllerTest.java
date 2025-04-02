package com.portal_egressos.portal_egressos_backend.controllers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portal_egressos.portal_egressos_backend.config.auth.SecurityFilter;
import com.portal_egressos.portal_egressos_backend.config.auth.TokenProvider;
import com.portal_egressos.portal_egressos_backend.dto.EgressoDTO;
import com.portal_egressos.portal_egressos_backend.enums.Status;
import com.portal_egressos.portal_egressos_backend.enums.UserRole;
import com.portal_egressos.portal_egressos_backend.models.Cargo;
import com.portal_egressos.portal_egressos_backend.models.Curso;
import com.portal_egressos.portal_egressos_backend.models.CursoEgresso;
import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.models.Usuario;
import com.portal_egressos.portal_egressos_backend.repositories.EgressoRepository;
import com.portal_egressos.portal_egressos_backend.repositories.UsuarioRepository;
import com.portal_egressos.portal_egressos_backend.services.CargoService;
import com.portal_egressos.portal_egressos_backend.services.CursoEgressoService;
import com.portal_egressos.portal_egressos_backend.services.CursoService;
import com.portal_egressos.portal_egressos_backend.services.EgressoService;
import com.portal_egressos.portal_egressos_backend.services.UsuarioService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = EgressoController.class, excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class
}, excludeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityFilter.class))
@ActiveProfiles("test")
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
        private CargoService cargoService;

        @MockBean
        private CursoEgressoService cursoEgressoService;

        @MockBean
        private UsuarioRepository userRepository;

        @MockBean
        private EgressoRepository egressoRepositorio;

        @MockBean
        private TokenProvider tokenProvider;

        @MockBean
        private UsuarioService usuarioService;

        @Test
        public void deveSalvarEgresso() throws Exception {

                EgressoDTO egressoDTO = EgressoDTO.builder()
                                .nome("Anderson Lopes")
                                .descricao("Cientista da Computação.")
                                .foto("https://example.com/foto2.jpg")
                                .linkedin("https://www.linkedin.com/in/anderson-lopes")
                                .instagram("https://www.instagram.com/anderson_silva")
                                .curriculo("https://example.com/anderson.pdf")
                                .status(Status.PENDENTE)
                                .emailUsuario("anderson@example.com")
                                .senhaUsuario("senha123")
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
                Curso curso = Curso.builder()
                                .id(1L)
                                .nivel("Graduação")
                                .build();

                CursoEgresso cursoEgresso = CursoEgresso.builder()
                                .anoInicio(2017)
                                .anoFim(2021)
                                .curso(curso)
                                .egresso(egresso)
                                .build();

                Cargo cargo = Cargo.builder()
                                .descricao("Desenvolvedor")
                                .build();

                Mockito.when(cargoService.listarCargoPorEgressoId(11L)).thenReturn(Collections.singletonList(cargo));
                Mockito.when(cursoEgressoService.buscarPorEgressoId(11L)).thenReturn(Optional.of(cursoEgresso));
                Mockito.when(egressoService.salvarEgresso(Mockito.any(Egresso.class), Mockito.any()))
                                .thenReturn(egresso);

                String dtoJson = new ObjectMapper().writeValueAsString(egressoDTO);

                MockMultipartFile dtoPart = new MockMultipartFile(
                                "dto",
                                "dto",
                                MediaType.APPLICATION_JSON_VALUE,
                                dtoJson.getBytes());

                MockMultipartFile imagemPart = new MockMultipartFile(
                                "imagem",
                                "imagem.png",
                                MediaType.IMAGE_PNG_VALUE,
                                "conteúdo da imagem".getBytes());

                mvc.perform(MockMvcRequestBuilders.multipart(API + "/salvar")
                                .file(dtoPart)
                                .file(imagemPart)
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                                .andExpect(MockMvcResultMatchers.status().isCreated())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(11))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.nomeEgresso").value("Anderson Lopes"));
        }

        @Test
        public void deveRemoverEgresso() throws Exception {
                String token = "valid-token";
                String email = "gbastos@gmail.com";
                Mockito.when(tokenProvider.extrairEmailDoToken(token)).thenReturn(email);

                Usuario usuario = Usuario.builder()
                                .id(1L)
                                .email(email)
                                .role(UserRole.EGRESSO)
                                .build();
                Mockito.when(usuarioService.buscarUsuarioPorEmail(email)).thenReturn(usuario);

                Egresso egresso = Egresso.builder()
                                .id(1L)
                                .nome("Gabriel B")
                                .foto("foto.jpg")
                                .usuario(usuario)
                                .build();
                Mockito.when(egressoService.buscarEgresso(Mockito.any()))
                                .thenReturn(Collections.singletonList(egresso));

                // Configuração dos Mocks
                Mockito.doNothing().when(egressoService).removerEgresso(1L);

                // Ação
                MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(API + "/deletar/1")
                                .header("Authorization", "Bearer " + token)
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

                Curso curso = Curso.builder()
                                .id(1L)
                                .nivel("Graduação")
                                .build();

                CursoEgresso cursoEgresso = CursoEgresso.builder()
                                .anoInicio(2017)
                                .anoFim(2021)
                                .curso(curso)
                                .egresso(egresso1)
                                .build();

                Cargo cargo = Cargo.builder()
                                .descricao("Desenvolvedor")
                                .build();

                List<Egresso> egressosMock = Arrays.asList(egresso1, egresso2);

                Mockito.when(cargoService.listarCargoPorEgressoId(1L)).thenReturn(Collections.singletonList(cargo));
                Mockito.when(cargoService.listarCargoPorEgressoId(2L)).thenReturn(Collections.singletonList(cargo));
                Mockito.when(cursoEgressoService.buscarPorEgressoId(1L)).thenReturn(Optional.of(cursoEgresso));
                Mockito.when(cursoEgressoService.buscarPorEgressoId(2L)).thenReturn(Optional.of(cursoEgresso));
                Mockito.when(egressoService.listarEgressos()).thenReturn(egressosMock);

                // Ação
                MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(API + "/listar")
                                .accept(MediaType.APPLICATION_JSON);

                // Verificação
                mvc.perform(request)
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nomeEgresso").value("Anderson Lopes"))
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
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].nomeEgresso").value("Anderson Silva"))
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

                Curso curso = Curso.builder()
                                .id(1L)
                                .nivel("Graduação")
                                .build();

                CursoEgresso cursoEgresso = CursoEgresso.builder()
                                .anoInicio(2017)
                                .anoFim(2021)
                                .curso(curso)
                                .egresso(egresso)
                                .build();

                Cargo cargo = Cargo.builder()
                                .descricao("Desenvolvedor")
                                .build();

                List<Egresso> egressosMock = Arrays.asList(egresso);
                Mockito.when(cargoService.listarCargoPorEgressoId(1L)).thenReturn(Collections.singletonList(cargo));
                Mockito.when(cursoEgressoService.buscarPorEgressoId(1L)).thenReturn(Optional.of(cursoEgresso));
                Mockito.when(egressoService.listarEgressosAprovados()).thenReturn(egressosMock);

                // Ação
                MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(API + "/buscarAprovados")
                                .accept(MediaType.APPLICATION_JSON);

                // Verificação
                mvc.perform(request)
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nomeEgresso").value("Anderson Lopes"))
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

                Curso curso = Curso.builder()
                                .id(1L)
                                .nivel("Graduação")
                                .build();

                CursoEgresso cursoEgresso = CursoEgresso.builder()
                                .anoInicio(2017)
                                .anoFim(2021)
                                .curso(curso)
                                .egresso(egresso)
                                .build();

                Cargo cargo = Cargo.builder()
                                .descricao("Desenvolvedor")
                                .build();

                List<Egresso> egressosMock = Arrays.asList(egresso);
                Mockito.when(cargoService.listarCargoPorEgressoId(1L)).thenReturn(Collections.singletonList(cargo));
                Mockito.when(cursoEgressoService.buscarPorEgressoId(1L)).thenReturn(Optional.of(cursoEgresso));
                Mockito.when(egressoService.buscarEgressoPorNome("Anderson"))
                                .thenReturn(egressosMock);

                MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(API + "/buscarPorNome")
                                .param("nome", "Anderson")
                                .accept(MediaType.APPLICATION_JSON);

                mvc.perform(request)
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nomeEgresso").value("Anderson Lopes"))
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
                String token = "token-valido";
                String email = "coordenador@example.com";

                Usuario usuarioCoordenador = Usuario.builder()
                                .email(email)
                                .senha("senha123")
                                .role(UserRole.COORDENADOR)
                                .build();

                Mockito.when(tokenProvider.extrairEmailDoToken(token)).thenReturn(email);
                Mockito.when(usuarioService.buscarUsuarioPorEmail(email)).thenReturn(usuarioCoordenador);

                EgressoDTO egressoDTO = EgressoDTO.builder()
                                .id(1L)
                                .nome("Anderson Silva")
                                .descricao("Nova descrição")
                                .foto("nova-foto.jpg")
                                .instagram("novo-instagram")
                                .linkedin("novo-linkedin")
                                .status(Status.APROVADO)
                                .emailUsuario("egresso@example.com")
                                .build();

                Usuario usuarioEgresso = Usuario.builder()
                                .email("egresso@example.com")
                                .role(UserRole.EGRESSO)
                                .build();

                Egresso egressoExistente = Egresso.builder()
                                .id(1L)
                                .nome("Anderson Antigo")
                                .usuario(usuarioEgresso)
                                .build();

                Egresso egressoAtualizado = Egresso.builder()
                                .id(1L)
                                .nome(egressoDTO.getNome())
                                .descricao(egressoDTO.getDescricao())
                                .foto(egressoDTO.getFoto())
                                .instagram(egressoDTO.getInstagram())
                                .linkedin(egressoDTO.getLinkedin())
                                .status(egressoDTO.getStatus())
                                .usuario(usuarioEgresso)
                                .build();
                Curso curso = Curso.builder()
                                .id(1L)
                                .nivel("Graduação")
                                .build();

                CursoEgresso cursoEgresso = CursoEgresso.builder()
                                .anoInicio(2017)
                                .anoFim(2021)
                                .curso(curso)
                                .egresso(egressoExistente)
                                .build();

                Cargo cargo = Cargo.builder()
                                .descricao("Desenvolvedor")
                                .build();

                Mockito.when(cargoService.listarCargoPorEgressoId(1L)).thenReturn(Collections.singletonList(cargo));
                Mockito.when(cursoEgressoService.buscarPorEgressoId(1L)).thenReturn(Optional.of(cursoEgresso));
                Mockito.when(egressoService.buscarPorId(1L)).thenReturn(egressoExistente);
                Mockito.when(egressoService.atualizarEgresso(Mockito.any(Egresso.class), Mockito.any()))
                                .thenReturn(egressoAtualizado);

                ObjectMapper objectMapper = new ObjectMapper();
                String json = objectMapper.writeValueAsString(egressoDTO);

                MockMultipartFile dtoPart = new MockMultipartFile(
                                "dto",
                                "dto",
                                MediaType.APPLICATION_JSON_VALUE,
                                json.getBytes());

                mvc.perform(MockMvcRequestBuilders.multipart(API + "/atualizar/1")
                                .file(dtoPart)
                                .with(request -> {
                                        request.setMethod("PUT");
                                        return request;
                                })
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.nomeEgresso").value(egressoDTO.getNome()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.descricao")
                                                .value(egressoDTO.getDescricao()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.status")
                                                .value(egressoDTO.getStatus().toString()));
        }
}
