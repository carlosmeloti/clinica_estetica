package com.cljtech.clinica.service;

import com.cljtech.clinica.data.Usuario;
import com.cljtech.clinica.data.repository.UsuarioRepository;
import com.cljtech.clinica.security.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface CustomUserDetailsService {
    UserDetails loadUserByUsername(String username);
}
