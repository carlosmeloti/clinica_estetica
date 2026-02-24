package com.cljtech.clinica.data;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class EntidadeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();
    private LocalDateTime dataAtualizacao;
    private Boolean ativo = true;
}