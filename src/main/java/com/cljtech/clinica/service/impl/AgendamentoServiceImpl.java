package com.cljtech.clinica.service.impl;

import com.cljtech.clinica.data.Agendamento;
import com.cljtech.clinica.data.Paciente;
import com.cljtech.clinica.data.Procedimento;
import com.cljtech.clinica.data.Usuario;
import com.cljtech.clinica.data.repository.AgendamentoRepository;
import com.cljtech.clinica.data.repository.PacienteRespository;
import com.cljtech.clinica.data.repository.ProcedimentoRepository;
import com.cljtech.clinica.data.repository.UsuarioRepository;
import com.cljtech.clinica.mapper.EntityMapper;
import com.cljtech.clinica.model.enuns.StatusAgendamento;
import com.cljtech.clinica.model.records.AgendamentoRequest;
import com.cljtech.clinica.model.records.AgendamentoResponse;
import com.cljtech.clinica.model.records.ProcedimentoRequestResponse;
import com.cljtech.clinica.service.AgendamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AgendamentoServiceImpl implements AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final PacienteRespository pacienteRespository;
    private final UsuarioRepository usuarioRepository;
    private final ProcedimentoRepository procedimentoRepository;
    private final EntityMapper entityMapper;

    @Override
    public AgendamentoResponse criar(AgendamentoRequest agendamentoRequest) {

        boolean existeAgendamentoNoMesmoHorario = agendamentoRepository.existeAgendamentoNoMesmoHorario(
                agendamentoRequest.profissionalId(),
                agendamentoRequest.dataHoraInicio(),
                agendamentoRequest.dataHoraFim()
        );

        if (existeAgendamentoNoMesmoHorario) {
            throw new RuntimeException("Já existe um agendamento no mesmo horário.");
        }

        Paciente paciente = pacienteRespository.findById(agendamentoRequest.pacienteId()).orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
        Usuario profissional = usuarioRepository.findById(agendamentoRequest.profissionalId()).orElseThrow(() ->  new RuntimeException("Profissional não encontrado"));
        List<Procedimento> procedimentos = procedimentoRepository.findAllByIdIn(agendamentoRequest.procedimentos()
                .stream()
                .map(ProcedimentoRequestResponse::id)
                .collect(Collectors
                        .toList()));

        if (procedimentos.size() != agendamentoRequest.procedimentos().size()) {
            throw new RuntimeException("Um ou mais procedimentos informados não foram encontrados.");
        }

        Agendamento agendamento = entityMapper.toAgendamento(agendamentoRequest);
        agendamento.setPaciente(paciente);
        agendamento.setProfissional(profissional);
        agendamento.setProcedimentos(procedimentos);

        agendamentoRepository.save(agendamento);

        return entityMapper.toAgendamentoRequestResponse(agendamento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listarPorDiaEProfissional(Long profissionalId, LocalDate data) {
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
    public List<AgendamentoResponse> listarTodos() {
        return agendamentoRepository.findAll()
                .stream()
                .map(entityMapper::toAgendamentoRequestResponse)
                .toList();
    }

    @Override
    public AgendamentoResponse atualizar(Long id, AgendamentoRequest agendamentoRequest) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));

        entityMapper.updateAgendamentoFromRequest(agendamentoRequest, agendamento);

        Agendamento salvo = agendamentoRepository.save(agendamento);

        return entityMapper.toAgendamentoRequestResponse(salvo);
    }

    @Override
    public List<AgendamentoResponse> listarPorStatus(StatusAgendamento status) {
        return agendamentoRepository.findByStatus(status)
                .stream()
                .map(entityMapper::toAgendamentoRequestResponse)
                .toList();
    }
}
