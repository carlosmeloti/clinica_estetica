-- Seed de desenvolvimento/homologação para testar todas as telas.
-- Idempotente: só insere se o usuário 'admin' ainda não existir.
--
-- Logins (senha de todos: 123456):
--   admin       / ADMIN
--   recepcao    / RECEPCAO
--   financeiro  / FINANCEIRO
--   ana.silva   / PROFISSIONAL
--   bruno.costa / PROFISSIONAL

DO $$
DECLARE
    v_senha CONSTANT VARCHAR := '$2a$10$1vVyvt5zfrMKK5giwPNrwuHQxvo9udt/G.DSno8UmjrUKngjH6Fje';
    v_now TIMESTAMP := NOW();

    id_admin BIGINT;
    id_recepcao BIGINT;
    id_financeiro BIGINT;
    id_ana BIGINT;
    id_bruno BIGINT;

    id_pac_maria BIGINT;
    id_pac_joana BIGINT;
    id_pac_carla BIGINT;
    id_pac_paula BIGINT;
    id_pac_lucia BIGINT;
    id_pac_fernanda BIGINT;

    id_proc_limpeza BIGINT;
    id_proc_botox BIGINT;
    id_proc_peeling BIGINT;
    id_proc_preench BIGINT;
    id_proc_laser BIGINT;
    id_proc_micro BIGINT;

    id_ins_toxina BIGINT;
    id_ins_acido BIGINT;
    id_ins_anestes BIGINT;
    id_ins_luva BIGINT;
    id_ins_algoda BIGINT;

    id_loc_fronte BIGINT;
    id_loc_glabela BIGINT;
    id_loc_labio BIGINT;
    id_loc_malar BIGINT;

    id_ag1 BIGINT;  -- concluído + pago (PIX) - Ana - ontem
    id_ag2 BIGINT;  -- concluído + pago (dinheiro) - Bruno - 3 dias
    id_ag3 BIGINT;  -- concluído + parcial - Ana - 5 dias (pendente no caixa)
    id_ag4 BIGINT;  -- concluído SEM pagamento - Bruno - 2 dias (pendente)
    id_ag5 BIGINT;  -- confirmado hoje Ana
    id_ag6 BIGINT;  -- agendado hoje Bruno
    id_ag7 BIGINT;  -- em atendimento hoje Ana
    id_ag8 BIGINT;  -- agendado amanhã Ana
    id_ag9 BIGINT;  -- agendado amanhã Bruno
    id_ag10 BIGINT; -- cancelado
    id_ag11 BIGINT; -- concluído + pago cartão - semana passada
    id_ag12 BIGINT; -- concluído + pago transfer - mês corrente

    id_evo1 BIGINT;
    id_evo2 BIGINT;
    id_evo3 BIGINT;
    id_evo4 BIGINT;
    id_evo7 BIGINT;
    id_evo11 BIGINT;
    id_evo12 BIGINT;

    id_pag1 BIGINT;
    id_pag2 BIGINT;
    id_pag3 BIGINT;
    id_pag11 BIGINT;
    id_pag12 BIGINT;
BEGIN
    IF EXISTS (SELECT 1 FROM clinica.usuarios WHERE login = 'admin') THEN
        RAISE NOTICE 'Seed V8 já aplicado (login admin existe). Pulando.';
        RETURN;
    END IF;

    ------------------------------------------------------------------
    -- USUÁRIOS
    ------------------------------------------------------------------
    INSERT INTO clinica.usuarios (data_criacao, data_atualizacao, ativo, login, senha, nome, email, perfil, registro_profissional)
    VALUES (v_now, v_now, TRUE, 'admin', v_senha, 'Administrador Sistema', 'admin@clinica.test', 'ADMIN', NULL)
    RETURNING id INTO id_admin;

    INSERT INTO clinica.usuarios (data_criacao, data_atualizacao, ativo, login, senha, nome, email, perfil, registro_profissional)
    VALUES (v_now, v_now, TRUE, 'recepcao', v_senha, 'Carla Recepção', 'recepcao@clinica.test', 'RECEPCAO', NULL)
    RETURNING id INTO id_recepcao;

    INSERT INTO clinica.usuarios (data_criacao, data_atualizacao, ativo, login, senha, nome, email, perfil, registro_profissional)
    VALUES (v_now, v_now, TRUE, 'financeiro', v_senha, 'Roberto Financeiro', 'financeiro@clinica.test', 'FINANCEIRO', NULL)
    RETURNING id INTO id_financeiro;

    INSERT INTO clinica.usuarios (data_criacao, data_atualizacao, ativo, login, senha, nome, email, perfil, registro_profissional)
    VALUES (v_now, v_now, TRUE, 'ana.silva', v_senha, 'Ana Silva', 'ana.silva@clinica.test', 'PROFISSIONAL', 'CRM-SP 123456')
    RETURNING id INTO id_ana;

    INSERT INTO clinica.usuarios (data_criacao, data_atualizacao, ativo, login, senha, nome, email, perfil, registro_profissional)
    VALUES (v_now, v_now, TRUE, 'bruno.costa', v_senha, 'Bruno Costa', 'bruno.costa@clinica.test', 'PROFISSIONAL', 'CRM-SP 654321')
    RETURNING id INTO id_bruno;

    ------------------------------------------------------------------
    -- PACIENTES (pessoa + paciente)
    ------------------------------------------------------------------
    INSERT INTO clinica.pessoa (data_criacao, data_atualizacao, ativo, nome, cpf, email, telefone, data_nascimento,
                                logradouro, numero, complemento, bairro, cidade, estado, cep)
    VALUES (v_now, v_now, TRUE, 'Maria Oliveira', '11111111111', 'maria.oliveira@test.com', '11999990001', DATE '1990-03-12',
            'Rua das Flores', '100', 'Apto 12', 'Centro', 'São Paulo', 'SP', '01001000')
    RETURNING id INTO id_pac_maria;
    INSERT INTO clinica.paciente (id, observacoes_gerais, tipo_sanguineo, nome_responsavel)
    VALUES (id_pac_maria, 'Pele sensível', 'O+', NULL);

    INSERT INTO clinica.pessoa (data_criacao, data_atualizacao, ativo, nome, cpf, email, telefone, data_nascimento,
                                logradouro, numero, complemento, bairro, cidade, estado, cep)
    VALUES (v_now, v_now, TRUE, 'Joana Souza', '22222222222', 'joana.souza@test.com', '11999990002', DATE '1985-07-22',
            'Av. Paulista', '1500', NULL, 'Bela Vista', 'São Paulo', 'SP', '01310100')
    RETURNING id INTO id_pac_joana;
    INSERT INTO clinica.paciente (id, observacoes_gerais, tipo_sanguineo, nome_responsavel)
    VALUES (id_pac_joana, NULL, 'A+', NULL);

    INSERT INTO clinica.pessoa (data_criacao, data_atualizacao, ativo, nome, cpf, email, telefone, data_nascimento,
                                logradouro, numero, complemento, bairro, cidade, estado, cep)
    VALUES (v_now, v_now, TRUE, 'Carla Mendes', '33333333333', 'carla.mendes@test.com', '11999990003', DATE '1995-11-05',
            'Rua Augusta', '250', NULL, 'Consolação', 'São Paulo', 'SP', '01305000')
    RETURNING id INTO id_pac_carla;
    INSERT INTO clinica.paciente (id, observacoes_gerais, tipo_sanguineo, nome_responsavel)
    VALUES (id_pac_carla, 'Alergia a lidocaína — confirmar', 'B+', NULL);

    INSERT INTO clinica.pessoa (data_criacao, data_atualizacao, ativo, nome, cpf, email, telefone, data_nascimento,
                                logradouro, numero, complemento, bairro, cidade, estado, cep)
    VALUES (v_now, v_now, TRUE, 'Paula Ferreira', '44444444444', 'paula.ferreira@test.com', '11999990004', DATE '1988-01-30',
            'Rua Harmonia', '88', 'Casa', 'Vila Madalena', 'São Paulo', 'SP', '05435000')
    RETURNING id INTO id_pac_paula;
    INSERT INTO clinica.paciente (id, observacoes_gerais, tipo_sanguineo, nome_responsavel)
    VALUES (id_pac_paula, NULL, 'AB+', NULL);

    INSERT INTO clinica.pessoa (data_criacao, data_atualizacao, ativo, nome, cpf, email, telefone, data_nascimento,
                                logradouro, numero, complemento, bairro, cidade, estado, cep)
    VALUES (v_now, v_now, TRUE, 'Lúcia Ramos', '55555555555', 'lucia.ramos@test.com', '11999990005', DATE '1979-09-18',
            'Rua Domingos de Morais', '1200', NULL, 'Vila Mariana', 'São Paulo', 'SP', '04010000')
    RETURNING id INTO id_pac_lucia;
    INSERT INTO clinica.paciente (id, observacoes_gerais, tipo_sanguineo, nome_responsavel)
    VALUES (id_pac_lucia, 'Retorno de preenchimento', 'O-', NULL);

    INSERT INTO clinica.pessoa (data_criacao, data_atualizacao, ativo, nome, cpf, email, telefone, data_nascimento,
                                logradouro, numero, complemento, bairro, cidade, estado, cep)
    VALUES (v_now, v_now, TRUE, 'Fernanda Lima', '66666666666', 'fernanda.lima@test.com', '11999990006', DATE '1992-05-09',
            'Alameda Santos', '700', 'Sala 3', 'Jardins', 'São Paulo', 'SP', '01418000')
    RETURNING id INTO id_pac_fernanda;
    INSERT INTO clinica.paciente (id, observacoes_gerais, tipo_sanguineo, nome_responsavel)
    VALUES (id_pac_fernanda, NULL, 'A-', NULL);

    ------------------------------------------------------------------
    -- PROCEDIMENTOS (com preço)
    ------------------------------------------------------------------
    INSERT INTO clinica.procedimento (data_criacao, data_atualizacao, ativo, nome, preco_sugerido, duracao_minutos)
    VALUES (v_now, v_now, TRUE, 'Limpeza de Pele', 180.00, 60)
    RETURNING id INTO id_proc_limpeza;

    INSERT INTO clinica.procedimento (data_criacao, data_atualizacao, ativo, nome, preco_sugerido, duracao_minutos)
    VALUES (v_now, v_now, TRUE, 'Aplicação de Toxina Botulínica', 1200.00, 45)
    RETURNING id INTO id_proc_botox;

    INSERT INTO clinica.procedimento (data_criacao, data_atualizacao, ativo, nome, preco_sugerido, duracao_minutos)
    VALUES (v_now, v_now, TRUE, 'Peeling Químico', 350.00, 40)
    RETURNING id INTO id_proc_peeling;

    INSERT INTO clinica.procedimento (data_criacao, data_atualizacao, ativo, nome, preco_sugerido, duracao_minutos)
    VALUES (v_now, v_now, TRUE, 'Preenchimento Labial', 1500.00, 50)
    RETURNING id INTO id_proc_preench;

    INSERT INTO clinica.procedimento (data_criacao, data_atualizacao, ativo, nome, preco_sugerido, duracao_minutos)
    VALUES (v_now, v_now, TRUE, 'Laser Facial', 450.00, 30)
    RETURNING id INTO id_proc_laser;

    INSERT INTO clinica.procedimento (data_criacao, data_atualizacao, ativo, nome, preco_sugerido, duracao_minutos)
    VALUES (v_now, v_now, TRUE, 'Microagulhamento', 400.00, 55)
    RETURNING id INTO id_proc_micro;

    ------------------------------------------------------------------
    -- INSUMOS (estoque) — modelo atual não tem preço de venda; procedimentos têm preço
    ------------------------------------------------------------------
    INSERT INTO clinica.insumo (data_criacao, data_atualizacao, ativo, nome, quantidade_estoque, unidade_medida)
    VALUES (v_now, v_now, TRUE, 'Toxina Botulínica 100U', 25, 'FRASCO')
    RETURNING id INTO id_ins_toxina;

    INSERT INTO clinica.insumo (data_criacao, data_atualizacao, ativo, nome, quantidade_estoque, unidade_medida)
    VALUES (v_now, v_now, TRUE, 'Ácido Hialurônico 1ml', 18, 'SERINGA')
    RETURNING id INTO id_ins_acido;

    INSERT INTO clinica.insumo (data_criacao, data_atualizacao, ativo, nome, quantidade_estoque, unidade_medida)
    VALUES (v_now, v_now, TRUE, 'Anestésico Tópico', 40, 'BISNAGA')
    RETURNING id INTO id_ins_anestes;

    INSERT INTO clinica.insumo (data_criacao, data_atualizacao, ativo, nome, quantidade_estoque, unidade_medida)
    VALUES (v_now, v_now, TRUE, 'Luva Procedimento M', 200, 'UNIDADE')
    RETURNING id INTO id_ins_luva;

    INSERT INTO clinica.insumo (data_criacao, data_atualizacao, ativo, nome, quantidade_estoque, unidade_medida)
    VALUES (v_now, v_now, TRUE, 'Algodão Hidrófilo', 80, 'PACOTE')
    RETURNING id INTO id_ins_algoda;

    ------------------------------------------------------------------
    -- LOCAIS DE APLICAÇÃO
    ------------------------------------------------------------------
    INSERT INTO clinica.local_aplicacao (data_criacao, data_atualizacao, ativo, nome, pre_definido)
    VALUES (v_now, v_now, TRUE, 'Frontal', TRUE)
    RETURNING id INTO id_loc_fronte;

    INSERT INTO clinica.local_aplicacao (data_criacao, data_atualizacao, ativo, nome, pre_definido)
    VALUES (v_now, v_now, TRUE, 'Glabela', TRUE)
    RETURNING id INTO id_loc_glabela;

    INSERT INTO clinica.local_aplicacao (data_criacao, data_atualizacao, ativo, nome, pre_definido)
    VALUES (v_now, v_now, TRUE, 'Lábios', TRUE)
    RETURNING id INTO id_loc_labio;

    INSERT INTO clinica.local_aplicacao (data_criacao, data_atualizacao, ativo, nome, pre_definido)
    VALUES (v_now, v_now, TRUE, 'Malar', TRUE)
    RETURNING id INTO id_loc_malar;

    ------------------------------------------------------------------
    -- AGENDAMENTOS
    ------------------------------------------------------------------
    -- 1) Ontem Ana — CONCLUIDO — Limpeza (pago PIX)
    INSERT INTO clinica.agendamento (data_criacao, data_atualizacao, ativo, paciente_id, profissional_id,
                                     data_hora_inicio, data_hora_fim, status, motivo_consulta, valor_previsto)
    VALUES (v_now, v_now, TRUE, id_pac_maria, id_ana,
            (CURRENT_DATE - 1) + TIME '10:00', (CURRENT_DATE - 1) + TIME '11:00',
            'CONCLUIDO', 'Limpeza de pele', 180.00)
    RETURNING id INTO id_ag1;
    INSERT INTO clinica.agendamento_procedimento (agendamento_id, procedimento_id) VALUES (id_ag1, id_proc_limpeza);

    -- 2) 3 dias atrás Bruno — CONCLUIDO — Peeling (pago dinheiro)
    INSERT INTO clinica.agendamento (data_criacao, data_atualizacao, ativo, paciente_id, profissional_id,
                                     data_hora_inicio, data_hora_fim, status, motivo_consulta, valor_previsto)
    VALUES (v_now, v_now, TRUE, id_pac_joana, id_bruno,
            (CURRENT_DATE - 3) + TIME '14:00', (CURRENT_DATE - 3) + TIME '14:40',
            'CONCLUIDO', 'Peeling', 350.00)
    RETURNING id INTO id_ag2;
    INSERT INTO clinica.agendamento_procedimento (agendamento_id, procedimento_id) VALUES (id_ag2, id_proc_peeling);

    -- 3) 5 dias atrás Ana — CONCLUIDO — Botox (pagamento PARCIAL)
    INSERT INTO clinica.agendamento (data_criacao, data_atualizacao, ativo, paciente_id, profissional_id,
                                     data_hora_inicio, data_hora_fim, status, motivo_consulta, valor_previsto)
    VALUES (v_now, v_now, TRUE, id_pac_carla, id_ana,
            (CURRENT_DATE - 5) + TIME '09:00', (CURRENT_DATE - 5) + TIME '09:45',
            'CONCLUIDO', 'Toxina botulínica', 1200.00)
    RETURNING id INTO id_ag3;
    INSERT INTO clinica.agendamento_procedimento (agendamento_id, procedimento_id) VALUES (id_ag3, id_proc_botox);

    -- 4) 2 dias atrás Bruno — CONCLUIDO — Preenchimento SEM pagamento (pendente caixa)
    INSERT INTO clinica.agendamento (data_criacao, data_atualizacao, ativo, paciente_id, profissional_id,
                                     data_hora_inicio, data_hora_fim, status, motivo_consulta, valor_previsto)
    VALUES (v_now, v_now, TRUE, id_pac_paula, id_bruno,
            (CURRENT_DATE - 2) + TIME '16:00', (CURRENT_DATE - 2) + TIME '16:50',
            'CONCLUIDO', 'Preenchimento labial', 1500.00)
    RETURNING id INTO id_ag4;
    INSERT INTO clinica.agendamento_procedimento (agendamento_id, procedimento_id) VALUES (id_ag4, id_proc_preench);

    -- 5) Hoje Ana — CONFIRMADO — Laser
    INSERT INTO clinica.agendamento (data_criacao, data_atualizacao, ativo, paciente_id, profissional_id,
                                     data_hora_inicio, data_hora_fim, status, motivo_consulta, valor_previsto)
    VALUES (v_now, v_now, TRUE, id_pac_lucia, id_ana,
            CURRENT_DATE + TIME '11:00', CURRENT_DATE + TIME '11:30',
            'CONFIRMADO', 'Laser facial', 450.00)
    RETURNING id INTO id_ag5;
    INSERT INTO clinica.agendamento_procedimento (agendamento_id, procedimento_id) VALUES (id_ag5, id_proc_laser);

    -- 6) Hoje Bruno — AGENDADO — Limpeza + Microagulhamento
    INSERT INTO clinica.agendamento (data_criacao, data_atualizacao, ativo, paciente_id, profissional_id,
                                     data_hora_inicio, data_hora_fim, status, motivo_consulta, valor_previsto)
    VALUES (v_now, v_now, TRUE, id_pac_fernanda, id_bruno,
            CURRENT_DATE + TIME '15:00', CURRENT_DATE + TIME '16:30',
            'AGENDADO', 'Combo facial', 580.00)
    RETURNING id INTO id_ag6;
    INSERT INTO clinica.agendamento_procedimento (agendamento_id, procedimento_id) VALUES
        (id_ag6, id_proc_limpeza),
        (id_ag6, id_proc_micro);

    -- 7) Hoje Ana — EM_ATENDIMENTO — Peeling
    INSERT INTO clinica.agendamento (data_criacao, data_atualizacao, ativo, paciente_id, profissional_id,
                                     data_hora_inicio, data_hora_fim, status, motivo_consulta, valor_previsto)
    VALUES (v_now, v_now, TRUE, id_pac_maria, id_ana,
            CURRENT_DATE + TIME '13:00', CURRENT_DATE + TIME '13:40',
            'EM_ATENDIMENTO', 'Peeling de manutenção', 350.00)
    RETURNING id INTO id_ag7;
    INSERT INTO clinica.agendamento_procedimento (agendamento_id, procedimento_id) VALUES (id_ag7, id_proc_peeling);

    -- 8) Amanhã Ana — AGENDADO — Botox
    INSERT INTO clinica.agendamento (data_criacao, data_atualizacao, ativo, paciente_id, profissional_id,
                                     data_hora_inicio, data_hora_fim, status, motivo_consulta, valor_previsto)
    VALUES (v_now, v_now, TRUE, id_pac_joana, id_ana,
            (CURRENT_DATE + 1) + TIME '10:00', (CURRENT_DATE + 1) + TIME '10:45',
            'AGENDADO', 'Retoque toxina', 1200.00)
    RETURNING id INTO id_ag8;
    INSERT INTO clinica.agendamento_procedimento (agendamento_id, procedimento_id) VALUES (id_ag8, id_proc_botox);

    -- 9) Amanhã Bruno — AGENDADO — Laser
    INSERT INTO clinica.agendamento (data_criacao, data_atualizacao, ativo, paciente_id, profissional_id,
                                     data_hora_inicio, data_hora_fim, status, motivo_consulta, valor_previsto)
    VALUES (v_now, v_now, TRUE, id_pac_carla, id_bruno,
            (CURRENT_DATE + 1) + TIME '14:30', (CURRENT_DATE + 1) + TIME '15:00',
            'AGENDADO', 'Laser', 450.00)
    RETURNING id INTO id_ag9;
    INSERT INTO clinica.agendamento_procedimento (agendamento_id, procedimento_id) VALUES (id_ag9, id_proc_laser);

    -- 10) Cancelado
    INSERT INTO clinica.agendamento (data_criacao, data_atualizacao, ativo, paciente_id, profissional_id,
                                     data_hora_inicio, data_hora_fim, status, motivo_consulta, valor_previsto)
    VALUES (v_now, v_now, TRUE, id_pac_paula, id_ana,
            (CURRENT_DATE - 4) + TIME '17:00', (CURRENT_DATE - 4) + TIME '17:30',
            'CANCELADO', 'Paciente desmarcou', 180.00)
    RETURNING id INTO id_ag10;
    INSERT INTO clinica.agendamento_procedimento (agendamento_id, procedimento_id) VALUES (id_ag10, id_proc_limpeza);

    -- 11) 10 dias atrás Ana — CONCLUIDO — Micro (pago cartão crédito)
    INSERT INTO clinica.agendamento (data_criacao, data_atualizacao, ativo, paciente_id, profissional_id,
                                     data_hora_inicio, data_hora_fim, status, motivo_consulta, valor_previsto)
    VALUES (v_now, v_now, TRUE, id_pac_lucia, id_ana,
            (CURRENT_DATE - 10) + TIME '11:00', (CURRENT_DATE - 10) + TIME '11:55',
            'CONCLUIDO', 'Microagulhamento', 400.00)
    RETURNING id INTO id_ag11;
    INSERT INTO clinica.agendamento_procedimento (agendamento_id, procedimento_id) VALUES (id_ag11, id_proc_micro);

    -- 12) 20 dias atrás Bruno — CONCLUIDO — Botox + Preenchimento (pago transferência, com desconto)
    INSERT INTO clinica.agendamento (data_criacao, data_atualizacao, ativo, paciente_id, profissional_id,
                                     data_hora_inicio, data_hora_fim, status, motivo_consulta, valor_previsto)
    VALUES (v_now, v_now, TRUE, id_pac_fernanda, id_bruno,
            (CURRENT_DATE - 20) + TIME '09:30', (CURRENT_DATE - 20) + TIME '11:00',
            'CONCLUIDO', 'Harmonização', 2700.00)
    RETURNING id INTO id_ag12;
    INSERT INTO clinica.agendamento_procedimento (agendamento_id, procedimento_id) VALUES
        (id_ag12, id_proc_botox),
        (id_ag12, id_proc_preench);

    ------------------------------------------------------------------
    -- ATENDIMENTOS (evolução clínica + estética + locais + procedimentos + consumo)
    ------------------------------------------------------------------
    -- evo1 (ag1)
    INSERT INTO clinica.evolucoes_clinicas (
        data_criacao, data_atualizacao, ativo, agendamento_id, paciente_id, profissional_id,
        data_registro, relato_clinico, notas_profissional, observacoes, orientacoes_ao_paciente,
        intercorrencias, retorno_recomendado, finalizado, diagnostico_hipotetico
    ) VALUES (
        v_now, v_now, TRUE, id_ag1, id_pac_maria, id_ana,
        (CURRENT_DATE - 1) + TIME '11:05',
        'Paciente apresentou comedões e oleosidade. Realizada limpeza profunda.',
        'Boa resposta ao tratamento.',
        'Pele oleosa',
        'Usso de protetor solar FPS 50 e evitar exposição solar por 24h.',
        NULL,
        '30 dias',
        TRUE,
        'Acne grau I'
    ) RETURNING id INTO id_evo1;
    INSERT INTO clinica.evolucoes_esteticas (id, numero_sessao, dose_aplicada_mg, peso_paciente_kg)
    VALUES (id_evo1, 1, NULL, 62.5);
    INSERT INTO clinica.evolucao_procedimentos (evolucao_id, procedimento_id) VALUES (id_evo1, id_proc_limpeza);
    INSERT INTO clinica.consumos_insumos (evolucao_id, insumo_id, quantidade_usada, data_consumo)
    VALUES (id_evo1, id_ins_luva, 2, (CURRENT_DATE - 1) + TIME '10:30'),
           (id_evo1, id_ins_algoda, 1, (CURRENT_DATE - 1) + TIME '10:35');

    -- evo2 (ag2)
    INSERT INTO clinica.evolucoes_clinicas (
        data_criacao, data_atualizacao, ativo, agendamento_id, paciente_id, profissional_id,
        data_registro, relato_clinico, notas_profissional, observacoes, orientacoes_ao_paciente,
        intercorrencias, retorno_recomendado, finalizado, diagnostico_hipotetico
    ) VALUES (
        v_now, v_now, TRUE, id_ag2, id_pac_joana, id_bruno,
        (CURRENT_DATE - 3) + TIME '14:45',
        'Peeling químico superficial realizado sem intercorrências.',
        'Eritema leve esperado.',
        NULL,
        'Hidratação intensa e protetor solar. Evitar maquiagem por 24h.',
        'Eritema leve',
        '45 dias',
        TRUE,
        'Melasma superficial'
    ) RETURNING id INTO id_evo2;
    INSERT INTO clinica.evolucoes_esteticas (id, numero_sessao, dose_aplicada_mg, peso_paciente_kg)
    VALUES (id_evo2, 2, NULL, 58.0);
    INSERT INTO clinica.evolucao_procedimentos (evolucao_id, procedimento_id) VALUES (id_evo2, id_proc_peeling);
    INSERT INTO clinica.consumos_insumos (evolucao_id, insumo_id, quantidade_usada, data_consumo)
    VALUES (id_evo2, id_ins_luva, 2, (CURRENT_DATE - 3) + TIME '14:10'),
           (id_evo2, id_ins_anestes, 1, (CURRENT_DATE - 3) + TIME '14:15');

    -- evo3 (ag3) botox
    INSERT INTO clinica.evolucoes_clinicas (
        data_criacao, data_atualizacao, ativo, agendamento_id, paciente_id, profissional_id,
        data_registro, relato_clinico, notas_profissional, observacoes, orientacoes_ao_paciente,
        intercorrencias, retorno_recomendado, finalizado, diagnostico_hipotetico
    ) VALUES (
        v_now, v_now, TRUE, id_ag3, id_pac_carla, id_ana,
        (CURRENT_DATE - 5) + TIME '09:50',
        'Aplicação de toxina em região frontal e glabela.',
        'Dose padrão 40U.',
        'Primeira aplicação na clínica',
        'Não deitar por 4h. Evitar exercícios intensos no dia.',
        NULL,
        '4 meses',
        TRUE,
        'Rugas dinâmicas'
    ) RETURNING id INTO id_evo3;
    INSERT INTO clinica.evolucoes_esteticas (id, numero_sessao, dose_aplicada_mg, peso_paciente_kg)
    VALUES (id_evo3, 1, 40.0, 65.0);
    INSERT INTO clinica.evolucao_locais (evolucao_id, local_id) VALUES (id_evo3, id_loc_fronte), (id_evo3, id_loc_glabela);
    INSERT INTO clinica.evolucao_procedimentos (evolucao_id, procedimento_id) VALUES (id_evo3, id_proc_botox);
    INSERT INTO clinica.consumos_insumos (evolucao_id, insumo_id, quantidade_usada, data_consumo)
    VALUES (id_evo3, id_ins_toxina, 1, (CURRENT_DATE - 5) + TIME '09:20'),
           (id_evo3, id_ins_luva, 2, (CURRENT_DATE - 5) + TIME '09:15');

    -- evo4 (ag4) preenchimento
    INSERT INTO clinica.evolucoes_clinicas (
        data_criacao, data_atualizacao, ativo, agendamento_id, paciente_id, profissional_id,
        data_registro, relato_clinico, notas_profissional, observacoes, orientacoes_ao_paciente,
        intercorrencias, retorno_recomendado, finalizado, diagnostico_hipotetico
    ) VALUES (
        v_now, v_now, TRUE, id_ag4, id_pac_paula, id_bruno,
        (CURRENT_DATE - 2) + TIME '16:55',
        'Preenchimento labial com 1ml de ácido hialurônico.',
        'Resultado simétrico.',
        NULL,
        'Gelo local. Evitar batom por 24h.',
        NULL,
        '15 dias (retorno de avaliação)',
        TRUE,
        'Hipovolumia labial'
    ) RETURNING id INTO id_evo4;
    INSERT INTO clinica.evolucoes_esteticas (id, numero_sessao, dose_aplicada_mg, peso_paciente_kg)
    VALUES (id_evo4, 1, NULL, 70.0);
    INSERT INTO clinica.evolucao_locais (evolucao_id, local_id) VALUES (id_evo4, id_loc_labio);
    INSERT INTO clinica.evolucao_procedimentos (evolucao_id, procedimento_id) VALUES (id_evo4, id_proc_preench);
    INSERT INTO clinica.consumos_insumos (evolucao_id, insumo_id, quantidade_usada, data_consumo)
    VALUES (id_evo4, id_ins_acido, 1, (CURRENT_DATE - 2) + TIME '16:20'),
           (id_evo4, id_ins_anestes, 1, (CURRENT_DATE - 2) + TIME '16:10'),
           (id_evo4, id_ins_luva, 2, (CURRENT_DATE - 2) + TIME '16:05');

    -- evo7 (ag7) em andamento — finalizado false
    INSERT INTO clinica.evolucoes_clinicas (
        data_criacao, data_atualizacao, ativo, agendamento_id, paciente_id, profissional_id,
        data_registro, relato_clinico, notas_profissional, observacoes, orientacoes_ao_paciente,
        intercorrencias, retorno_recomendado, finalizado, diagnostico_hipotetico
    ) VALUES (
        v_now, v_now, TRUE, id_ag7, id_pac_maria, id_ana,
        CURRENT_DATE + TIME '13:10',
        'Início do peeling. Paciente confortável.',
        'Aguardando tempo de ação do ácido.',
        NULL,
        NULL,
        NULL,
        NULL,
        FALSE,
        NULL
    ) RETURNING id INTO id_evo7;
    INSERT INTO clinica.evolucoes_esteticas (id, numero_sessao, dose_aplicada_mg, peso_paciente_kg)
    VALUES (id_evo7, 3, NULL, 62.5);
    INSERT INTO clinica.evolucao_procedimentos (evolucao_id, procedimento_id) VALUES (id_evo7, id_proc_peeling);

    -- evo11
    INSERT INTO clinica.evolucoes_clinicas (
        data_criacao, data_atualizacao, ativo, agendamento_id, paciente_id, profissional_id,
        data_registro, relato_clinico, notas_profissional, observacoes, orientacoes_ao_paciente,
        intercorrencias, retorno_recomendado, finalizado, diagnostico_hipotetico
    ) VALUES (
        v_now, v_now, TRUE, id_ag11, id_pac_lucia, id_ana,
        (CURRENT_DATE - 10) + TIME '12:00',
        'Microagulhamento facial 1.5mm.',
        'Boa regeneração esperada.',
        NULL,
        'Hidratação e protetor. Evitar sol 7 dias.',
        NULL,
        '30 dias',
        TRUE,
        'Flacidez leve'
    ) RETURNING id INTO id_evo11;
    INSERT INTO clinica.evolucoes_esteticas (id, numero_sessao, dose_aplicada_mg, peso_paciente_kg)
    VALUES (id_evo11, 1, NULL, 68.0);
    INSERT INTO clinica.evolucao_procedimentos (evolucao_id, procedimento_id) VALUES (id_evo11, id_proc_micro);
    INSERT INTO clinica.consumos_insumos (evolucao_id, insumo_id, quantidade_usada, data_consumo)
    VALUES (id_evo11, id_ins_luva, 2, (CURRENT_DATE - 10) + TIME '11:10'),
           (id_evo11, id_ins_anestes, 1, (CURRENT_DATE - 10) + TIME '11:15');

    -- evo12
    INSERT INTO clinica.evolucoes_clinicas (
        data_criacao, data_atualizacao, ativo, agendamento_id, paciente_id, profissional_id,
        data_registro, relato_clinico, notas_profissional, observacoes, orientacoes_ao_paciente,
        intercorrencias, retorno_recomendado, finalizado, diagnostico_hipotetico
    ) VALUES (
        v_now, v_now, TRUE, id_ag12, id_pac_fernanda, id_bruno,
        (CURRENT_DATE - 20) + TIME '11:10',
        'Harmonização: toxina frontal/glabela + preenchimento labial.',
        'Paciente muito satisfeita.',
        'Combo promocional',
        'Cuidados combinados de toxina e preenchimento.',
        NULL,
        '15 dias',
        TRUE,
        'Harmonização facial'
    ) RETURNING id INTO id_evo12;
    INSERT INTO clinica.evolucoes_esteticas (id, numero_sessao, dose_aplicada_mg, peso_paciente_kg)
    VALUES (id_evo12, 1, 36.0, 60.0);
    INSERT INTO clinica.evolucao_locais (evolucao_id, local_id)
    VALUES (id_evo12, id_loc_fronte), (id_evo12, id_loc_glabela), (id_evo12, id_loc_labio);
    INSERT INTO clinica.evolucao_procedimentos (evolucao_id, procedimento_id)
    VALUES (id_evo12, id_proc_botox), (id_evo12, id_proc_preench);
    INSERT INTO clinica.consumos_insumos (evolucao_id, insumo_id, quantidade_usada, data_consumo)
    VALUES (id_evo12, id_ins_toxina, 1, (CURRENT_DATE - 20) + TIME '09:45'),
           (id_evo12, id_ins_acido, 1, (CURRENT_DATE - 20) + TIME '10:20'),
           (id_evo12, id_ins_luva, 4, (CURRENT_DATE - 20) + TIME '09:40'),
           (id_evo12, id_ins_anestes, 1, (CURRENT_DATE - 20) + TIME '10:15');

    ------------------------------------------------------------------
    -- PAGAMENTOS / CAIXA
    ------------------------------------------------------------------
    -- pag1: limpeza PIX integral
    INSERT INTO clinica.pagamento (
        data_criacao, data_atualizacao, ativo, agendamento_id, valor_bruto, desconto, valor_pago,
        forma_pagamento, status, data_pagamento, observacao, registrado_por_id
    ) VALUES (
        v_now, v_now, TRUE, id_ag1, 180.00, 0, 180.00,
        'PIX', 'PAGO', (CURRENT_DATE - 1) + TIME '11:20', 'Pagamento na saída', id_recepcao
    ) RETURNING id INTO id_pag1;
    INSERT INTO clinica.pagamento_item (data_criacao, data_atualizacao, ativo, pagamento_id, procedimento_id, descricao, valor_unitario, quantidade)
    VALUES (v_now, v_now, TRUE, id_pag1, id_proc_limpeza, 'Limpeza de Pele', 180.00, 1);

    -- pag2: peeling dinheiro
    INSERT INTO clinica.pagamento (
        data_criacao, data_atualizacao, ativo, agendamento_id, valor_bruto, desconto, valor_pago,
        forma_pagamento, status, data_pagamento, observacao, registrado_por_id
    ) VALUES (
        v_now, v_now, TRUE, id_ag2, 350.00, 0, 350.00,
        'DINHEIRO', 'PAGO', (CURRENT_DATE - 3) + TIME '15:00', NULL, id_recepcao
    ) RETURNING id INTO id_pag2;
    INSERT INTO clinica.pagamento_item (data_criacao, data_atualizacao, ativo, pagamento_id, procedimento_id, descricao, valor_unitario, quantidade)
    VALUES (v_now, v_now, TRUE, id_pag2, id_proc_peeling, 'Peeling Químico', 350.00, 1);

    -- pag3: botox PARCIAL (600 de 1200) — ainda aparece em pendentes
    INSERT INTO clinica.pagamento (
        data_criacao, data_atualizacao, ativo, agendamento_id, valor_bruto, desconto, valor_pago,
        forma_pagamento, status, data_pagamento, observacao, registrado_por_id
    ) VALUES (
        v_now, v_now, TRUE, id_ag3, 1200.00, 0, 600.00,
        'CARTAO_DEBITO', 'PARCIAL', (CURRENT_DATE - 5) + TIME '10:10', 'Entrada; restante a pagar', id_financeiro
    ) RETURNING id INTO id_pag3;
    INSERT INTO clinica.pagamento_item (data_criacao, data_atualizacao, ativo, pagamento_id, procedimento_id, descricao, valor_unitario, quantidade)
    VALUES (v_now, v_now, TRUE, id_pag3, id_proc_botox, 'Aplicação de Toxina Botulínica', 1200.00, 1);

    -- pag11: micro cartão crédito
    INSERT INTO clinica.pagamento (
        data_criacao, data_atualizacao, ativo, agendamento_id, valor_bruto, desconto, valor_pago,
        forma_pagamento, status, data_pagamento, observacao, registrado_por_id
    ) VALUES (
        v_now, v_now, TRUE, id_ag11, 400.00, 0, 400.00,
        'CARTAO_CREDITO', 'PAGO', (CURRENT_DATE - 10) + TIME '12:15', NULL, id_recepcao
    ) RETURNING id INTO id_pag11;
    INSERT INTO clinica.pagamento_item (data_criacao, data_atualizacao, ativo, pagamento_id, procedimento_id, descricao, valor_unitario, quantidade)
    VALUES (v_now, v_now, TRUE, id_pag11, id_proc_micro, 'Microagulhamento', 400.00, 1);

    -- pag12: combo com desconto 200, transferência
    INSERT INTO clinica.pagamento (
        data_criacao, data_atualizacao, ativo, agendamento_id, valor_bruto, desconto, valor_pago,
        forma_pagamento, status, data_pagamento, observacao, registrado_por_id
    ) VALUES (
        v_now, v_now, TRUE, id_ag12, 2700.00, 200.00, 2500.00,
        'TRANSFERENCIA', 'PAGO', (CURRENT_DATE - 20) + TIME '11:30', 'Desconto combo harmonização', id_financeiro
    ) RETURNING id INTO id_pag12;
    INSERT INTO clinica.pagamento_item (data_criacao, data_atualizacao, ativo, pagamento_id, procedimento_id, descricao, valor_unitario, quantidade)
    VALUES
        (v_now, v_now, TRUE, id_pag12, id_proc_botox, 'Aplicação de Toxina Botulínica', 1200.00, 1),
        (v_now, v_now, TRUE, id_pag12, id_proc_preench, 'Preenchimento Labial', 1500.00, 1);

    RAISE NOTICE 'Seed V8 aplicado com sucesso. Logins: admin/recepcao/financeiro/ana.silva/bruno.costa — senha: 123456';
END $$;
