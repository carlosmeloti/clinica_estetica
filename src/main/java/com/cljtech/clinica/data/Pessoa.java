package com.cljtech.clinica.data;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) // Cria tabelas separadas para os detalhes
@Data
public abstract class Pessoa extends EntidadeBase {

    private String nome;
    @Column(unique = true)
    private String cpf;
    private String email;
    private String telefone;
    private LocalDate dataNascimento;
    @Embedded
    private Endereco endereco;
}