package com.cljtech.clinica.data;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "evolucoes_clinicas")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
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

    @Lob
    @Column(columnDefinition = "TEXT")
    private String relatoClinico;

    private String diagnosticoHipotetico;

}
