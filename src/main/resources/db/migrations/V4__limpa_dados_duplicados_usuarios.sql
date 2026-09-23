-- Limpeza de e-mails duplicados mantendo apenas o registro mais antigo (menor ID)
DELETE FROM clinica.usuarios
WHERE id IN (
    SELECT id
    FROM (
        SELECT id,
               ROW_NUMBER() OVER (PARTITION BY email ORDER BY id) as row_num
        FROM clinica.usuarios
        WHERE email IS NOT NULL
    ) t
    WHERE t.row_num > 1
);

-- Limpeza de registros profissionais duplicados mantendo apenas o registro mais antigo
DELETE FROM clinica.usuarios
WHERE id IN (
    SELECT id
    FROM (
        SELECT id,
               ROW_NUMBER() OVER (PARTITION BY registro_profissional ORDER BY id) as row_num
        FROM clinica.usuarios
        WHERE registro_profissional IS NOT NULL
    ) t
    WHERE t.row_num > 1
);
