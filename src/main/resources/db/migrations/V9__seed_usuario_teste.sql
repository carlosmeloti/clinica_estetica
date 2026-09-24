-- Usuário de teste para login rápido em desenvolvimento.
-- Login: teste | Senha: 123456 | Perfil: ADMIN
-- Idempotente: só insere se o login 'teste' ainda não existir.

INSERT INTO clinica.usuarios (
    data_criacao,
    data_atualizacao,
    ativo,
    login,
    senha,
    nome,
    email,
    perfil,
    registro_profissional
)
SELECT
    NOW(),
    NOW(),
    TRUE,
    'teste',
    -- BCrypt de '123456' (mesmo hash do seed V8)
    '$2a$10$1vVyvt5zfrMKK5giwPNrwuHQxvo9udt/G.DSno8UmjrUKngjH6Fje',
    'Usuário Teste',
    'teste@clinica.test',
    'ADMIN',
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM clinica.usuarios WHERE login = 'teste'
);
