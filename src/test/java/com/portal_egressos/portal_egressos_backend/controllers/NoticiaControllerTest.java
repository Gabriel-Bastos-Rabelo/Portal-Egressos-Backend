package com.portal_egressos.portal_egressos_backend.controllers;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portal_egressos.portal_egressos_backend.config.auth.SecurityFilter;
import com.portal_egressos.portal_egressos_backend.dto.NoticiaDTO;
import com.portal_egressos.portal_egressos_backend.enums.Status;
import com.portal_egressos.portal_egressos_backend.exceptions.RegraNegocioRunTime;
import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.models.Noticia;
import com.portal_egressos.portal_egressos_backend.services.EgressoService;
import com.portal_egressos.portal_egressos_backend.services.NoticiaService;


@WebMvcTest(
    controllers = NoticiaController.class,
    excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class
    },
    excludeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityFilter.class)
)

@AutoConfigureMockMvc
@ActiveProfiles("test")
public class NoticiaControllerTest {

    static final String API = "/api/noticia";

    @Autowired
    MockMvc mvc; 

    @MockBean
    NoticiaService noticiaService; 

    @MockBean
    EgressoService egressoService;

    @Autowired
    ObjectMapper objectMapper;

    
    @Test
    public void deveSalvarNoticia() throws Exception {

        NoticiaDTO dto = NoticiaDTO.builder()
            .egressoId(1L)
            .titulo("Notícia de Teste")
            .descricao("Descrição de teste")
            .linkNoticia("link noticia")
            .build();

        Egresso egresso = Egresso.builder().id(1L).nome("Gabriel Bastos Rabelo").foto("foto egresso").build();
        Mockito.when(egressoService.buscarEgresso(Mockito.any(Egresso.class)))
            .thenReturn(Collections.singletonList(egresso));


        Noticia noticiaSalva = Noticia.builder()
            .id(10L)
            .titulo("Notícia de Teste")
            .descricao("Descrição de teste")
            .linkNoticia("link noticia")
            .status(Status.PENDENTE)
            .egresso(egresso)
            .build();

        Mockito.when(noticiaService.salvarNoticia(Mockito.any(Noticia.class)))
            .thenReturn(noticiaSalva);

        String json = objectMapper.writeValueAsString(dto);

        MockHttpServletRequestBuilder request = post(API)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json);

        mvc.perform(request)
        .andExpect(status().isOk()) 
        .andExpect(MockMvcResultMatchers.jsonPath("id").value(10L))
        .andExpect(MockMvcResultMatchers.jsonPath("titulo").value("Notícia de Teste"))
        .andExpect(MockMvcResultMatchers.jsonPath("status").value("PENDENTE"));
    }

    @Test
    public void deveRetornarBadRequestAoSalvarNoticiaComEgressoInexistente() throws Exception {
        NoticiaDTO dto = NoticiaDTO.builder()
            .egressoId(999L)
            .titulo("Notícia X")
            .descricao("Notícia sem egresso válido")
            .build();

        Mockito.when(egressoService.buscarEgresso(Mockito.any(Egresso.class)))
            .thenReturn(Collections.emptyList());

        String json = objectMapper.writeValueAsString(dto);

        MockHttpServletRequestBuilder request = post(API)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json);

        mvc.perform(request)
        .andExpect(status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$")
            .value("Egresso não encontrado para o ID: 999"));
    }

    @Test
    public void deveListarNoticias() throws Exception {
        // Cenário
        Egresso egresso = Egresso.builder().id(1L).nome("Gabriel Bastos Rabelo").foto("foto egresso").build();
        Noticia n1 = Noticia.builder().id(1L).titulo("Notícia 1").egresso(egresso).build();
        Noticia n2 = Noticia.builder().id(2L).titulo("Notícia 2").egresso(egresso).build();

        Mockito.when(noticiaService.listarNoticias())
            .thenReturn(java.util.Arrays.asList(n1, n2));

        MockHttpServletRequestBuilder request = get(API)  
            .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].titulo").value("Notícia 1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].titulo").value("Notícia 2"));
    }

    @Test
    public void deveListarNoticiasAprovadas() throws Exception {
        Egresso egresso = Egresso.builder().id(1L).nome("Gabriel Bastos Rabelo").foto("foto egresso").build();
        Noticia n1 = Noticia.builder().id(1L).titulo("Notícia 1").egresso(egresso).build();
        Noticia n2 = Noticia.builder().id(2L).titulo("Notícia 2").egresso(egresso).build();

        Mockito.when(noticiaService.listarNoticiasAprovadas())
            .thenReturn(java.util.Arrays.asList(n1, n2));

        MockHttpServletRequestBuilder request = get(API.concat("/aprovadas")) 
            .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].titulo").value("Notícia 1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].titulo").value("Notícia 2"));
    }

    @Test
    public void deveListarNoticiasPendentes() throws Exception {
        Egresso egresso = Egresso.builder().id(1L).nome("Gabriel Bastos Rabelo").foto("foto egresso").build();
        Noticia n1 = Noticia.builder().id(1L).titulo("Notícia 1").egresso(egresso).build();
        Noticia n2 = Noticia.builder().id(2L).titulo("Notícia 2").egresso(egresso).build();

        Mockito.when(noticiaService.listarNoticiasPendentes())
            .thenReturn(java.util.Arrays.asList(n1, n2));

        MockHttpServletRequestBuilder request = get(API.concat("/pendentes"))  
            .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].titulo").value("Notícia 1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].titulo").value("Notícia 2"));
    }

    @Test
    public void deveAtualizarStatusNoticia() throws Exception {
        Egresso egresso = Egresso.builder().id(1L).nome("Gabriel Bastos Rabelo").foto("foto egresso").build();
        Long id = 5L;
        Noticia noticia = Noticia.builder().id(id).status(Status.APROVADO).titulo("Teste").egresso(egresso).build();

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