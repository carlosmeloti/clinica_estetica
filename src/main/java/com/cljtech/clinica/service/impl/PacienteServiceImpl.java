package com.cljtech.clinica.service.impl;

import com.cljtech.clinica.data.repository.PacienteRepository;
import com.cljtech.clinica.mapper.EntityMapper;
import com.cljtech.clinica.model.records.PacienteRequestResponse;
import com.cljtech.clinica.service.PacienteService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cljtech.clinica.exception.RecursoNaoEncontradoException;
import com.cljtech.clinica.exception.RegraNegocioException;

@Service
@RequiredArgsConstructor
@Transactional
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRespository;
    private final EntityMapper entityMapper;

    @Override
    public PacienteRequestResponse salvar(PacienteRequestResponse request) {
        if (pacienteRespository.existsByCpf(request.cpf())) {
            throw new RegraNegocioException("Já existe um paciente cadastrado com este CPF.");
        }
        if (pacienteRespository.existsByEmail(request.email())) {
            throw new RegraNegocioException("Já existe um paciente cadastrado com este e-mail.");
        }
        return entityMapper.toPacienteRequestResponse(
                pacienteRespository.save(entityMapper.toPaciente(request))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PacienteRequestResponse buscar(Long id) {
        return entityMapper.toPacienteRequestResponse(
                pacienteRespository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Paciente não encontrado.")));
    }

    @Override
    public Page<PacienteRequestResponse> buscarPorCriterios(String nome, String cpf, String email, Pageable pageable) {
        if (nome == null && cpf == null && email == null) {
            return listar(pageable);
        }
        return pacienteRespository.findByCriterios(nome, cpf, email, pageable)
                .map(entityMapper::toPacienteRequestResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PacienteRequestResponse> listar(Pageable pageable) {
        return pacienteRespository.findAll(pageable).map(entityMapper::toPacienteRequestResponse);
    }

    @Override
    public PacienteRequestResponse atualizar(Long id, PacienteRequestResponse request) {
        if (!pacienteRespository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Paciente não encontrado.");
        }
        
        // Verifica se o CPF/Email já pertence a outro paciente
        pacienteRespository.findByCriterios(null, request.cpf(), null, Pageable.unpaged())
                .getContent().stream()
                .filter(p -> !p.getId().equals(id))
                .findAny()
                .ifPresent(p -> { throw new RegraNegocioException("CPF já cadastrado para outro paciente."); });

        pacienteRespository.findByCriterios(null, null, request.email(), Pageable.unpaged())
                .getContent().stream()
                .filter(p -> !p.getId().equals(id))
                .findAny()
                .ifPresent(p -> { throw new RegraNegocioException("E-mail já cadastrado para outro paciente."); });

        var paciente = entityMapper.toPaciente(request);
        paciente.setId(id);
        return entityMapper.toPacienteRequestResponse(pacienteRespository.save(paciente));
    }

    @Override
    public void deletar(Long id) {
        if (!pacienteRespository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Paciente não encontrado.");
        }
        pacienteRespository.deleteById(id);
    }
}
