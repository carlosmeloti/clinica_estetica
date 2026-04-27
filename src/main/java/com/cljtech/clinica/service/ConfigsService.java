package com.cljtech.clinica.service;

import com.cljtech.clinica.records.InsumoRequestResponse;
import com.cljtech.clinica.records.LocalAplicacaoRequestResponse;

import java.util.List;

public interface ConfigsService {

    void criarLocalAplicacao(LocalAplicacaoRequestResponse request);

    List<LocalAplicacaoRequestResponse> listarLocaisAplicacao();

    void criarInsumo(InsumoRequestResponse request);

    List<InsumoRequestResponse> listarInsumos();
}
