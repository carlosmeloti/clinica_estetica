package com.cljtech.clinica.controiller;

import com.cljtech.clinica.model.records.AgendamentoRequestResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/agendamento")
public interface AgendamentoController {

    @PostMapping("/criar")
    ResponseEntity<AgendamentoRequestResponse> criar(@RequestBody  AgendamentoRequestResponse agendamentoRequestResponse);

    @GetMapping("/listar")
    ResponseEntity<List<AgendamentoRequestResponse>> listarPorDiaEProfissional(
            @RequestParam Long profissionalId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data
    );

    @GetMapping("/listar/todos")
    ResponseEntity<List<AgendamentoRequestResponse>> listarTodos();

    @PutMapping("/atualizar/{id}")
    ResponseEntity<AgendamentoRequestResponse> atualizar(@RequestBody @PathVariable Long id,AgendamentoRequestResponse agendamentoRequestResponse);
}
