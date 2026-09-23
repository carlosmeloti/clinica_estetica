package com.cljtech.clinica.controller.impl;

import com.cljtech.clinica.controller.UsuarioController;
import com.cljtech.clinica.model.enuns.PerfilUsuario;
import com.cljtech.clinica.model.records.UsuarioRequest;
import com.cljtech.clinica.model.records.UsuarioResponse;
import com.cljtech.clinica.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UsuarioControllerImpl implements UsuarioController {

    private final UsuarioService usuarioService;

    @Override
    public ResponseEntity<UsuarioResponse> criar(UsuarioRequest usuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.salvar(usuario));
    }

    @Override
    public ResponseEntity<UsuarioResponse> atualizar(Long id, UsuarioRequest usuario) {
        return ResponseEntity.ok(usuarioService.atualizar(id, usuario));
    }

    @Override
    public ResponseEntity<Void> deletar(Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<UsuarioResponse> buscarPorId(Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @Override
    public ResponseEntity<Page<UsuarioResponse>> buscar(String nome, String email, String login, PerfilUsuario perfil, Pageable pageable) {
        return ResponseEntity.ok(usuarioService.buscarComFiltros(nome, email, login, perfil, pageable));
    }
}
