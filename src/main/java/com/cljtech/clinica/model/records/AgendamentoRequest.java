package com.cljtech.clinica.model.records;

import com.cljtech.clinica.model.enuns.StatusAgendamento;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record AgendamentoRequest(Long id,
                                 @NotNull(message = "O paciente é obrigatório.")
                                 Long pacienteId,
                                 @NotNull(message = "O profissional é obrigatório.")
                                 Long profissionalId,
                                 @NotNull(message = "O procedimento é obrigatório.")
                                 List<ProcedimentoRequestResponse> procedimentos,
                                 @NotNull(message = "A data e hora de início é obrigatória.")
                                 @FutureOrPresent(message = "A data e hora de início não pode estar no passado.")
                                 LocalDateTime dataHoraInicio,
                                 @NotNull(message = "A data e hora de fim é obrigatória.")
                                 LocalDateTime dataHoraFim,
                                 StatusAgendamento status,
                                 String motivoConsulta,
                                 BigDecimal valorPrevisto) {
}
