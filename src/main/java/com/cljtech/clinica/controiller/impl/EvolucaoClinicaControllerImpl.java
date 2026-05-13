package com.cljtech.clinica.controiller.impl;

import com.cljtech.clinica.controiller.EvolucaoClinicaController;
import com.cljtech.clinica.model.records.EvolucaoClinicaRequestResponse;
import com.cljtech.clinica.service.EvolucaoClinicaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EvolucaoClinicaControllerImpl implements EvolucaoClinicaController {

    private final EvolucaoClinicaService evolucaoClinicaService;

    @Override
    public ResponseEntity<EvolucaoClinicaRequestResponse> criar(EvolucaoClinicaRequestResponse request) {
        return ResponseEntity.ok(evolucaoClinicaService.criar(request));
    }
}
