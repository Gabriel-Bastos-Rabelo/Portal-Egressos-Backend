package com.portal_egressos.portal_egressos_backend.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portal_egressos.portal_egressos_backend.exceptions.RegraNegocioRunTime;
import com.portal_egressos.portal_egressos_backend.models.Usuario;
import com.portal_egressos.portal_egressos_backend.repositories.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    UsuarioRepository usuarioRepositorio;

    @Override
    public UserDetails loadUserByUsername(String login) {
        UserDetails user = usuarioRepositorio.findByEmail(login);

        if (user == null) {
            throw new RegraNegocioRunTime("Conta não encontrada.");
        }

        return user;

    }

    public Usuario buscarPorId(Long id) {
        Usuario usuario = usuarioRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado com id: " + id));
        return usuario;
    }

    @Transactional
    public Usuario salvarUsuario(Usuario usuario) {
        Optional<Usuario> usuarioExistente = usuarioRepositorio.findUsuarioByEmail(usuario.getEmail());
        if (usuarioExistente.isPresent()) {
            throw new RegraNegocioRunTime(
                    String.format("Usuario com email '%s' já existe.", usuario.getEmail()));
        }

        String senhaEncriptada = new BCryptPasswordEncoder().encode(usuario.getSenha());

        usuario.setSenha(senhaEncriptada);
        return usuarioRepositorio.save(usuario);
    }

    public Usuario buscarUsuarioPorEmail(String email) {
        return usuarioRepositorio.findUsuarioByEmail(email)
                .orElseThrow(() -> new RegraNegocioRunTime("Usuário não encontrado com o e-mail: " + email));
    }
}
