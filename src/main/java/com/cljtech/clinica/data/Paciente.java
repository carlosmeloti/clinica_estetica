package com.cljtech.clinica.data;

import jakarta.persistence.Entity;

@Entity
public class Paciente extends Pessoa {
    private String observacoesGerais;
    private String tipoSanguineo;

    // Aqui você pode colocar campos que 90% das clínicas pedem
    private String nomeResponsavel; // Caso seja criança
}
