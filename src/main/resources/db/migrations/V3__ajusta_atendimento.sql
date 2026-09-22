ALTER TABLE clinica.evolucoes_clinicas ALTER COLUMN relato_clinico DROP NOT NULL;
ALTER TABLE clinica.evolucoes_clinicas ADD COLUMN notas_profissional TEXT;
ALTER TABLE clinica.evolucoes_clinicas ADD COLUMN observacoes TEXT;
ALTER TABLE clinica.evolucoes_clinicas ADD COLUMN orientacoes_ao_paciente TEXT;
ALTER TABLE clinica.evolucoes_clinicas ADD COLUMN intercorrencias TEXT;
ALTER TABLE clinica.evolucoes_clinicas ADD COLUMN retorno_recomendado VARCHAR(255);
ALTER TABLE clinica.evolucoes_clinicas ADD COLUMN finalizado BOOLEAN DEFAULT FALSE;

CREATE TABLE clinica.evolucao_procedimentos (
    evolucao_id BIGINT NOT NULL,
    procedimento_id BIGINT NOT NULL,
    CONSTRAINT pk_evolucao_procedimentos PRIMARY KEY (evolucao_id, procedimento_id),
    CONSTRAINT fk_evolproc_on_evolucao_clinica FOREIGN KEY (evolucao_id) REFERENCES clinica.evolucoes_clinicas (id),
    CONSTRAINT fk_evolproc_on_procedimento FOREIGN KEY (procedimento_id) REFERENCES clinica.procedimento (id)
);

CREATE INDEX idx_evolucao_agendamento ON clinica.evolucoes_clinicas(agendamento_id);
CREATE INDEX idx_evolucao_paciente ON clinica.evolucoes_clinicas(paciente_id);
CREATE INDEX idx_evolucao_profissional ON clinica.evolucoes_clinicas(profissional_id);
