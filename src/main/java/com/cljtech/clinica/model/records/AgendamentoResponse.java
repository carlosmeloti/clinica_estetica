package com.cljtech.clinica.model.records;

import com.cljtech.clinica.model.enuns.StatusAgendamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record AgendamentoResponse(Long id,
                                  PacienteRequestResponse paciente,
                                  UsuarioResponse profissional,
                                  List<ProcedimentoRequestResponse> procedimentos,
                                  LocalDateTime dataHoraInicio,
                                  LocalDateTime dataHoraFim,
                                  StatusAgendamento status,
                                  String motivoConsulta,
                                  BigDecimal valorPrevisto) {
}
