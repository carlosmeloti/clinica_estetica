package com.cljtech.clinica.service;

import com.cljtech.clinica.data.Usuario;
import com.cljtech.clinica.model.records.UsuarioResponse;

import java.util.List;


public interface UsuarioService {
    void salvar(Usuario usuario);
    List<UsuarioResponse> listarTodos();
    UsuarioResponse buscarPorLogin(String login);
}

