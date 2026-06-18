package com.cljtech.clinica.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "evolucoes_esteticas", schema = "clinica")
@Getter
@Setter
@NoArgsConstructor
public class EvolucaoEstetica extends EvolucaoClinica {

    @ManyToMany
    @JoinTable(
            name = "evolucao_locais",
            schema = "clinica",
            joinColumns = @JoinColumn(name = "evolucao_id"),
            inverseJoinColumns = @JoinColumn(name = "local_id")
    )
    private List<LocalAplicacao> locaisAplicados;

    @Column(name = "numero_sessao")
    private Integer numeroSessao;

    @Column(name = "dose_aplicada_mg")
    private Double doseAplicadaMg;

    @Column(name = "peso_paciente_kg")
    private Double pesoPacienteKg;
}
