package com.cljtech.clinica.data;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

import java.util.List;

public class EvolucaoEstetica extends EvolucaoClinica{
    @ManyToMany
    @JoinTable(
            name = "evolucao_locais",
            joinColumns = @JoinColumn(name = "evolucao_id"),
            inverseJoinColumns = @JoinColumn(name = "local_id")
    )
    private List<LocalAplicacao> locaisAplicados;
    private Integer numeroSessao;
    private Double doseAplicadaMg;
    private Double pesoPacienteKg;
}
