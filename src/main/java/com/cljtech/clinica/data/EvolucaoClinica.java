package com.cljtech.clinica.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "evolucoes_clinicas")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public class EvolucaoClinica extends EntidadeBase {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agendamento_id", nullable = false, unique = true)
    private Agendamento agendamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profissional_id", nullable = false)
    private Usuario profissional;

    @Column(name = "data_registro", nullable = false)
    private LocalDateTime dataRegistro;

    @Lob
    @Column(name = "relato_clinico", nullable = false, columnDefinition = "TEXT")
    private String relatoClinico;

    @Column(name = "diagnostico_hipotetico")
    private String diagnosticoHipotetico;

}
