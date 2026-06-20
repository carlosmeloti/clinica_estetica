-- Criar a tabela de consumo de insumos atrelada ao schema clinica
CREATE TABLE clinica.consumos_insumos (
                                          id BIGSERIAL NOT NULL,
                                          evolucao_id BIGINT NOT NULL,
                                          insumo_id BIGINT NOT NULL,
                                          quantidade_usada NUMERIC(19, 2) NOT NULL,
                                          data_consumo TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Chave Primária
                                          CONSTRAINT pk_consumos_insumos PRIMARY KEY (id),

    -- Chave Estrangeira para a tabela Pai (Evoluções Clínicas)
                                          CONSTRAINT fk_consumo_evolucao
                                              FOREIGN KEY (evolucao_id)
                                                  REFERENCES clinica.evolucoes_clinicas (id)
                                                  ON DELETE RESTRICT,

    -- Chave Estrangeira para a tabela de Insumos (CORRIGIDO PARA SINGULAR)
                                          CONSTRAINT fk_consumo_insumo
                                              FOREIGN KEY (insumo_id)
                                                  REFERENCES clinica.insumo (id) -- <--- Mudado aqui de 'insumos' para 'insumo'
                                                  ON DELETE RESTRICT
);

-- Índices para otimizar as buscas por relatórios de consumo
CREATE INDEX idx_consumo_evolucao ON clinica.consumos_insumos(evolucao_id);
CREATE INDEX idx_consumo_insumo ON clinica.consumos_insumos(insumo_id);