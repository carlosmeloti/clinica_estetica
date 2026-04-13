package com.cljtech.clinica.data;

import jakarta.persistence.Entity;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class Procedimento extends EntidadeBase {
    private String nome;
    private BigDecimal precoSugerido;
    private Integer duracaoMinutos;

}

