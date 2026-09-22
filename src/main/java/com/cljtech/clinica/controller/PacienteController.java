package com.cljtech.clinica.controller;

import com.cljtech.clinica.data.Paciente;
import com.cljtech.clinica.model.records.PacienteRequestResponse;
import com.cljtech.clinica.model.records.PacienteResumoResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/paciente", "/pacientes"})
public interface PacienteController {

    @PostMapping("/criar")
    ResponseEntity<PacienteRequestResponse> criar(@RequestBody @Valid PacienteRequestResponse paciente);

    @GetMapping("/buscar/{id}")
    ResponseEntity<PacienteRequestResponse> buscar(@PathVariable Long id);

    @GetMapping("/criterios")
    ResponseEntity<Page<PacienteRequestResponse>> buscarPorCriterios(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String email,
            Pageable pageable);

    @GetMapping("/autocomplete")
    ResponseEntity<Page<PacienteResumoResponse>> autocomplete(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String email,
            Pageable pageable);

    @GetMapping("/listar")
    ResponseEntity<Page<PacienteRequestResponse>> listar(Pageable pageable);

    @PutMapping("/atualizar/{id}")
    ResponseEntity<PacienteRequestResponse> atualizar(@PathVariable Long id, @RequestBody @Valid PacienteRequestResponse paciente);

    @DeleteMapping("/deletar/{id}")
    ResponseEntity<Void> deletar(@PathVariable Long id);
}
