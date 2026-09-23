package com.cljtech.clinica.service;

import com.cljtech.clinica.data.Agendamento;
import com.cljtech.clinica.data.Paciente;
import com.cljtech.clinica.data.Procedimento;
import com.cljtech.clinica.data.Usuario;
import com.cljtech.clinica.data.repository.AgendamentoRepository;
import com.cljtech.clinica.data.repository.PacienteRepository;
import com.cljtech.clinica.data.repository.ProcedimentoRepository;
import com.cljtech.clinica.data.repository.UsuarioRepository;
import com.cljtech.clinica.exception.ConflitoException;
import com.cljtech.clinica.exception.RegraNegocioException;
import com.cljtech.clinica.mapper.EntityMapper;
import com.cljtech.clinica.model.records.AgendamentoRequest;
import com.cljtech.clinica.model.records.AgendamentoResponse;
import com.cljtech.clinica.model.records.ProcedimentoRequestResponse;
import com.cljtech.clinica.service.impl.AgendamentoServiceImpl;
import com.cljtech.clinica.model.enuns.PerfilUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class AgendamentoServiceTest {

    @Mock
    private AgendamentoRepository agendamentoRepository;
    @Mock
    private PacienteRepository pacienteRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private ProcedimentoRepository procedimentoRepository;
    @Mock
    private EntityMapper entityMapper;

    @InjectMocks
    private AgendamentoServiceImpl agendamentoService;

    private AgendamentoRequest request;
    private Agendamento agendamento;

    @BeforeEach
    void setUp() {
        request = new AgendamentoRequest(
                null, 1L, 1L,
                List.of(new ProcedimentoRequestResponse(1L, "Limpeza", null, null)),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(1),
                null, null, null
        );
        agendamento = new Agendamento();
        agendamento.setId(1L);
    }

    @Test
    void criarAgendamentoComSucesso() {
        Usuario profissional = new Usuario();
        profissional.setPerfil(PerfilUsuario.PROFISSIONAL);
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(new Paciente()));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(procedimentoRepository.findAllByIdIn(any())).thenReturn(List.of(new Procedimento()));
        when(agendamentoRepository.existeAgendamentoNoMesmoHorario(any(), any(), any(), any(), any())).thenReturn(false);
        when(entityMapper.toAgendamento(request)).thenReturn(agendamento);
        when(agendamentoRepository.save(any())).thenReturn(agendamento);
        when(entityMapper.toAgendamentoRequestResponse(agendamento)).thenReturn(mock(AgendamentoResponse.class));

        AgendamentoResponse result = agendamentoService.criar(request);

        assertNotNull(result);
        verify(agendamentoRepository).save(any());
    }

    @Test
    void deveLancarExcecaoAoAgendarParaUsuarioNaoProfissional() {
        Usuario admin = new Usuario();
        admin.setPerfil(PerfilUsuario.ADMIN);
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(new Paciente()));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(admin));

        assertThrows(RegraNegocioException.class, () -> agendamentoService.criar(request));
    }

    @Test
    void impedirAgendamentoComConflitoDeHorario() {
        when(agendamentoRepository.existeAgendamentoNoMesmoHorario(any(), any(), any(), any(), any())).thenReturn(true);

        assertThrows(ConflitoException.class, () -> agendamentoService.criar(request));
    }

    @Test
    void impedirAgendamentoComDataFinalAntesDaInicial() {
        AgendamentoRequest invalidRequest = new AgendamentoRequest(
                null, 1L, 1L,
                List.of(new ProcedimentoRequestResponse(1L, "Limpeza", null, null)),
                LocalDateTime.now().plusHours(2),
                LocalDateTime.now().plusHours(1),
                null, null, null
        );

        assertThrows(RegraNegocioException.class, () -> agendamentoService.criar(invalidRequest));
    }

    @Test
    void listarAgendaGeralSemProfissional() {
        when(agendamentoRepository.findAll(any(Specification.class), any(Sort.class)))
                .thenReturn(List.of(agendamento));
        when(entityMapper.toAgendamentoRequestResponse(agendamento)).thenReturn(mock(AgendamentoResponse.class));

        LocalDate inicio = LocalDate.of(2026, 9, 1);
        LocalDate fim = LocalDate.of(2026, 9, 30);

        List<AgendamentoResponse> resultado = agendamentoService.listarAgenda(inicio, fim, null, null);

        assertEquals(1, resultado.size());
        verify(agendamentoRepository).findAll(any(Specification.class), eq(Sort.by("dataHoraInicio")));
        verify(usuarioRepository, never()).findById(any());
    }

    @Test
    void listarAgendaPorProfissional() {
        Usuario profissional = new Usuario();
        profissional.setPerfil(PerfilUsuario.PROFISSIONAL);
        when(usuarioRepository.findById(10L)).thenReturn(Optional.of(profissional));
        when(agendamentoRepository.findAll(any(Specification.class), any(Sort.class)))
                .thenReturn(List.of(agendamento));
        when(entityMapper.toAgendamentoRequestResponse(agendamento)).thenReturn(mock(AgendamentoResponse.class));

        LocalDate dia = LocalDate.of(2026, 9, 23);
        List<AgendamentoResponse> resultado = agendamentoService.listarAgenda(dia, dia, 10L, null);

        assertEquals(1, resultado.size());
        verify(usuarioRepository).findById(10L);
    }

    @Test
    void listarAgendaComIntervaloInvalido() {
        LocalDate inicio = LocalDate.of(2026, 9, 30);
        LocalDate fim = LocalDate.of(2026, 9, 1);

        assertThrows(RegraNegocioException.class,
                () -> agendamentoService.listarAgenda(inicio, fim, null, null));
    }

    @Test
    void listarAgendaComIntervaloMaiorQueLimite() {
        LocalDate inicio = LocalDate.of(2026, 1, 1);
        LocalDate fim = LocalDate.of(2026, 5, 1);

        assertThrows(RegraNegocioException.class,
                () -> agendamentoService.listarAgenda(inicio, fim, null, null));
    }
}
