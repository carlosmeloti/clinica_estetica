package com.cljtech.clinica.service;


import com.cljtech.clinica.data.Paciente;
import com.cljtech.clinica.model.records.PacienteRequestResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PacienteService {

    void salvar(PacienteRequestResponse pacienteRequestResponse);

    PacienteRequestResponse buscar(Long id);

    Page<PacienteRequestResponse> listar(Pageable pageable);

    void atualizar(PacienteRequestResponse pacienteRequestResponse);

    void deletar(Long id);
}
