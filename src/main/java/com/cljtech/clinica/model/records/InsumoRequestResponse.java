package com.cljtech.clinica.model.records;

public record InsumoRequestResponse(Long id, String nome, Double quantidadeEstoque, String unidadeMedida, Double quantidadeUsada) {
}
