package com.portal_egressos.portal_egressos_backend.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import com.portal_egressos.portal_egressos_backend.config.auth.TokenProvider;
import com.portal_egressos.portal_egressos_backend.dto.CoordenadorDTO;
import com.portal_egressos.portal_egressos_backend.enums.UserRole;
import com.portal_egressos.portal_egressos_backend.models.Coordenador;
import com.portal_egressos.portal_egressos_backend.models.Curso;
import com.portal_egressos.portal_egressos_backend.models.Usuario;
import com.portal_egressos.portal_egressos_backend.repositories.UsuarioRepository;
import com.portal_egressos.portal_egressos_backend.services.CoordenadorService;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@WebMvcTest(controllers = CoordenadorController.class)
@AutoConfigureMockMvc
public class CoordenadorControllerTest {
        static final String API = "/api/coordenador";

        @Autowired
        MockMvc mvc;

        @MockBean
        CoordenadorService coordService;

        @MockBean
        private UsuarioRepository usuarioRepository;

        @MockBean
        private TokenProvider tokenProvider;

        @Test
        public void deveListarCoordenadores() throws Exception {
                // Configuração do ObjectMapper para lidar com LocalDateTime
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

                // Cenário
                Usuario usuario1 = Usuario.builder()
                                .email("teste@teste.com")
                                .senha("12345678")
                                .role(UserRole.COORDENADOR)
                                .build();

                Usuario usuario2 = Usuario.builder()
                                .email("teste2@teste.com")
                                .senha("12345678")
                                .role(UserRole.COORDENADOR)
                                .build();

                Curso curso1 = Curso.builder()
                                .id(1L)
                                .nome("coordenador")
                                .nivel("graduação")
                                .build();

                Curso curso2 = Curso.builder()
                                .id(2L)
                                .nome("coordenador")
                                .nivel("graduação")
                                .build();

                List<Coordenador> retornoServLista = Arrays.asList(
                                Coordenador.builder()
                                                .id(1L)
                                                .nome("Anderson Lopes")
                                                .dataCriacao(LocalDateTime.of(2024, 12, 10, 0, 0))
                                                .ativo(true)
                                                .curso(curso1)
                                                .usuario(usuario1)
                                                .build(),
                                Coordenador.builder()
                                                .id(2L)
                                                .nome("Vitor Sousa")
                                                .dataCriacao(LocalDateTime.of(2024, 12, 10, 0, 0))
                                                .ativo(true)
                                                .curso(curso2)
                                                .usuario(usuario2)
                                                .build());

                Mockito.when(coordService.listarCoordenadores()).thenReturn(retornoServLista);

                // Ação
                MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(API + "/listar");

                // Verificação
                mvc.perform(request)
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id")
                                                .value(retornoServLista.get(0).getId()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nome")
                                                .value(retornoServLista.get(0).getNome()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].dataCriacao")
                                                .value(retornoServLista.get(0).getDataCriacao()
                                                                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))

                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].ativo")
                                                .value(retornoServLista.get(0).getAtivo()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id")
                                                .value(retornoServLista.get(1).getId()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].nome")
                                                .value(retornoServLista.get(1).getNome()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].dataCriacao")
                                                .value(retornoServLista.get(0).getDataCriacao()
                                                                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))

                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].ativo")
                                                .value(retornoServLista.get(1).getAtivo()));
        }

        @Test
        public void deveBuscarCoordenadorPorNome() throws Exception {
                // Cenário
                Usuario usuario = Usuario.builder()
                                .email("teste@teste.com")
                                .senha("12345678")
                                .role(UserRole.COORDENADOR)
                                .build();

                Curso curso = Curso.builder()
                                .nome("coordenador")
                                .nivel("graduação")
                                .build();

                Coordenador coordenador = Coordenador.builder()
                                .id(1L)
                                .nome("Anderson Lopes")
                                .dataCriacao(LocalDateTime.of(2024, 12, 10, 0, 0))
                                .ativo(true)
                                .curso(curso)
                                .usuario(usuario)
                                .build();

                List<Coordenador> coordenadorMock = Arrays.asList(coordenador);

                // Simulações
                Mockito.when(coordService.buscarCoordenadorPorNome("Anderson")).thenReturn(coordenadorMock);

                // Ação
                MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(API + "/buscarPorNome")
                                .param("nome", "Anderson")
                                .accept(MediaType.APPLICATION_JSON);

                // Verificação
                mvc.perform(request)
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id")
                                                .value(coordenador.getId()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nome")
                                                .value(coordenador.getNome()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].dataCriacao")
                                                .value(coordenador.getDataCriacao()
                                                                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].ativo")
                                                .value(coordenador.getAtivo()));
        }

        @Test
        public void deveAtualizarCoordenador() throws Exception {
                // Cenário
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

                CoordenadorDTO dto = CoordenadorDTO.builder()
                                .id(1L)
                                .nome("Anderson Lopes Atualizado")
                                .dataCriacao(LocalDateTime.of(2024, 12, 10, 0, 0))
                                .ativo(true)
                                .build();

                Usuario usuario = Usuario.builder()
                                .email("teste@teste.com")
                                .senha("12345678")
                                .role(UserRole.COORDENADOR)
                                .build();

                Curso curso = Curso.builder()
                                .id(1L)
                                .nome("coordenador")
                                .nivel("graduação")
                                .build();

                Coordenador coordenadorExistente = Coordenador.builder()
                                .id(dto.getId())
                                .nome("Anderson Lopes")
                                .dataCriacao(LocalDateTime.of(2024, 12, 10, 0, 0))
                                .ativo(true)
                                .curso(curso)
                                .usuario(usuario)
                                .build();

                Coordenador coordenadorAtualizado = Coordenador.builder()
                                .id(dto.getId())
                                .nome(dto.getNome()) 
                                .dataCriacao(dto.getDataCriacao())
                                .ativo(dto.getAtivo())
                                .curso(curso)
                                .usuario(usuario)
                                .build();

               
                Mockito.when(coordService.buscarPorId(1L))
                                .thenReturn(coordenadorExistente); 

                Mockito.when(coordService.atualizarCoordenador(Mockito.any(Coordenador.class)))
                                .thenReturn(coordenadorAtualizado); 
                                
                String json = objectMapper.writeValueAsString(dto);

                // Ação
                MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(API + "/atualizar/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json);

                // Verificação
                mvc.perform(request)
                                .andExpect(MockMvcResultMatchers.status().isOk()) // Status esperado: 200
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(coordenadorAtualizado.getId()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.nome")
                                                .value(coordenadorAtualizado.getNome()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.dataCriacao")
                                                .value(coordenadorAtualizado.getDataCriacao()
                                                                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.ativo")
                                                .value(coordenadorAtualizado.getAtivo()));
        }

}
