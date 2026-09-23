package com.cljtech.clinica.controller.impl;

import com.cljtech.clinica.controller.ProfissionalController;
import com.cljtech.clinica.exception.RegraNegocioException;
import com.cljtech.clinica.model.enuns.PerfilUsuario;
import com.cljtech.clinica.model.records.ProfissionalResponse;
import com.cljtech.clinica.model.records.UsuarioRequest;
import com.cljtech.clinica.model.records.UsuarioResponse;
import com.cljtech.clinica.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProfissionalControllerImpl implements ProfissionalController {

    private final UsuarioService usuarioService;

    @Override
    public ResponseEntity<UsuarioResponse> criar(UsuarioRequest request) {
        if (request.perfil() != PerfilUsuario.PROFISSIONAL) {
            throw new RegraNegocioException("O perfil informado deve ser PROFISSIONAL.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.salvar(request));
    }

    @Override
    public ResponseEntity<List<ProfissionalResponse>> listar() {
        return ResponseEntity.ok(usuarioService.listarProfissionais());
    }

    @Override
    public ResponseEntity<UsuarioResponse> buscarPorId(Long id) {
        UsuarioResponse usuario = usuarioService.buscarPorId(id);
        if (!PerfilUsuario.PROFISSIONAL.name().equals(usuario.perfil())) {
            throw new RegraNegocioException("O usuário informado não é um profissional.");
        }
        return ResponseEntity.ok(usuario);
    }
}
