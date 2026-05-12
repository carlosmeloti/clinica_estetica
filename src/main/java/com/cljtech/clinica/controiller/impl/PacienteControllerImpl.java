package com.cljtech.clinica.controiller.impl;

import com.cljtech.clinica.controiller.PacienteController;
import com.cljtech.clinica.data.Paciente;
import com.cljtech.clinica.model.records.PacienteRequestResponse;
import com.cljtech.clinica.service.PacienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PacienteControllerImpl implements PacienteController {

    public final PacienteService pacienteService;

    @Override
    public ResponseEntity<Void> criar(PacienteRequestResponse paciente) {
        pacienteService.salvar(paciente);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<PacienteRequestResponse> buscar(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<Page<PacienteRequestResponse>> listar(Pageable pageable) {
        return null;
    }

    @Override
    public ResponseEntity<Void> atualizar(PacienteRequestResponse paciente) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deletar(Long id) {
        return null;
    }
}
