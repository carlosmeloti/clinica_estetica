package com.cljtech.clinica.service;


import com.cljtech.clinica.data.Paciente;
import com.cljtech.clinica.model.records.PacienteRequestResponse;

public interface PacienteService {

    void criar(PacienteRequestResponse paciente);

    Paciente buscar(Long id);

    Iterable<Paciente> listar();

    void atualizar(Paciente paciente);

    void deletar(Long id);
}
