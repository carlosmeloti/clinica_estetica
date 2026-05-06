package com.cljtech.clinica.controiller;

import com.cljtech.clinica.data.Paciente;
import com.cljtech.clinica.model.records.PacienteRequestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paciente")
public interface PacienteController {

    @PostMapping("/criar")
    ResponseEntity<Void> criar(@RequestBody PacienteRequestResponse paciente);

    @GetMapping("/buscar/{id}")
    ResponseEntity<Paciente> buscar(@PathVariable Long id);

    @GetMapping("/listar")
    ResponseEntity<Iterable<Paciente>> listar();

    @PutMapping("/atualizar")
    ResponseEntity<Void> atualizar(@RequestBody Paciente paciente);

    @DeleteMapping("/deletar/{id}")
    ResponseEntity<Void> deletar(@PathVariable Long id);
}
