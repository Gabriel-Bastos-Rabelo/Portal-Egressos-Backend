package com.portal_egressos.portal_egressos_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import com.portal_egressos.portal_egressos_backend.exceptions.RegraNegocioRunTime;
import com.portal_egressos.portal_egressos_backend.repositories.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService{
    
    @Autowired
    UsuarioRepository usuarioRepository;


    @Override
    public UserDetails loadUserByUsername(String login) {
        UserDetails user = usuarioRepository.findByEmail(login);

        if (user == null) {
            throw new RegraNegocioRunTime("Conta não encontrada");
        }
        
        return user; 

    }


}
