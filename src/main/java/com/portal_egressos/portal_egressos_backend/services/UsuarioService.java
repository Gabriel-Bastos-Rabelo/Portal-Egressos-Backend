package com.portal_egressos.portal_egressos_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import com.portal_egressos.portal_egressos_backend.exceptions.RegraNegocioRunTime;
import com.portal_egressos.portal_egressos_backend.models.Usuario;
import com.portal_egressos.portal_egressos_backend.repositories.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService{
    
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

    public Usuario buscarPorId(Long id){
        Usuario usuario = usuarioRepositorio.findById(id)
                            .orElseThrow(() -> new RuntimeException("Usuario não encontrado com id: " + id));
        return usuario;
    }
}
