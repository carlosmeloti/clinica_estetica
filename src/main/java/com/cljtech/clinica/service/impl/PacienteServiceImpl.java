package com.cljtech.clinica.service.impl;

import com.cljtech.clinica.data.repository.PacienteRespository;
import com.cljtech.clinica.mapper.EntityMapper;
import com.cljtech.clinica.model.records.PacienteRequestResponse;
import com.cljtech.clinica.service.PacienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRespository pacienteRespository;
    private final EntityMapper entityMapper;

    @Override
    public void salvar(PacienteRequestResponse request) {
        pacienteRespository.save(entityMapper.toPaciente(request));
    }

    @Override
    public PacienteRequestResponse buscar(Long id) {
        return entityMapper.toPacienteRequestResponse(
                pacienteRespository.findById(id).orElseThrow(() -> new RuntimeException("Paciente não encontrado.")));
    }

    @Override
    public Page<PacienteRequestResponse> listar(Pageable pageable) {
        return pacienteRespository.findAll(pageable).map(entityMapper::toPacienteRequestResponse);
    }

    @Override
    public void atualizar(PacienteRequestResponse pacienteRequestResponse) {
        pacienteRespository.save(entityMapper.toPaciente(pacienteRequestResponse));
    }
    @Override
    public void deletar(Long id) {

    }
}
