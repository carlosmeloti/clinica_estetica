package com.cljtech.clinica.service.impl;

import com.cljtech.clinica.data.Paciente;
import com.cljtech.clinica.data.repository.PacienteRepository;
import com.cljtech.clinica.mapper.EntityMapper;
import com.cljtech.clinica.model.records.PacienteRequestResponse;
import com.cljtech.clinica.model.records.PacienteResumoResponse;
import com.cljtech.clinica.service.PacienteService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cljtech.clinica.exception.RecursoNaoEncontradoException;
import com.cljtech.clinica.exception.RegraNegocioException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        return pacienteRespository.findAll(criarSpecification(nome, cpf, email), pageable)
                .map(entityMapper::toPacienteRequestResponse);
    }

    @Override
    public Page<PacienteResumoResponse> buscarResumoPorCriterios(String nome, String cpf, String email, Pageable pageable) {
        return pacienteRespository.findAll(criarSpecification(nome, cpf, email), pageable)
                .map(entityMapper::toPacienteResumoResponse);
    }

    private Specification<Paciente> criarSpecification(String nome, String cpf, String email) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            Optional.ofNullable(nome)
                    .map(String::trim)
                    .filter(n -> !n.isEmpty())
                    .ifPresent(n -> predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("nome")), "%" + n.toLowerCase() + "%")));

            Optional.ofNullable(cpf)
                    .map(String::trim)
                    .filter(c -> !c.isEmpty())
                    .ifPresent(c -> predicates.add(criteriaBuilder.equal(root.get("cpf"), c)));

            Optional.ofNullable(email)
                    .map(String::trim)
                    .filter(e -> !e.isEmpty())
                    .ifPresent(e -> predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + e.toLowerCase() + "%")));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
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
        pacienteRespository.findAll(criarSpecification(null, request.cpf(), null), Pageable.unpaged())
                .getContent().stream()
                .filter(p -> !p.getId().equals(id))
                .findAny()
                .ifPresent(p -> { throw new RegraNegocioException("CPF já cadastrado para outro paciente."); });

        pacienteRespository.findAll(criarSpecification(null, null, request.email()), Pageable.unpaged())
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
