package com.portal_egressos.portal_egressos_backend.controllers;

import java.time.LocalDate;
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
import com.portal_egressos.portal_egressos_backend.repositories.UsuarioRepository;
import com.portal_egressos.portal_egressos_backend.services.DepoimentoService;
import com.portal_egressos.portal_egressos_backend.services.EgressoService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = DepoimentoController.class)
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class) // configuração de segurança personalizada que nao precisa de autenticação
public class DepoimentoControllerTest {

    static final String API = "/api/depoimento";

    @Autowired
    MockMvc mvc;

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
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/depoimento/salvar")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        // Verificação
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

}
