package com.cljtech.clinica.data;


import com.cljtech.clinica.model.enuns.PerfilUsuario;
import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario extends EntidadeBase {

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String senha; // Lembre-se: salvar sempre com BCrypt!

    @Column(nullable = false)
    private String nome;

    private String email;

    @Enumerated(EnumType.STRING)
    private PerfilUsuario perfil; // ADMIN, MEDICO, RECEPCAO

    // Se o usuário for um profissional de saúde, vinculamos o registro dele
    private String registroProfissional; // CRM, CRO, etc.
}
