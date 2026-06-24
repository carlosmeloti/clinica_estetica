package com.cljtech.clinica.service;


import com.cljtech.clinica.data.Paciente;
import com.cljtech.clinica.model.records.PacienteRequestResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PacienteService {

    void salvar(PacienteRequestResponse pacienteRequestResponse);

    PacienteRequestResponse buscar(Long id);

    Page<PacienteRequestResponse> buscarPorCriterios(String nome, String cpf, String email, Pageable pageable);

    Page<PacienteRequestResponse> listar(Pageable pageable);

    void atualizar(PacienteRequestResponse pacienteRequestResponse);

    void deletar(Long id);
}
