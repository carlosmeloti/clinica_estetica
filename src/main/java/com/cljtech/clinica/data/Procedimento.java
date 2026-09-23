package com.cljtech.clinica.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class Procedimento extends EntidadeBase {

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precoSugerido;

    private Integer duracaoMinutos;

}

