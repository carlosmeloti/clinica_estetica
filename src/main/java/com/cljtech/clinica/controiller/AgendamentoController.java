package com.cljtech.clinica.controiller;

import com.cljtech.clinica.model.enuns.StatusAgendamento;
import com.cljtech.clinica.model.records.AgendamentoRequest;
import com.cljtech.clinica.model.records.AgendamentoResponse;
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
    ResponseEntity<AgendamentoResponse> criar(@RequestBody @Valid AgendamentoRequest agendamentoRequest);

    @GetMapping("/listar-dia-profissional")
    ResponseEntity<List<AgendamentoResponse>> listarPorDiaEProfissional(
            @RequestParam Long profissionalId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data
    );

    @GetMapping("/listar-por-status")
    ResponseEntity<List<AgendamentoResponse>> listarPorStatus(@RequestParam StatusAgendamento status);

    @GetMapping("/listar-todos")
    ResponseEntity<List<AgendamentoResponse>> listarTodos();

    @PatchMapping("/atualizar/{id}")
    ResponseEntity<AgendamentoResponse> atualizar(@PathVariable Long id, @RequestBody AgendamentoRequest agendamentoRequest);
}
