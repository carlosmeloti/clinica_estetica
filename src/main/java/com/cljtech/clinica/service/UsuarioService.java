package com.cljtech.clinica.service;

import com.cljtech.clinica.data.Usuario;
import com.cljtech.clinica.model.enuns.PerfilUsuario;
import com.cljtech.clinica.model.records.ProfissionalResponse;
import com.cljtech.clinica.model.records.UsuarioRequest;
import com.cljtech.clinica.model.records.UsuarioResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UsuarioService {
    UsuarioResponse salvar(UsuarioRequest request);
    UsuarioResponse atualizar(Long id, UsuarioRequest request);
    void deletar(Long id);
    UsuarioResponse buscarPorId(Long id);
    UsuarioResponse buscarPorLogin(String login);
    Page<UsuarioResponse> buscarComFiltros(String nome, String email, String login, PerfilUsuario perfil, Pageable pageable);
    List<ProfissionalResponse> listarProfissionais();
}

