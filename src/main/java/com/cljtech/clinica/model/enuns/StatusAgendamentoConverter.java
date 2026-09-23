package com.cljtech.clinica.model.enuns;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StatusAgendamentoConverter implements AttributeConverter<StatusAgendamento, String> {

    @Override
    public String convertToDatabaseColumn(StatusAgendamento attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public StatusAgendamento convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return null;
        }
        return switch (dbData) {
            case "PENDENTE" -> StatusAgendamento.AGENDADO;
            case "FINALIZADO" -> StatusAgendamento.CONCLUIDO;
            case "AUSENTE" -> StatusAgendamento.NAO_COMPARECEU;
            default -> StatusAgendamento.valueOf(dbData);
        };
    }
}
