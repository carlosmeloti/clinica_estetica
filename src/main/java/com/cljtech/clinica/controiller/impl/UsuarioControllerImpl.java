package com.cljtech.clinica.controiller.impl;

import com.cljtech.clinica.controiller.UsuarioController;
import com.cljtech.clinica.data.Usuario;
import com.cljtech.clinica.mapper.EntityMapper;
import com.cljtech.clinica.records.UsuarioRequest;
import com.cljtech.clinica.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UsuarioControllerImpl implements UsuarioController {

    private final UsuarioService usuarioService;
    private final EntityMapper entityMapper;

    @Override
    public ResponseEntity<Void> criar(UsuarioRequest usuario) {
        usuarioService.salvar(entityMapper.toUsuario(usuario));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<List<Usuario>> listar() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @Override
    public ResponseEntity<Usuario> buscarPorLogin(String login) {
        return ResponseEntity.ok(usuarioService.buscarPorLogin(login));
    }
}
