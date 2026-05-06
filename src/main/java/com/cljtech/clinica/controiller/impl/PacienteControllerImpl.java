package com.cljtech.clinica.controiller.impl;

import com.cljtech.clinica.controiller.PacienteController;
import com.cljtech.clinica.data.Paciente;
import com.cljtech.clinica.model.records.PacienteRequestResponse;
import com.cljtech.clinica.service.PacienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PacienteControllerImpl implements PacienteController {

    public final PacienteService pacienteService;

    @Override
    public ResponseEntity<Void> criar(PacienteRequestResponse paciente) {
        return null;
    }

    @Override
    public ResponseEntity<Paciente> buscar(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<Iterable<Paciente>> listar() {
        return null;
    }

    @Override
    public ResponseEntity<Void> atualizar(Paciente paciente) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deletar(Long id) {
        return null;
    }
}
