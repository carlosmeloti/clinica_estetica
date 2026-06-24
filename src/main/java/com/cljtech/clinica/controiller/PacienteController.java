package com.cljtech.clinica.controiller;

import com.cljtech.clinica.data.Paciente;
import com.cljtech.clinica.model.records.PacienteRequestResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paciente")
public interface PacienteController {

    @PostMapping("/criar")
    ResponseEntity<Void> criar(@RequestBody PacienteRequestResponse paciente);

    @GetMapping("/buscar/{id}")
    ResponseEntity<PacienteRequestResponse> buscar(@PathVariable Long id);

    @GetMapping("/criterios")
    ResponseEntity<Page<PacienteRequestResponse>> buscarPorCriterios(@RequestParam String nome, String cpf, String email, Pageable pageable);

    @GetMapping("/listar")
    ResponseEntity<Page<PacienteRequestResponse>> listar(Pageable pageable);

    @PutMapping("/atualizar")
    ResponseEntity<Void> atualizar(@RequestBody PacienteRequestResponse paciente);

    @DeleteMapping("/deletar/{id}")
    ResponseEntity<Void> deletar(@PathVariable Long id);
}
