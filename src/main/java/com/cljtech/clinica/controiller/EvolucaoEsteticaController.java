package com.cljtech.clinica.controiller;

import com.cljtech.clinica.model.records.EvolucaoEsteticaRequestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/evolucao-clinica")
public interface EvolucaoEsteticaController {

    @PostMapping( "/criar")
    ResponseEntity<EvolucaoEsteticaRequestResponse> criar(@RequestBody EvolucaoEsteticaRequestResponse request);
}
