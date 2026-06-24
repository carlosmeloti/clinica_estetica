package com.cljtech.clinica.model.records;

import com.cljtech.clinica.model.enuns.PerfilUsuario;


public record TokenResponse(String token, String nome, PerfilUsuario perfil) {}
