package com.cljtech.clinica.service.impl;

import com.cljtech.clinica.data.Paciente;
import com.cljtech.clinica.model.records.PacienteRequestResponse;
import com.cljtech.clinica.service.PacienteService;
import org.springframework.stereotype.Service;

@Service
public class PacienteServiceImpl implements PacienteService {
    @Override
    public void criar(PacienteRequestResponse paciente) {

    }

    @Override
    public Paciente buscar(Long id) {
        return null;
    }

    @Override
    public Iterable<Paciente> listar() {
        return null;
    }

    @Override
    public void atualizar(Paciente paciente) {

    }

    @Override
    public void deletar(Long id) {

    }
}
