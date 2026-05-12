package com.cljtech.clinica.service.impl;

import com.cljtech.clinica.data.Usuario;
import com.cljtech.clinica.data.repository.UsuarioRepository;
import com.cljtech.clinica.mapper.EntityMapper;
import com.cljtech.clinica.model.records.UsuarioResponse;
import com.cljtech.clinica.service.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityMapper entityMapper;

    public void salvar(Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
    }

    public List<UsuarioResponse> listarTodos() {
        return entityMapper.toUsuarioResponse(usuarioRepository.findAll());
    }

    public UsuarioResponse buscarPorLogin(String login) {
        return entityMapper.toUsuarioResponse(usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado")));
    }
}

