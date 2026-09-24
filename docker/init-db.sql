-- Executado na primeira inicialização do volume Postgres
CREATE SCHEMA IF NOT EXISTS clinica AUTHORIZATION CURRENT_USER;
GRANT ALL ON SCHEMA clinica TO CURRENT_USER;
ALTER DATABASE clinica_estetica SET search_path TO clinica, public;
