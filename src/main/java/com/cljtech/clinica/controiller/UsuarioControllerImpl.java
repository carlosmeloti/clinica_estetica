package com.cljtech.clinica.controiller;

import com.cljtech.clinica.data.Usuario;
import com.cljtech.clinica.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UsuarioControllerImpl implements UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioControllerImpl(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public ResponseEntity<Usuario> criar(Usuario usuario) {
        return ResponseEntity.ok(usuarioService.salvar(usuario));
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
