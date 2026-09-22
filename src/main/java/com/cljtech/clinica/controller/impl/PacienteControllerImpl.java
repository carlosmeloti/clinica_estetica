package com.cljtech.clinica.controller.impl;

import com.cljtech.clinica.controller.PacienteController;
import com.cljtech.clinica.model.records.PacienteRequestResponse;
import com.cljtech.clinica.model.records.PacienteResumoResponse;
import com.cljtech.clinica.service.PacienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PacienteControllerImpl implements PacienteController {

    public final PacienteService pacienteService;

    @Override
    public ResponseEntity<PacienteRequestResponse> criar(PacienteRequestResponse paciente) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteService.salvar(paciente));
    }

    @Override
    public ResponseEntity<PacienteRequestResponse> buscar(Long id) {
        return ResponseEntity.ok(pacienteService.buscar(id));
    }

    @Override
    public ResponseEntity<Page<PacienteRequestResponse>> buscarPorCriterios(String nome, String cpf, String email, Pageable pageable) {
       Page<PacienteRequestResponse> pacientes = pacienteService.buscarPorCriterios(nome, cpf, email, pageable);
       return ResponseEntity.ok(pacientes);
    }

    @Override
    public ResponseEntity<Page<PacienteResumoResponse>> autocomplete(String nome, String cpf, String email, Pageable pageable) {
        Page<PacienteResumoResponse> pacientes = pacienteService.buscarResumoPorCriterios(nome, cpf, email, pageable);
        return ResponseEntity.ok(pacientes);
    }

    @Override
    public ResponseEntity<Page<PacienteRequestResponse>> listar(Pageable pageable) {
        return ResponseEntity.ok(pacienteService.listar(pageable));
    }

    @Override
    public ResponseEntity<PacienteRequestResponse> atualizar(Long id, PacienteRequestResponse paciente) {
        return ResponseEntity.ok(pacienteService.atualizar(id, paciente));
    }

    @Override
    public ResponseEntity<Void> deletar(Long id) {
        pacienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
