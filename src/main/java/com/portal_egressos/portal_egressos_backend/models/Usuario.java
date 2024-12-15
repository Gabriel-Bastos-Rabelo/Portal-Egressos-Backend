package com.portal_egressos.portal_egressos_backend.models;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.portal_egressos.portal_egressos_backend.enums.UserRole;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuario", schema = "public")
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "senha", nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable=false)
    private UserRole role;

    public Usuario(String email, String password, UserRole role) {
        this.email = email;
        this.senha = password;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == UserRole.COORDENADOR) {
        return List.of(new SimpleGrantedAuthority("ROLE_COORDENADOR"));
        }
        return List.of(new SimpleGrantedAuthority("ROLE_EGRESSO"));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return senha;
    }


}
