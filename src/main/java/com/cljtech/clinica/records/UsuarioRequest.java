package com.cljtech.clinica.records;

import com.cljtech.clinica.model.enuns.PerfilUsuario;

public record UsuarioRequest(String login,
                             String senha,
                             String nome,
                             String email,
                             PerfilUsuario perfil,
                             String registroProfissional) {
}
