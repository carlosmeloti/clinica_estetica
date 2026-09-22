package com.cljtech.clinica.service;

import com.cljtech.clinica.data.*;
import com.cljtech.clinica.data.repository.*;
import com.cljtech.clinica.exception.RegraNegocioException;
import com.cljtech.clinica.mapper.EntityMapper;
import com.cljtech.clinica.model.enuns.StatusAgendamento;
import com.cljtech.clinica.model.records.AtendimentoRequest;
import com.cljtech.clinica.model.records.AtendimentoResponse;
import com.cljtech.clinica.model.records.InsumoRequestResponse;
import com.cljtech.clinica.service.impl.AtendimentoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtendimentoServiceTest {

    @Mock
    private EvolucaoClinicaRepository evolucaoRepository;
    @Mock
    private EvolucaoEsteticaRepository evolucaoEsteticaRepository;
    @Mock
    private AgendamentoRepository agendamentoRepository;
    @Mock
    private ProcedimentoRepository procedimentoRepository;
    @Mock
    private LocalAplicacaoRepository localAplicacaoRepository;
    @Mock
    private ConsumoInsumoRepository consumoInsumoRepository;
    @Mock
    private InsumosRepository insumosRepository;
    @Mock
    private EntityMapper entityMapper;
    @Mock
    private AgendamentoService agendamentoService;

    @InjectMocks
    private AtendimentoServiceImpl atendimentoService;

    private Agendamento agendamento;
    private AtendimentoRequest request;

    @BeforeEach
    void setUp() {
        agendamento = new Agendamento();
        agendamento.setId(1L);
        agendamento.setStatus(StatusAgendamento.CONFIRMADO);
        agendamento.setPaciente(new Paciente());
        agendamento.setProfissional(new Usuario());

        request = new AtendimentoRequest(
                1L, "Relato", "Notas", "Obs", "Orienta", "Inter", "Retorno",
                List.of(1L), List.of(1L), new ArrayList<>(), 1, 70.0, 10.0
        );
    }

    @Test
    void criarAtendimentoComSucesso() {
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));
        when(evolucaoRepository.existsByAgendamentoId(1L)).thenReturn(false);
        when(evolucaoEsteticaRepository.save(any())).thenReturn(new EvolucaoEstetica());
        
        // Simular o mapeamento de resposta para evitar NullPointerException no populateConsumos
        AtendimentoResponse mockResponse = mock(AtendimentoResponse.class);
        when(entityMapper.toAtendimentoResponse(any(EvolucaoEstetica.class))).thenReturn(mockResponse);

        AtendimentoResponse response = atendimentoService.criarAtendimento(request);

        assertNotNull(response);
        verify(agendamentoService).mudarStatus(eq(1L), eq(StatusAgendamento.EM_ATENDIMENTO));
        verify(evolucaoEsteticaRepository).save(any());
    }

    @Test
    void impedirAtendimentoParaAgendamentoCancelado() {
        agendamento.setStatus(StatusAgendamento.CANCELADO);
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));

        assertThrows(RegraNegocioException.class, () -> atendimentoService.criarAtendimento(request));
    }

    @Test
    void impedirAtendimentoDuplicado() {
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));
        when(evolucaoRepository.existsByAgendamentoId(1L)).thenReturn(true);

        assertThrows(RegraNegocioException.class, () -> atendimentoService.criarAtendimento(request));
    }

    @Test
    void finalizarAtendimentoComSucesso() {
        EvolucaoEstetica evolucao = new EvolucaoEstetica();
        evolucao.setId(1L);
        evolucao.setAgendamento(agendamento);
        
        when(evolucaoEsteticaRepository.findById(1L)).thenReturn(Optional.of(evolucao));
        when(evolucaoEsteticaRepository.save(any())).thenReturn(evolucao);
        
        AtendimentoResponse mockResponse = mock(AtendimentoResponse.class);
        when(entityMapper.toAtendimentoResponse(any(EvolucaoEstetica.class))).thenReturn(mockResponse);

        AtendimentoResponse response = atendimentoService.finalizarAtendimento(1L);

        assertNotNull(response);
        assertTrue(evolucao.getFinalizado());
        verify(agendamentoService).mudarStatus(eq(1L), eq(StatusAgendamento.CONCLUIDO));
    }
}
