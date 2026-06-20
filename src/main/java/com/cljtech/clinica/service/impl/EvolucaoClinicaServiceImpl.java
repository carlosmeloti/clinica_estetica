package com.cljtech.clinica.service.impl;

import com.cljtech.clinica.data.Agendamento;
import com.cljtech.clinica.data.EvolucaoClinica;
import com.cljtech.clinica.data.EvolucaoEstetica;
import com.cljtech.clinica.data.repository.AgendamentoRepository;
import com.cljtech.clinica.data.repository.EvolucaoClinicaRepository;
import com.cljtech.clinica.data.repository.EvolucaoEsteticaRepository;
import com.cljtech.clinica.mapper.EntityMapper;
import com.cljtech.clinica.model.records.EvolucaoEsteticaRequestResponse;
import com.cljtech.clinica.service.EvolucaoEsteticaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class EvolucaoClinicaServiceImpl implements EvolucaoEsteticaService {

    private final EvolucaoEsteticaRepository evolucaoEsteticaRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final EntityMapper entityMapper;

    @Override
    public EvolucaoEsteticaRequestResponse criar(EvolucaoEsteticaRequestResponse request) {

        Agendamento agendamento = agendamentoRepository.findById(request.agendamentoId())
                .orElseThrow(() -> new RuntimeException("Agendamento id " + request.agendamentoId() + " não encontrado."));

        if (agendamento.getProfissional() == null) {
            throw new IllegalStateException("Este agendamento não possui um profissional atribuído no banco de dados.");
        }

        EvolucaoEstetica evolucao = new EvolucaoEstetica();
        evolucao.setAgendamento(agendamento);
        evolucao.setPaciente(agendamento.getPaciente());
        evolucao.setProfissional(agendamento.getProfissional());

        evolucao.setDataRegistro(LocalDateTime.now());
        evolucao.setRelatoClinico(request.relatoClinico());
        evolucao.setNumeroSessao(request.numeroSessao());
        evolucao.setPesoPacienteKg(request.pesoPacienteKg());
        evolucao.setDoseAplicadaMg(request.doseAplicadaMg());


        EvolucaoEstetica evolucaoEstetica = evolucaoEsteticaRepository.save(entityMapper.toEvolucaoClinica(request));
        return entityMapper.toEvolucaoClinicaRequestRessponse(evolucaoEstetica);
    }
}
