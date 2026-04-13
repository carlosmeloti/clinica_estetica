package com.cljtech.clinica.data;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class Paciente extends Pessoa {
    private String observacoesGerais;
    private String tipoSanguineo;
    private String nomeResponsavel;
}
