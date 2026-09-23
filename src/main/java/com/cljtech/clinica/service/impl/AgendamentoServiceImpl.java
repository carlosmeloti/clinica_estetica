package com.cljtech.clinica.service.impl;

import com.cljtech.clinica.data.Agendamento;
import com.cljtech.clinica.data.Paciente;
import com.cljtech.clinica.data.Procedimento;
import com.cljtech.clinica.data.Usuario;
import com.cljtech.clinica.data.repository.AgendamentoRepository;
import com.cljtech.clinica.data.repository.PacienteRepository;
import com.cljtech.clinica.data.repository.ProcedimentoRepository;
import com.cljtech.clinica.data.repository.UsuarioRepository;
import com.cljtech.clinica.mapper.EntityMapper;
import com.cljtech.clinica.model.enuns.PerfilUsuario;
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

import com.cljtech.clinica.exception.ConflitoException;
import com.cljtech.clinica.exception.RecursoNaoEncontradoException;
import com.cljtech.clinica.exception.RegraNegocioException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
@Transactional
public class AgendamentoServiceImpl implements AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final PacienteRepository pacienteRespository;
    private final UsuarioRepository usuarioRepository;
    private final ProcedimentoRepository procedimentoRepository;
    private final EntityMapper entityMapper;

    @Override
    public AgendamentoResponse criar(AgendamentoRequest agendamentoRequest) {
        validarAgendamento(agendamentoRequest, null);

        Paciente paciente = pacienteRespository.findById(agendamentoRequest.pacienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Paciente não encontrado"));
        Usuario profissional = usuarioRepository.findById(agendamentoRequest.profissionalId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Profissional não encontrado"));

        if (profissional.getPerfil() != PerfilUsuario.PROFISSIONAL) {
            throw new RegraNegocioException("O usuário selecionado não tem perfil de profissional.");
        }
        
        List<Procedimento> procedimentos = buscarProcedimentos(agendamentoRequest.procedimentos());

        Agendamento agendamento = entityMapper.toAgendamento(agendamentoRequest);
        agendamento.setPaciente(paciente);
        agendamento.setProfissional(profissional);
        agendamento.setProcedimentos(procedimentos);
        agendamento.setStatus(StatusAgendamento.AGENDADO);

        return entityMapper.toAgendamentoRequestResponse(agendamentoRepository.save(agendamento));
    }

    private List<Procedimento> buscarProcedimentos(List<ProcedimentoRequestResponse> requests) {
        if (requests == null || requests.isEmpty()) {
            throw new RegraNegocioException("A lista de procedimentos não pode estar vazia.");
        }
        List<Long> ids = requests.stream().map(ProcedimentoRequestResponse::id).toList();
        List<Procedimento> procedimentos = procedimentoRepository.findAllByIdIn(ids);
        if (procedimentos.size() != ids.size()) {
            throw new RecursoNaoEncontradoException("Um ou mais procedimentos informados não foram encontrados.");
        }
        return procedimentos;
    }

    private void validarAgendamento(AgendamentoRequest request, Long idParaIgnorar) {
        if (request.dataHoraInicio() == null || request.dataHoraFim() == null) {
            throw new RegraNegocioException("Data/hora de início e fim são obrigatórias.");
        }
        if (request.dataHoraInicio().isAfter(request.dataHoraFim())) {
            throw new RegraNegocioException("A data de início deve ser anterior à data de fim.");
        }
        
        boolean conflito = agendamentoRepository.existeAgendamentoNoMesmoHorario(
                request.profissionalId(),
                request.dataHoraInicio(),
                request.dataHoraFim(),
                StatusAgendamento.CANCELADO,
                idParaIgnorar
        );

        if (conflito) {
            throw new ConflitoException("Já existe um agendamento para este profissional no horário selecionado.");
        }
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
    public Page<AgendamentoResponse> listarTodos(Pageable pageable) {
        return agendamentoRepository.findAll(pageable)
                .map(entityMapper::toAgendamentoRequestResponse);
    }

    @Override
    public AgendamentoResponse atualizar(Long id, AgendamentoRequest request) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Agendamento não encontrado"));

        if (agendamento.getStatus() == StatusAgendamento.CANCELADO || agendamento.getStatus() == StatusAgendamento.CONCLUIDO) {
            throw new RegraNegocioException("Não é possível alterar um agendamento cancelado ou concluído.");
        }

        validarAgendamento(request, id);

        if (request.pacienteId() != null && !request.pacienteId().equals(agendamento.getPaciente().getId())) {
            Paciente paciente = pacienteRespository.findById(request.pacienteId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Paciente não encontrado"));
            agendamento.setPaciente(paciente);
        }

        if (request.profissionalId() != null && !request.profissionalId().equals(agendamento.getProfissional().getId())) {
            Usuario profissional = usuarioRepository.findById(request.profissionalId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Profissional não encontrado"));

            if (profissional.getPerfil() != PerfilUsuario.PROFISSIONAL) {
                throw new RegraNegocioException("O usuário selecionado não tem perfil de profissional.");
            }

            agendamento.setProfissional(profissional);
        }

        if (request.procedimentos() != null) {
            agendamento.setProcedimentos(buscarProcedimentos(request.procedimentos()));
        }

        entityMapper.updateAgendamentoFromRequest(request, agendamento);
        return entityMapper.toAgendamentoRequestResponse(agendamentoRepository.save(agendamento));
    }

    @Override
    public AgendamentoResponse confirmar(Long id) {
        return mudarStatus(id, StatusAgendamento.CONFIRMADO);
    }

    @Override
    public AgendamentoResponse cancelar(Long id) {
        return mudarStatus(id, StatusAgendamento.CANCELADO);
    }

    @Override
    public AgendamentoResponse concluir(Long id) {
        return mudarStatus(id, StatusAgendamento.CONCLUIDO);
    }

    @Override
    public AgendamentoResponse naoCompareceu(Long id) {
        return mudarStatus(id, StatusAgendamento.NAO_COMPARECEU);
    }

    @Override
    public AgendamentoResponse mudarStatus(Long id, StatusAgendamento novoStatus) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Agendamento não encontrado"));
        
        if (novoStatus == StatusAgendamento.EM_ATENDIMENTO) {
            if (agendamento.getStatus() == StatusAgendamento.CANCELADO || 
                agendamento.getStatus() == StatusAgendamento.CONCLUIDO ||
                agendamento.getStatus() == StatusAgendamento.NAO_COMPARECEU) {
                throw new RegraNegocioException("Não é possível iniciar atendimento para agendamento " + agendamento.getStatus());
            }
        }

        agendamento.setStatus(novoStatus);
        return entityMapper.toAgendamentoRequestResponse(agendamentoRepository.save(agendamento));
    }

    @Override
    public List<AgendamentoResponse> listarPorStatus(StatusAgendamento status) {
        return agendamentoRepository.findByStatus(status)
                .stream()
                .map(entityMapper::toAgendamentoRequestResponse)
                .toList();
    }
}
