package com.cljtech.clinica.data.repository;

import com.cljtech.clinica.data.Usuario;
import com.cljtech.clinica.model.enuns.PerfilUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {
    Optional<Usuario> findByLogin(String login);
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByRegistroProfissional(String registroProfissional);
    List<Usuario> findByPerfil(PerfilUsuario perfil);
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);
    boolean existsByRegistroProfissional(String registroProfissional);
}
