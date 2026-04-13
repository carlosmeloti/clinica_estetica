package com.cljtech.clinica.data;

import com.cljtech.clinica.model.enuns.StatusAgendamento;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Agendamento extends EntidadeBase {

    @ManyToOne(optional = false)
    private Paciente paciente;

    @ManyToOne(optional = false)
    private Usuario profissional;

    @ManyToOne(optional = false)
    private Procedimento procedimento;

    @Column(nullable = false)
    private LocalDateTime dataHoraInicio;

    @Column(nullable = false)
    private LocalDateTime dataHoraFim;

    @Enumerated(EnumType.STRING)
    private StatusAgendamento status;

    private String motivoConsulta;

    private BigDecimal valorPrevisto;

}
