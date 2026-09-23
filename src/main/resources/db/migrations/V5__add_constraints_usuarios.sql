ALTER TABLE clinica.usuarios ADD CONSTRAINT uc_usuarios_email UNIQUE (email);
ALTER TABLE clinica.usuarios ADD CONSTRAINT uc_usuarios_registro_profissional UNIQUE (registro_profissional);
