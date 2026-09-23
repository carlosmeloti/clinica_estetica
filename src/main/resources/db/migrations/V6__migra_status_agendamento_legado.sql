-- Migrates legacy status values to StatusAgendamento enum names:
-- PENDENTE -> AGENDADO, FINALIZADO -> CONCLUIDO, AUSENTE -> NAO_COMPARECEU
UPDATE clinica.agendamento SET status = 'AGENDADO' WHERE status = 'PENDENTE';
UPDATE clinica.agendamento SET status = 'CONCLUIDO' WHERE status = 'FINALIZADO';
UPDATE clinica.agendamento SET status = 'NAO_COMPARECEU' WHERE status = 'AUSENTE';
