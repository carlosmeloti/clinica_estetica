package com.cljtech.clinica.service;

import com.cljtech.clinica.data.Usuario;
import com.cljtech.clinica.data.repository.UsuarioRepository;
import com.cljtech.clinica.mapper.EntityMapper;
import com.cljtech.clinica.records.UsuarioResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityMapper entityMapper;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, EntityMapper entityMapper) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.entityMapper = entityMapper;
    }

    public Usuario salvar(Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public UsuarioResponse buscarPorLogin(String login) {
        return entityMapper.toUsuarioResponse(usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado")));
    }
}

