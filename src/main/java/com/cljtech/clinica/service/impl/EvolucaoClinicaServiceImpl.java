package com.cljtech.clinica.service.impl;

import com.cljtech.clinica.data.EvolucaoClinica;
import com.cljtech.clinica.data.repository.EvolucaoClinicaRepository;
import com.cljtech.clinica.mapper.EntityMapper;
import com.cljtech.clinica.model.records.EvolucaoClinicaRequestResponse;
import com.cljtech.clinica.service.EvolucaoClinicaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EvolucaoClinicaServiceImpl implements EvolucaoClinicaService {

    private final EvolucaoClinicaRepository evolucaoClinicaRepository;
    private final EntityMapper entityMapper;

    @Override
    public EvolucaoClinicaRequestResponse criar(EvolucaoClinicaRequestResponse request) {
        EvolucaoClinica evolucaoClinica = evolucaoClinicaRepository.save(entityMapper.toEvolucaoClinica(request));
        return entityMapper.toEvolucaoClinicaRequestRessponse(evolucaoClinica);
    }
}
