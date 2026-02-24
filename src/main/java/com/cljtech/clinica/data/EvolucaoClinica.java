package com.cljtech.clinica.data;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "evolucoes_clinicas")
@Inheritance(strategy = InheritanceType.JOINED)
public class EvolucaoClinica extends EntidadeBase {

    @OneToOne
    @JoinColumn(name = "agendamento_id", nullable = false)
    private Agendamento agendamento;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "profissional_id", nullable = false)
    private Usuario profissional;

    @Column(nullable = false)
    private LocalDateTime dataRegistro;

    @Lob // Para textos longos (anamnese, evolução do dia)
    @Column(columnDefinition = "TEXT")
    private String relatoClinico;

    private String diagnosticoHipotetico;

    // Campos para controle de anexos (fotos de antes/depois, exames)
    // Guardamos apenas o caminho/link do arquivo
    @ElementCollection
    private List<String> linksAnexos;

    // Getters e Setters
}
