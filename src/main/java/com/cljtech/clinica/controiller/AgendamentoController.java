package com.cljtech.clinica.controiller;

import com.cljtech.clinica.model.records.AgendamentoRequestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agendamento")
public interface AgendamentoController {

    @PostMapping("/criar")
    ResponseEntity<AgendamentoRequestResponse> criar(@RequestBody  AgendamentoRequestResponse agendamentoRequestResponse);
}
