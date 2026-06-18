package com.cljtech.clinica.service.impl;

import com.cljtech.clinica.data.Agendamento;
import com.cljtech.clinica.data.repository.AgendamentoRepository;
import com.cljtech.clinica.mapper.EntityMapper;
import com.cljtech.clinica.model.records.AgendamentoRequestResponse;
import com.cljtech.clinica.service.AgendamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AgendamentoServiceImpl implements AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final EntityMapper entityMapper;
    @Override
    public AgendamentoRequestResponse criar(AgendamentoRequestResponse agendamentoRequestResponse) {
        Agendamento agendamento = agendamentoRepository.save(entityMapper.toAgendamento(agendamentoRequestResponse));
        return entityMapper.toAgendamentoRequestResponse(agendamento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgendamentoRequestResponse> listarPorDiaEProfissional(Long profissionalId, LocalDate data) {
        return agendamentoRepository.findByProfissionalIdAndDataHoraInicioBetween(
                        profissionalId,
                        data.atStartOfDay(),
                        data.atTime(LocalTime.MAX)
                )
                .stream()
                .map(entityMapper::toAgendamentoRequestResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgendamentoRequestResponse> listarTodos() {
        return agendamentoRepository.findAll()
                .stream()
                .map(entityMapper::toAgendamentoRequestResponse)
                .toList();
    }

    @Override
    public AgendamentoRequestResponse atualizar(Long id, AgendamentoRequestResponse agendamentoRequestResponse) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));

        entityMapper.updateAgendamentoFromRequest(agendamentoRequestResponse, agendamento);

        Agendamento salvo = agendamentoRepository.save(agendamento);

        return entityMapper.toAgendamentoRequestResponse(salvo);
    }
}
