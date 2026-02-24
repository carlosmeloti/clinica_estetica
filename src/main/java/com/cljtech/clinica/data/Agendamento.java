package com.cljtech.clinica.data;

import com.cljtech.clinica.model.enuns.StatusAgendamento;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Agendamento extends EntidadeBase {

    @ManyToOne(optional = false)
    private Paciente paciente;

    @ManyToOne(optional = false)
    private Usuario profissional; // Quem vai atender

    @Column(nullable = false)
    private LocalDateTime dataHoraInicio;

    @Column(nullable = false)
    private LocalDateTime dataHoraFim;

    @Enumerated(EnumType.STRING)
    private StatusAgendamento status; // AGENDADO, CONFIRMADO, CANCELADO, FINALIZADO

    private String motivoConsulta; // Breve descrição
    private BigDecimal valorPrevisto;

    // Campo para linkar com a "Evolução" depois que o atendimento termina
    @OneToOne(mappedBy = "agendamento")
    private EvolucaoClinica evolucao;
}
