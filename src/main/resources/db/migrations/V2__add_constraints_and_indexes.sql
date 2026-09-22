-- Migration incremental para adicionar restrições e índices
ALTER TABLE clinica.pessoa ADD CONSTRAINT uc_pessoa_email UNIQUE (email);
CREATE INDEX idx_pessoa_nome ON clinica.pessoa(nome);
CREATE INDEX idx_agendamento_data_inicio ON clinica.agendamento(data_hora_inicio);
CREATE INDEX idx_agendamento_profissional ON clinica.agendamento(profissional_id);
