package com.cljtech.clinica.model.records;

import com.cljtech.clinica.model.enuns.PerfilUsuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioRequest(
        @NotBlank(message = "{login.obrigatorio}")
        String login,

        String senha,

        @NotBlank(message = "{nome.obrigatorio}")
        String nome,

        @Email(message = "{email.invalido}")
        String email,

        @NotNull(message = "{perfil.obrigatorio}")
        PerfilUsuario perfil,

        String registroProfissional) {
}
