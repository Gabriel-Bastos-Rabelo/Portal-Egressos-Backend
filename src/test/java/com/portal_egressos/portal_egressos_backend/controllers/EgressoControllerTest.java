package com.portal_egressos.portal_egressos_backend.controllers;

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
import com.portal_egressos.portal_egressos_backend.config.auth.TokenProvider;
import com.portal_egressos.portal_egressos_backend.dto.EgressoDTO;
import com.portal_egressos.portal_egressos_backend.enums.Status;
import com.portal_egressos.portal_egressos_backend.enums.UserRole;
import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.models.Usuario;
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
}
