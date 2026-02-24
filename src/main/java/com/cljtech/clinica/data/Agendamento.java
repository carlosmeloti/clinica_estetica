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
    private Usuario profissional;

    @Column(nullable = false)
    private LocalDateTime dataHoraInicio;

    @Column(nullable = false)
    private LocalDateTime dataHoraFim;

    @Enumerated(EnumType.STRING)
    private StatusAgendamento status;

    private String motivoConsulta;
    private BigDecimal valorPrevisto;

    @OneToOne(mappedBy = "agendamento")
    private EvolucaoClinica evolucao;
}
