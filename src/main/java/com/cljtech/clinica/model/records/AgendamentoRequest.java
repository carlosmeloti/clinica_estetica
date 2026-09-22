package com.cljtech.clinica.model.records;

import com.cljtech.clinica.model.enuns.StatusAgendamento;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;

public record AgendamentoRequest(Long id,
                                 @NotNull(message = "{agendamento.paciente.obrigatorio}")
                                 Long pacienteId,
                                 @NotNull(message = "{agendamento.profissional.obrigatorio}")
                                 Long profissionalId,
                                 @NotEmpty(message = "{agendamento.procedimentos.obrigatorios}")
                                 List<ProcedimentoRequestResponse> procedimentos,
                                 @NotNull(message = "{agendamento.dataHoraInicio.obrigatoria}")
                                 @FutureOrPresent(message = "{agendamento.dataHoraInicio.futuro}")
                                 LocalDateTime dataHoraInicio,
                                 @NotNull(message = "{agendamento.dataHoraFim.obrigatoria}")
                                 LocalDateTime dataHoraFim,
                                 StatusAgendamento status,
                                 String motivoConsulta,
                                 BigDecimal valorPrevisto) {
}
