package com.portal_egressos.portal_egressos_backend.controllers;

import com.portal_egressos.portal_egressos_backend.config.auth.TokenProvider;
import com.portal_egressos.portal_egressos_backend.enums.UserRole;
import com.portal_egressos.portal_egressos_backend.models.Curso;
import com.portal_egressos.portal_egressos_backend.models.CursoEgresso;
import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.models.Usuario;
import com.portal_egressos.portal_egressos_backend.repositories.UsuarioRepository;
import com.portal_egressos.portal_egressos_backend.services.CursoService;
import com.portal_egressos.portal_egressos_backend.services.EgressoService;
import com.portal_egressos.portal_egressos_backend.enums.Status;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = CursoController.class)
@Import(TestSecurityConfig.class) // configuração de segurança personalizada que nao precisa de autenticação
@AutoConfigureMockMvc
public class CursoControllerTest {

    static final String API = "/api/cursos";

    @Autowired
    MockMvc mvc;

    @MockBean
    CursoService cursoService;

    @MockBean 
    EgressoService egressoService;

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private UsuarioRepository userRepository;

    @Test
    public void deveListarCursos() throws Exception {
        // Cenário
        List<Curso> retornoServLista = Arrays.asList(
                Curso.builder()
                        .id(1L)
                        .nome("Curso de teste")
                        .nivel("Nivel de teste")
                        .build(),
                Curso.builder()
                        .id(2L)
                        .nome("Curso de teste 2")
                        .nivel("Nivel de teste")
                        .build());

        Mockito.when(cursoService.listarCursos()).thenReturn(retornoServLista);

        // Ação
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(API + "/listarCursos");

        // Verificação
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(retornoServLista.get(0).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nome").value(retornoServLista.get(0).getNome()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nivel").value(retornoServLista.get(0).getNivel()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(retornoServLista.get(1).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].nome").value(retornoServLista.get(1).getNome()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].nivel").value(retornoServLista.get(1).getNivel()));
    }

    @Test
    public void deveListarEgressosPorCurso() throws Exception {
        // Cenário

        // Cursos
        List<Curso> cursos = new ArrayList<>();
        Curso curso1 = Curso.builder().id(1L).nome("Curso Teste 1").nivel("Nível Teste 1").build();
        cursos.add(curso1);

        // Usuários
        List<Usuario> usuarios = new ArrayList<>();
        Usuario usuario1 = Usuario.builder().email("teste1@teste.com").senha("senha123").role(UserRole.EGRESSO).build();
        usuarios.add(usuario1);
        Usuario usuario2 = Usuario.builder().email("teste2@teste.com").senha("senha123").role(UserRole.EGRESSO).build();
        usuarios.add(usuario2);

        // Egressos
        List<Egresso> egressos = new ArrayList<>();
        Egresso egresso1 = Egresso.builder()
                .nome("Egresso 1")
                .descricao("Descrição do Egresso 1")
                .foto("urlFoto1")
                .linkedin("https://www.linkedin.com/in/egresso1")
                .instagram("https://www.instagram.com/egresso1")
                .curriculo("Currículo do Egresso 1")
                .usuario(usuario1)
                .status(Status.PENDENTE)
                .build();
        egressos.add(egresso1);

        Egresso egresso2 = Egresso.builder()
                .nome("Egresso 2")
                .descricao("Descrição do Egresso 2")
                .foto("urlFoto2")
                .linkedin("https://www.linkedin.com/in/egresso2")
                .instagram("https://www.instagram.com/egresso2")
                .curriculo("Currículo do Egresso 2")
                .usuario(usuario2)
                .status(Status.PENDENTE)
                .build();
        egressos.add(egresso2);

        Mockito.when(cursoService.buscarPorId(1L)).thenReturn(curso1);
        Mockito.when(cursoService.listarEgressosPorCurso(curso1)).thenReturn(egressos);

        //ação
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/api/cursos/listar_egressos_por_curso/1");

        // Verificação
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(curso1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nome").value(curso1.getNome()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nivel").value(curso1.getNivel()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].idEgresso").value(egresso1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nomeEgresso").value(egresso1.getNome()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].idEgresso").value(egresso2.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].nomeEgresso").value(egresso2.getNome()));
    }

    @Test
    public void deveListarQuantidadeDeEgressosPorCurso() throws Exception {
        // Cenário

        // cursos
        List<Curso> cursos = new ArrayList<>();
        Curso curso1 = Curso.builder().id(1L).nome("Curso Teste 1").nivel("Nível Teste 1").build();
        Curso curso2 = Curso.builder().id(2L).nome("Curso Teste 2").nivel("Nível Teste 2").build();
        cursos.add(curso1);
        cursos.add(curso2);

        // usuário
        Usuario usuario1 = Usuario.builder()
                .email("teste1@teste.com")
                .senha("senha123")
                .role(UserRole.EGRESSO)
                .build();

        // egresso
        Egresso egresso1 = Egresso.builder()
                .nome("Egresso 1")
                .descricao("Descrição do Egresso 1")
                .foto("urlFoto1")
                .linkedin("https://www.linkedin.com/in/egresso1")
                .instagram("https://www.instagram.com/egresso1")
                .curriculo("Currículo do Egresso 1")
                .usuario(usuario1)
                .status(Status.PENDENTE)
                .build();

        // Associando o egresso aos dois cursos
        CursoEgresso cursoEgresso1 = CursoEgresso.builder()
                .egresso(egresso1)
                .curso(curso1)
                .anoInicio(2020)
                .anoFim(2023)
                .build();

        CursoEgresso cursoEgresso2 = CursoEgresso.builder()
                .egresso(egresso1)
                .curso(curso2)
                .anoInicio(2021)
                .anoFim(2024)
                .build();

        
        int quantidadeEgressosCurso = 1;

        Mockito.when(cursoService.listarQuantidadeDeEgressosPorCurso(Mockito.any(Curso.class))).thenReturn(quantidadeEgressosCurso);

        // Ação
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/api/cursos/listar_quantidade_egressos_por_curso/1");

        // Verificação
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk()) // Verificar se o status da resposta é OK (200)
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(quantidadeEgressosCurso)); // Verificar se o corpo da resposta contém o valor correto da quantidade
    }


}
