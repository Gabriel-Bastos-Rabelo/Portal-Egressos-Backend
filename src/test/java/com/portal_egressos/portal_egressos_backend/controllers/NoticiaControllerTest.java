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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portal_egressos.portal_egressos_backend.config.auth.TokenProvider;
import com.portal_egressos.portal_egressos_backend.enums.Status;
import com.portal_egressos.portal_egressos_backend.exceptions.RegraNegocioRunTime;
import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.models.Noticia;
import com.portal_egressos.portal_egressos_backend.repositories.UsuarioRepository;
import com.portal_egressos.portal_egressos_backend.services.EgressoService;
import com.portal_egressos.portal_egressos_backend.services.NoticiaService;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@WebMvcTest(controllers = NoticiaController.class)
@AutoConfigureMockMvc
public class NoticiaControllerTest {

    static final String API = "/api/noticia";

    @Autowired
    private MockMvc mvc; 

    @MockBean
    private NoticiaService noticiaService; 

    @MockBean
    private EgressoService egressoService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private UsuarioRepository userRepository;

    

    @Test
    public void deveListarNoticiasAprovadas() throws Exception {
        Noticia n1 = Noticia.builder().id(1L).descricao("Notícia 1").build();
        Noticia n2 = Noticia.builder().id(2L).descricao("Notícia 2").build();

        Mockito.when(noticiaService.listarNoticiasAprovadas())
            .thenReturn(java.util.Arrays.asList(n1, n2));

        MockHttpServletRequestBuilder request = get(API.concat("/aprovadas")) 
            .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].descricao").value("Notícia 1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].descricao").value("Notícia 2"));
    }

    @Test
    public void deveListarNoticiasPendentes() throws Exception {
        Noticia n1 = Noticia.builder().id(1L).descricao("Notícia 1").build();
        Noticia n2 = Noticia.builder().id(2L).descricao("Notícia 2").build();

        Mockito.when(noticiaService.listarNoticiasPendentes())
            .thenReturn(java.util.Arrays.asList(n1, n2));

        MockHttpServletRequestBuilder request = get(API.concat("/pendentes"))  
            .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].descricao").value("Notícia 1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].descricao").value("Notícia 2"));
    }

    @Test
    public void deveAtualizarStatusNoticia() throws Exception {
        Egresso egresso = Egresso.builder().id(1L).nome("Gabriel Bastos Rabelo").foto("foto egresso").build();
        Long id = 5L;
        Noticia noticia = Noticia.builder().id(id).status(Status.APROVADO).descricao("Teste").build();

        Mockito.when(noticiaService.atualizarStatusNoticia(Mockito.any(Noticia.class)))
            .thenReturn(noticia);

        MockHttpServletRequestBuilder request = put(API.concat("/{id}/status"), id)
            .param("status", "APROVADO")
            .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("id").value(id))
        .andExpect(MockMvcResultMatchers.jsonPath("status").value("APROVADO"));
    }

    @Test
    public void deveDeletarNoticia() throws Exception {
        Long id = 1L;
        Noticia noticiaEncontrada = Noticia.builder().id(id).build();
        Mockito.when(noticiaService.buscarPorNoticiaId(id)).thenReturn(noticiaEncontrada);

        MockHttpServletRequestBuilder request = delete(API.concat("/{id}"), id)
            .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
        .andExpect(status().isNoContent()); 
    }

    @Test
    public void deveRetornarBadRequestAoDeletarNoticiaInexistente() throws Exception {
        Long id = 999L;
        Mockito.when(noticiaService.buscarPorNoticiaId(id))
            .thenThrow(new RegraNegocioRunTime("Notícia não encontrada para o ID: " + id));

        MockHttpServletRequestBuilder request = delete(API.concat("/{id}"), id)
            .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
        .andExpect(status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$")
            .value("Notícia não encontrada para o ID: 999"));
    }



}