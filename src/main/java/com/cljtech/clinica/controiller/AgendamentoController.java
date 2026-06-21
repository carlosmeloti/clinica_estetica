package com.cljtech.clinica.controiller;

import com.cljtech.clinica.model.enuns.StatusAgendamento;
import com.cljtech.clinica.model.records.AgendamentoRequestResponse;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/agendamento")
public interface AgendamentoController {

    @PostMapping("/criar")
    ResponseEntity<AgendamentoRequestResponse> criar(@RequestBody @Valid AgendamentoRequestResponse agendamentoRequestResponse);

    @GetMapping("/listar-dia-profissional")
    ResponseEntity<List<AgendamentoRequestResponse>> listarPorDiaEProfissional(
            @RequestParam Long profissionalId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data
    );

    @GetMapping("/listar-por-status")
    ResponseEntity<List<AgendamentoRequestResponse>> listarPorStatus(@RequestParam StatusAgendamento status);

    @GetMapping("/listar-todos")
    ResponseEntity<List<AgendamentoRequestResponse>> listarTodos();

    @PatchMapping("/atualizar/{id}")
    ResponseEntity<AgendamentoRequestResponse> atualizar(@PathVariable Long id,@RequestBody AgendamentoRequestResponse agendamentoRequestResponse);
}
