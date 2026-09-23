package com.cljtech.clinica.service;

import com.cljtech.clinica.data.Usuario;
import com.cljtech.clinica.data.repository.UsuarioRepository;
import com.cljtech.clinica.exception.RegraNegocioException;
import com.cljtech.clinica.mapper.EntityMapper;
import com.cljtech.clinica.model.enuns.PerfilUsuario;
import com.cljtech.clinica.model.records.ProfissionalResponse;
import com.cljtech.clinica.model.records.UsuarioRequest;
import com.cljtech.clinica.model.records.UsuarioResponse;
import com.cljtech.clinica.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EntityMapper entityMapper;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private UsuarioRequest request;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        request = new UsuarioRequest("login.teste", "senha123", "Nome Teste", "teste@email.com", PerfilUsuario.PROFISSIONAL, "REG123");
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setLogin("login.teste");
        usuario.setNome("Nome Teste");
        usuario.setPerfil(PerfilUsuario.PROFISSIONAL);
        usuario.setRegistroProfissional("REG123");
    }

    @Test
    void deveSalvarUsuarioComSucesso() {
        when(usuarioRepository.existsByLogin(request.login())).thenReturn(false);
        when(usuarioRepository.existsByEmail(request.email())).thenReturn(false);
        when(usuarioRepository.findByRegistroProfissional(request.registroProfissional())).thenReturn(Optional.empty());
        when(entityMapper.toUsuario(request)).thenReturn(usuario);
        when(passwordEncoder.encode(any())).thenReturn("encoded_senha");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(entityMapper.toUsuarioResponse(usuario)).thenReturn(new UsuarioResponse(1L, "Nome Teste", "teste@email.com", "login.teste", "PROFISSIONAL", "REG123"));

        UsuarioResponse response = usuarioService.salvar(request);

        assertNotNull(response);
        assertEquals("Nome Teste", response.nome());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void deveLancarExcecaoAoSalvarUsuarioComLoginExistente() {
        when(usuarioRepository.existsByLogin(request.login())).thenReturn(true);

        assertThrows(RegraNegocioException.class, () -> usuarioService.salvar(request));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoAoSalvarProfissionalSemRegistro() {
        UsuarioRequest requestSemRegistro = new UsuarioRequest("login", "senha", "nome", "email", PerfilUsuario.PROFISSIONAL, null);
        
        assertThrows(RegraNegocioException.class, () -> usuarioService.salvar(requestSemRegistro));
    }

    @Test
    void deveListarProfissionais() {
        when(usuarioRepository.findByPerfil(PerfilUsuario.PROFISSIONAL)).thenReturn(List.of(usuario));
        ProfissionalResponse profResponse = new ProfissionalResponse(1L, "Nome Teste", "teste@email.com", "login.teste", "REG123");
        when(entityMapper.toProfissionalResponse(anyList())).thenReturn(List.of(profResponse));

        List<ProfissionalResponse> profissionais = usuarioService.listarProfissionais();

        assertFalse(profissionais.isEmpty());
        assertEquals(1, profissionais.size());
        assertEquals("REG123", profissionais.get(0).registroProfissional());
    }
}
