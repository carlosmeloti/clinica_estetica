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
import com.cljtech.clinica.model.enuns.StatusAgendamento;
import com.cljtech.clinica.model.records.AgendamentoRequest;
import com.cljtech.clinica.model.records.AgendamentoResponse;
import com.cljtech.clinica.model.records.ProcedimentoRequestResponse;
import com.cljtech.clinica.service.impl.AgendamentoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(new Paciente()));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(new Usuario()));
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
}
