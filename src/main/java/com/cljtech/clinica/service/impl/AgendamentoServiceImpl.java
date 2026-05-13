package com.cljtech.clinica.service.impl;

import com.cljtech.clinica.data.Agendamento;
import com.cljtech.clinica.data.repository.AgendamentoRepository;
import com.cljtech.clinica.mapper.EntityMapper;
import com.cljtech.clinica.model.records.AgendamentoRequestResponse;
import com.cljtech.clinica.service.AgendamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AgendamentoServiceImpl implements AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final EntityMapper entityMapper;
    @Override
    public AgendamentoRequestResponse criar(AgendamentoRequestResponse agendamentoRequestResponse) {
        Agendamento agendamento = agendamentoRepository.save(entityMapper.toAgendamento(agendamentoRequestResponse));
        return entityMapper.toAgendamentoRequestResponse(agendamento);
    }
}
