package com.portal_egressos.portal_egressos_backend.controllers;



import java.util.Arrays;
import java.util.Collections;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portal_egressos.portal_egressos_backend.config.auth.SecurityFilter;
import com.portal_egressos.portal_egressos_backend.config.auth.TokenProvider;
import com.portal_egressos.portal_egressos_backend.dto.CargoDTO;
import com.portal_egressos.portal_egressos_backend.enums.UserRole;
import com.portal_egressos.portal_egressos_backend.models.Cargo;
import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.models.Usuario;
import com.portal_egressos.portal_egressos_backend.repositories.UsuarioRepository;
import com.portal_egressos.portal_egressos_backend.services.CargoService;
import com.portal_egressos.portal_egressos_backend.services.EgressoService;
import com.portal_egressos.portal_egressos_backend.services.UsuarioService;


@ExtendWith(SpringExtension.class)
@WebMvcTest(
    controllers = CargoController.class,
    excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class
    },
    excludeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityFilter.class)
)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CargoControllerTest {

    static final String API = "/api/cargo";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CargoService cargoService;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private EgressoService egressoService;

    @MockBean
    private TokenProvider tokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioRepository userRepository;

    @Test
    public void deveSalvarCargo() throws Exception {
       
        CargoDTO dto = CargoDTO.builder()
            .descricao("Desenvolvedor")
            .local("Empresa XYZ")
            .anoInicio(2020)
            .anoFim(2022)
            .build();


        String token = "valid-token";
        String email = "gbastos@gmail.com";
        Mockito.when(tokenProvider.extrairEmailDoToken(token)).thenReturn(email);

        Usuario usuario = Usuario.builder()
            .id(1L)
            .email(email)
            .role(UserRole.EGRESSO)
            .build();
        Mockito.when(usuarioService.buscarUsuarioPorEmail(email)).thenReturn(usuario);

        Mockito.when(userRepository.findByEmail(email)).thenReturn(usuario);
        Egresso egresso = Egresso.builder()
            .id(1L)
            .nome("Gabriel B")
            .foto("foto.jpg")
            .usuario(usuario)
            .build();
        Mockito.when(egressoService.buscarEgresso(Mockito.any()))
            .thenReturn(Collections.singletonList(egresso));

        Cargo cargoSalvo = Cargo.builder()
            .id(1L)
            .descricao(dto.getDescricao())
            .local(dto.getLocal())
            .anoInicio(dto.getAnoInicio())
            .anoFim(dto.getAnoFim())
            .egresso(egresso)
            .build();
        Mockito.when(cargoService.salvarCargo(Mockito.any(Cargo.class))).thenReturn(cargoSalvo);

        String json = objectMapper.writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API)
            .header("Authorization", "Bearer " + token)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json);

        mvc.perform(request)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("id").value(1L))
        .andExpect(jsonPath("descricao").value("Desenvolvedor"))
        .andExpect(jsonPath("local").value("Empresa XYZ"))
        .andExpect(jsonPath("anoInicio").value("2020"))
        .andExpect(jsonPath("anoFim").value("2022"))
        .andExpect(jsonPath("nomeEgresso").value("Gabriel B"))
        .andExpect(jsonPath("fotoEgresso").value("foto.jpg"));
    }

    @Test
    public void deveRetornarBadRequestAoSalvarCargoComEgressoInexistente() throws Exception {
        CargoDTO dto = CargoDTO.builder()
            .descricao("Desenvolvedor")
            .local("Empresa XYZ")
            .anoInicio(2020)
            .anoFim(2022)
            .build();

        String token = "valid-token";
        String email = "gbastos@gmail.com";
        Mockito.when(tokenProvider.extrairEmailDoToken(token)).thenReturn(email);

        Usuario usuario = Usuario.builder()
            .id(1L)
            .email(email)
            .role(UserRole.EGRESSO)
            .build();
        Mockito.when(usuarioService.buscarUsuarioPorEmail(email)).thenReturn(usuario);

        Mockito.when(egressoService.buscarEgresso(Mockito.any()))
            .thenReturn(Collections.emptyList());

        String json = objectMapper.writeValueAsString(dto);

        MockHttpServletRequestBuilder request = post(API)
            .header("Authorization", "Bearer " + token)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json);

        mvc.perform(request)
        .andExpect(status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$")
        .value("Egresso não encontrado para o usuário associado."));
    }

    @Test
    public void deveListarCargosPorEgresso() throws Exception {
        Egresso egresso = Egresso.builder().id(1L).nome("Gabriel Bastos Rabelo").foto("foto egresso").build();
        Long egressoId = 1L;
        Cargo c1 = Cargo.builder()
            .id(1L)
            .descricao("Desenvolvedor")
            .local("Empresa XYZ")
            .anoInicio(2020)
            .egresso(egresso)
            .anoFim(2022)
            .build();
        Cargo c2 = Cargo.builder()
            .id(2L)
            .descricao("Analista")
            .local("Empresa ABC")
            .anoInicio(2019)
            .egresso(egresso)
            .anoFim(2021)
            .build();

        Mockito.when(cargoService.listarCargoPorEgressoId(egressoId))
            .thenReturn(Arrays.asList(c1, c2));

        MockHttpServletRequestBuilder request = get(API + "/egresso/" + egressoId)
            .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].descricao").value("Desenvolvedor"))
        .andExpect(jsonPath("$[0].local").value("Empresa XYZ"))
        .andExpect(jsonPath("$[1].descricao").value("Analista"))
        .andExpect(jsonPath("$[1].local").value("Empresa ABC"));
    }

    @Test
    public void deveAtualizarStatusCargo() throws Exception {
        Long cargoId = 1L;
        CargoDTO dto = CargoDTO.builder()
            .id(cargoId)
            .descricao("Desenvolvedor Sênior")
            .local("Empresa XYZ")
            .anoInicio(2021)
            .anoFim(2023)
            .build();

        String token = "valid-token";
        String email = "gbastos@gmail.com";
        Mockito.when(tokenProvider.extrairEmailDoToken(token)).thenReturn(email);

        Usuario usuario = Usuario.builder()
            .id(2L)
            .email(email)
            .role(UserRole.COORDENADOR)
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

        Cargo cargoExistente = Cargo.builder()
            .id(cargoId)
            .descricao("Desenvolvedor")
            .local("Empresa XYZ")
            .anoInicio(2020)
            .anoFim(2022)
            .egresso(egresso)
            .build();

        Cargo cargoAtualizado = Cargo.builder()
            .id(cargoId)
            .descricao(dto.getDescricao())
            .local(dto.getLocal())
            .anoInicio(dto.getAnoInicio())
            .anoFim(dto.getAnoFim())
            .egresso(egresso)
            .build();

        Mockito.when(cargoService.verificarCargoPorEgresso(cargoId, egresso.getId()))
            .thenReturn(cargoExistente);

        Mockito.when(cargoService.atualizarCargo(Mockito.any(Cargo.class)))
            .thenReturn(cargoAtualizado);

        String json = objectMapper.writeValueAsString(dto);

        MockHttpServletRequestBuilder request = put(API + "/{id}", cargoId)
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
        .andExpect(status().isOk())
        .andExpect(jsonPath("id").value(cargoId))
        .andExpect(jsonPath("descricao").value("Desenvolvedor Sênior"))
        .andExpect(jsonPath("local").value("Empresa XYZ"))
        .andExpect(jsonPath("anoInicio").value("2021"))
        .andExpect(jsonPath("anoFim").value("2023"))
        .andExpect(jsonPath("nomeEgresso").value("Gabriel B"))
        .andExpect(jsonPath("fotoEgresso").value("foto.jpg"));
    }

    @Test
    public void deveDeletarCargo() throws Exception {
        Long cargoId = 1L;

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

        Cargo cargoExistente = Cargo.builder()
            .id(cargoId)
            .descricao("Desenvolvedor")
            .local("Empresa XYZ")
            .anoInicio(2021)
            .anoFim(2022)
            .egresso(egresso)
            .build();
        Mockito.when(cargoService.verificarCargoPorEgresso(cargoId, egresso.getId()))
            .thenReturn(cargoExistente);

        Mockito.doNothing().when(cargoService).removerCargo(cargoExistente);

        MockHttpServletRequestBuilder request = delete(API + "/{id}", cargoId)
            .header("Authorization", "Bearer " + token)
            .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
        .andExpect(status().isNoContent());
    }





}
