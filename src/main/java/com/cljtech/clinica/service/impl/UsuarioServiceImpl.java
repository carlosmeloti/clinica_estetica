package com.cljtech.clinica.service.impl;

import com.cljtech.clinica.data.Usuario;
import com.cljtech.clinica.data.repository.UsuarioRepository;
import com.cljtech.clinica.exception.RecursoNaoEncontradoException;
import com.cljtech.clinica.exception.RegraNegocioException;
import com.cljtech.clinica.mapper.EntityMapper;
import com.cljtech.clinica.model.enuns.PerfilUsuario;
import com.cljtech.clinica.model.records.ProfissionalResponse;
import com.cljtech.clinica.model.records.UsuarioRequest;
import com.cljtech.clinica.model.records.UsuarioResponse;
import com.cljtech.clinica.service.UsuarioService;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityMapper entityMapper;

    @Override
    public UsuarioResponse salvar(UsuarioRequest request) {
        validarNovoUsuario(request);

        Usuario usuario = entityMapper.toUsuario(request);
        if (!StringUtils.hasText(request.senha())) {
            throw new RegraNegocioException("Senha é obrigatória para novos usuários.");
        }
        usuario.setSenha(passwordEncoder.encode(request.senha()));
        usuario.setAtivo(true);

        return entityMapper.toUsuarioResponse(usuarioRepository.save(usuario));
    }

    @Override
    public UsuarioResponse atualizar(Long id, UsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        validarAtualizacaoUsuario(id, request);

        usuario.setNome(request.nome());
        usuario.setLogin(request.login());
        usuario.setEmail(request.email());
        usuario.setPerfil(request.perfil());
        usuario.setRegistroProfissional(request.registroProfissional());

        if (StringUtils.hasText(request.senha())) {
            usuario.setSenha(passwordEncoder.encode(request.senha()));
        }

        return entityMapper.toUsuarioResponse(usuarioRepository.save(usuario));
    }

    @Override
    public void deletar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Usuário não encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(entityMapper::toUsuarioResponse)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorLogin(String login) {
        return usuarioRepository.findByLogin(login)
                .map(entityMapper::toUsuarioResponse)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UsuarioResponse> buscarComFiltros(String nome, String email, String login, PerfilUsuario perfil, Pageable pageable) {
        Specification<Usuario> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(nome)) {
                predicates.add(cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"));
            }
            if (StringUtils.hasText(email)) {
                predicates.add(cb.equal(cb.lower(root.get("email")), email.toLowerCase()));
            }
            if (StringUtils.hasText(login)) {
                predicates.add(cb.equal(cb.lower(root.get("login")), login.toLowerCase()));
            }
            if (perfil != null) {
                predicates.add(cb.equal(root.get("perfil"), perfil));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return usuarioRepository.findAll(spec, pageable).map(entityMapper::toUsuarioResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProfissionalResponse> listarProfissionais() {
        return entityMapper.toProfissionalResponse(usuarioRepository.findByPerfil(PerfilUsuario.PROFISSIONAL));
    }

    private void validarNovoUsuario(UsuarioRequest request) {
        if (usuarioRepository.existsByLogin(request.login())) {
            throw new RegraNegocioException("Já existe um usuário com este login.");
        }
        if (StringUtils.hasText(request.email()) && usuarioRepository.existsByEmail(request.email())) {
            throw new RegraNegocioException("Já existe um usuário com este e-mail.");
        }
        validarRegistroProfissional(null, request);
    }

    private void validarAtualizacaoUsuario(Long id, UsuarioRequest request) {
        usuarioRepository.findByLogin(request.login())
                .ifPresent(u -> {
                    if (!u.getId().equals(id)) {
                        throw new RegraNegocioException("Já existe outro usuário com este login.");
                    }
                });

        if (StringUtils.hasText(request.email())) {
            usuarioRepository.findByEmail(request.email())
                    .ifPresent(u -> {
                        if (!u.getId().equals(id)) {
                            throw new RegraNegocioException("Já existe outro usuário com este e-mail.");
                        }
                    });
        }
        validarRegistroProfissional(id, request);
    }

    private void validarRegistroProfissional(Long id, UsuarioRequest request) {
        if (request.perfil() == PerfilUsuario.PROFISSIONAL) {
            if (!StringUtils.hasText(request.registroProfissional())) {
                throw new RegraNegocioException("Registro profissional é obrigatório para profissionais.");
            }
            usuarioRepository.findByRegistroProfissional(request.registroProfissional())
                    .ifPresent(u -> {
                        if (id == null || !u.getId().equals(id)) {
                            throw new RegraNegocioException("Já existe um profissional cadastrado com este registro.");
                        }
                    });
        }
    }
}

