package com.cljtech.clinica.service;

import com.cljtech.clinica.model.records.InsumoRequestResponse;
import com.cljtech.clinica.model.records.LocalAplicacaoRequestResponse;
import com.cljtech.clinica.model.records.ProcedimentoRequestResponse;

import java.util.List;

public interface ConfigsService {

    void criarLocalAplicacao(LocalAplicacaoRequestResponse request);

    List<LocalAplicacaoRequestResponse> listarLocaisAplicacao();

    void criarInsumo(InsumoRequestResponse request);

    List<InsumoRequestResponse> listarInsumos();

    void criarProcedimentos(ProcedimentoRequestResponse request);
    List<ProcedimentoRequestResponse> listarProcedimentos();
}
