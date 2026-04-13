package com.cljtech.clinica.data;


import com.cljtech.clinica.model.enuns.PerfilUsuario;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "usuarios")
@Data
public class Usuario extends EntidadeBase {

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private String nome;

    private String email;

    @Enumerated(EnumType.STRING)
    private PerfilUsuario perfil; // ADMIN, MEDICO, RECEPCAO

    private String registroProfissional;
}
