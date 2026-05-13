package com.cljtech.clinica.model.records;

import com.cljtech.clinica.model.enuns.StatusAgendamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AgendamentoRequestResponse(Long id,
                                         Long pacienteId,
                                         Long profissionalId,
                                         Long procedimentoId,
                                         LocalDateTime dataHoraInicio,
                                         LocalDateTime dataHoraFim,
                                         StatusAgendamento status,
                                         String motivoConsulta,
                                         BigDecimal valorPrevisto) {
}
