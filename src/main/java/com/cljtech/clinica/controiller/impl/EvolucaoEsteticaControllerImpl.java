package com.cljtech.clinica.controiller.impl;

import com.cljtech.clinica.controiller.EvolucaoEsteticaController;
import com.cljtech.clinica.model.records.EvolucaoEsteticaRequestResponse;
import com.cljtech.clinica.service.EvolucaoEsteticaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EvolucaoEsteticaControllerImpl implements EvolucaoEsteticaController {

    private final EvolucaoEsteticaService evolucaoEsteticaService;

    @Override
    public ResponseEntity<EvolucaoEsteticaRequestResponse> criar(EvolucaoEsteticaRequestResponse request) {
        return ResponseEntity.ok(evolucaoEsteticaService.criar(request));
    }


}
