package com.cljtech.clinica.model.records;

import java.math.BigDecimal;

public record ProcedimentoRequestResponse(Long id, String nome, BigDecimal precoSugerido, Integer duracaoMinutos) {
}
