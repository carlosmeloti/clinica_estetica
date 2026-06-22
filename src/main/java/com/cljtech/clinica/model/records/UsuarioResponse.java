package com.cljtech.clinica.model.records;

public record UsuarioResponse(Long id, String nome, String email, String login, String perfil, String registroProfissional) {
}
