package com.portal_egressos.portal_egressos_backend.services;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.portal_egressos.portal_egressos_backend.exceptions.RegraNegocioRunTime;
import com.portal_egressos.portal_egressos_backend.models.Coordenador;
import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.repositories.CoordenadorRepository;

@Service
public class CoordenadorService {

    @Autowired
    private CoordenadorRepository coordenadorRepositorio;

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);

    @Transactional
    public Coordenador salvarCoordenador(Coordenador coordenador) {
        verificarCoordenador(coordenador);
        Optional<Coordenador> coordenadorExistente = coordenadorRepositorio
                .findByUsuarioEmail(coordenador.getUsuario().getEmail());
        if (coordenadorExistente.isPresent()) {
            throw new RegraNegocioRunTime(
                    String.format("Coordenador com email '%s' já existe.", coordenador.getUsuario().getEmail()));
        }

        String senhaEncriptada = new BCryptPasswordEncoder().encode(coordenador.getUsuario().getSenha());

        coordenador.getUsuario().setSenha(senhaEncriptada);
        return coordenadorRepositorio.save(coordenador);
    }

    @Transactional
    public Coordenador atualizarCoordenador(Coordenador coordenador) {
        verificarCoordenadorId(coordenador);

        Coordenador coordenadorExistente = coordenadorRepositorio.findById(coordenador.getId()).get();
        if (coordenador.getUsuario().getSenha() != null && !coordenador.getUsuario().getSenha().isEmpty()) {
            String senhaEncriptada = new BCryptPasswordEncoder().encode(coordenador.getUsuario().getSenha());
            coordenadorExistente.getUsuario().setSenha(senhaEncriptada);
        }
        if (coordenador.getNome() != null && !coordenador.getNome().isEmpty()) {
            coordenadorExistente.setNome(coordenador.getNome());
        }
        if (coordenador.getDataCriacao() != null) {
            coordenadorExistente.setDataCriacao(coordenador.getDataCriacao());
        }
        if (coordenador.getAtivo() != null) {
            coordenadorExistente.setAtivo(coordenador.getAtivo());
        }

        return coordenadorRepositorio.save(coordenadorExistente);
    }

    @Transactional
    public void removerCoordenador(Coordenador coordenador) {
        verificarCoordenadorId(coordenador);
        coordenadorRepositorio.delete(coordenador);
    }

    public void verificarCoordenador(Coordenador coordenador) {
        if (coordenador == null) {
            throw new RegraNegocioRunTime("Coordenador inválido.");
        }
        if (coordenador.getNome() == null) {
            throw new RegraNegocioRunTime("Nome do coordenador deve ser informado.");
        }
        if (coordenador.getUsuario() == null) {
            throw new RegraNegocioRunTime("Usuário do coordenador deve ser informado.");
        }
        if (coordenador.getUsuario().getEmail() == null) {
            throw new RegraNegocioRunTime("Email do coordenador deve ser informado.");
        }
        if (!validarEmail(coordenador.getUsuario().getEmail())) {
            throw new RegraNegocioRunTime("Email informado é inválido");
        }
        if (coordenador.getUsuario().getSenha() == null) {
            throw new RegraNegocioRunTime("Senha do coordenador deve ser informado.");
        }
        if (!validarSenha(coordenador.getUsuario().getSenha())) {
            throw new RegraNegocioRunTime("Senha informada deve ter no mínimo 8 caracteres");
        }

    }

    private boolean validarEmail(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean validarSenha(String senha) {
        return senha != null && senha.length() >= 8;
    }

    public void verificarCoordenadorId(Coordenador coordenador) {
        if ((coordenador == null) || (coordenador.getId() == null)
                || !(coordenadorRepositorio.existsById(coordenador.getId()))) {
            throw new RegraNegocioRunTime("ID de coordenador é inválido.");
        }
    }

    public List<Coordenador> listarCoordenadores() {
        return coordenadorRepositorio.findAll();
    }

    public List<Coordenador> buscarCoordenadorPorNome(String nome) {
        Coordenador filtro = new Coordenador();
        if (nome != null && !nome.isEmpty()) {
            filtro.setNome(nome);
        }

        Example<Coordenador> example = Example.of(filtro, ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(StringMatcher.CONTAINING));

        return coordenadorRepositorio.findAll(example);
    }

    public Coordenador buscarPorId(Long id) {
        Coordenador coordenador = coordenadorRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Coordenador não encontrado com id: " + id));
        return coordenador;
    }
}
