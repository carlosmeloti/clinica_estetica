package com.cljtech.clinica.service;

import com.cljtech.clinica.data.Agendamento;
import com.cljtech.clinica.data.Paciente;
import com.cljtech.clinica.data.Pagamento;
import com.cljtech.clinica.data.Procedimento;
import com.cljtech.clinica.data.Usuario;
import com.cljtech.clinica.data.repository.AgendamentoRepository;
import com.cljtech.clinica.data.repository.PagamentoRepository;
import com.cljtech.clinica.data.repository.ProcedimentoRepository;
import com.cljtech.clinica.data.repository.UsuarioRepository;
import com.cljtech.clinica.exception.RecursoNaoEncontradoException;
import com.cljtech.clinica.exception.RegraNegocioException;
import com.cljtech.clinica.model.enuns.FormaPagamento;
import com.cljtech.clinica.model.enuns.PerfilUsuario;
import com.cljtech.clinica.model.enuns.StatusAgendamento;
import com.cljtech.clinica.model.enuns.StatusPagamento;
import com.cljtech.clinica.model.records.PagamentoRequest;
import com.cljtech.clinica.model.records.PagamentoResponse;
import com.cljtech.clinica.model.records.RelatorioCaixaResponse;
import com.cljtech.clinica.model.records.SugestaoCobrancaResponse;
import com.cljtech.clinica.service.impl.CaixaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CaixaServiceTest {

    @Mock
    private PagamentoRepository pagamentoRepository;
    @Mock
    private AgendamentoRepository agendamentoRepository;
    @Mock
    private ProcedimentoRepository procedimentoRepository;
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private CaixaServiceImpl caixaService;

    private Agendamento agendamento;
    private Procedimento procedimento;

    @BeforeEach
    void setUp() {
        Paciente paciente = new Paciente();
        paciente.setId(1L);
        paciente.setNome("Maria");

        Usuario profissional = new Usuario();
        profissional.setId(2L);
        profissional.setNome("Dra. Ana");
        profissional.setPerfil(PerfilUsuario.PROFISSIONAL);

        procedimento = new Procedimento();
        procedimento.setId(10L);
        procedimento.setNome("Limpeza de Pele");
        procedimento.setPrecoSugerido(new BigDecimal("150.00"));

        agendamento = new Agendamento();
        agendamento.setId(100L);
        agendamento.setPaciente(paciente);
        agendamento.setProfissional(profissional);
        agendamento.setProcedimentos(List.of(procedimento));
        agendamento.setStatus(StatusAgendamento.CONCLUIDO);
        agendamento.setDataHoraInicio(LocalDateTime.now().minusHours(1));
        agendamento.setDataHoraFim(LocalDateTime.now());
        agendamento.setValorPrevisto(new BigDecimal("150.00"));
    }

    @Test
    void registrarPagamentoComItensDoAgendamento() {
        when(agendamentoRepository.findById(100L)).thenReturn(Optional.of(agendamento));
        when(pagamentoRepository.save(any(Pagamento.class))).thenAnswer(inv -> {
            Pagamento p = inv.getArgument(0);
            p.setId(1L);
            return p;
        });

        PagamentoRequest request = new PagamentoRequest(
                100L,
                BigDecimal.ZERO,
                new BigDecimal("150.00"),
                FormaPagamento.PIX,
                null,
                null,
                null,
                null,
                null
        );

        PagamentoResponse response = caixaService.registrarPagamento(request);

        assertEquals(StatusPagamento.PAGO, response.status());
        assertEquals(new BigDecimal("150.00"), response.valorBruto());
        assertEquals(1, response.itens().size());
        assertEquals("Limpeza de Pele", response.itens().getFirst().descricao());
        verify(pagamentoRepository).save(any(Pagamento.class));
    }

    @Test
    void impedirPagamentoEmAgendamentoCancelado() {
        agendamento.setStatus(StatusAgendamento.CANCELADO);
        when(agendamentoRepository.findById(100L)).thenReturn(Optional.of(agendamento));

        PagamentoRequest request = new PagamentoRequest(
                100L, null, new BigDecimal("100.00"), FormaPagamento.DINHEIRO,
                null, null, null, null, null
        );

        assertThrows(RegraNegocioException.class, () -> caixaService.registrarPagamento(request));
    }

    @Test
    void sugerirCobrancaComSaldoEmAberto() {
        when(agendamentoRepository.findById(100L)).thenReturn(Optional.of(agendamento));
        when(pagamentoRepository.somarValorPagoPorAgendamento(eq(100L), eq(StatusPagamento.ESTORNADO)))
                .thenReturn(new BigDecimal("50.00"));

        SugestaoCobrancaResponse sugestao = caixaService.sugerirCobranca(100L);

        assertEquals(new BigDecimal("150.00"), sugestao.valorSugerido());
        assertEquals(new BigDecimal("50.00"), sugestao.totalJaPago());
        assertEquals(new BigDecimal("100.00"), sugestao.saldoEmAberto());
    }

    @Test
    void gerarRelatorioAgregaTotais() {
        Pagamento pagamento = new Pagamento();
        pagamento.setId(1L);
        pagamento.setAgendamento(agendamento);
        pagamento.setValorBruto(new BigDecimal("150.00"));
        pagamento.setDesconto(BigDecimal.ZERO);
        pagamento.setValorPago(new BigDecimal("150.00"));
        pagamento.setFormaPagamento(FormaPagamento.PIX);
        pagamento.setStatus(StatusPagamento.PAGO);
        pagamento.setDataPagamento(LocalDateTime.of(2026, 9, 23, 10, 0));
        pagamento.adicionarItem(criarItem(pagamento));

        when(pagamentoRepository.findAll(any(Specification.class), any(Sort.class)))
                .thenReturn(List.of(pagamento));
        when(agendamentoRepository.findAll(any(Specification.class), any(Sort.class)))
                .thenReturn(List.of(agendamento));
        when(pagamentoRepository.somarValorPagoPorAgendamento(eq(100L), eq(StatusPagamento.ESTORNADO)))
                .thenReturn(new BigDecimal("150.00"));

        RelatorioCaixaResponse relatorio = caixaService.gerarRelatorio(
                LocalDate.of(2026, 9, 1),
                LocalDate.of(2026, 9, 30),
                null
        );

        assertEquals(new BigDecimal("150.00"), relatorio.totalRecebido());
        assertEquals(1, relatorio.quantidadePagamentos());
        assertFalse(relatorio.porFormaPagamento().isEmpty());
        assertFalse(relatorio.porDia().isEmpty());
    }

    @Test
    void estornarPagamento() {
        Pagamento pagamento = new Pagamento();
        pagamento.setId(1L);
        pagamento.setAgendamento(agendamento);
        pagamento.setValorBruto(new BigDecimal("150.00"));
        pagamento.setDesconto(BigDecimal.ZERO);
        pagamento.setValorPago(new BigDecimal("150.00"));
        pagamento.setFormaPagamento(FormaPagamento.DINHEIRO);
        pagamento.setStatus(StatusPagamento.PAGO);
        pagamento.setDataPagamento(LocalDateTime.now());

        when(pagamentoRepository.findById(1L)).thenReturn(Optional.of(pagamento));
        when(pagamentoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        PagamentoResponse response = caixaService.estornar(1L, "digitado errado");

        assertEquals(StatusPagamento.ESTORNADO, response.status());
        assertTrue(response.observacao().contains("Estorno"));
    }

    @Test
    void agendamentoInexistenteNaSugestao() {
        when(agendamentoRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(RecursoNaoEncontradoException.class, () -> caixaService.sugerirCobranca(999L));
    }

    private com.cljtech.clinica.data.PagamentoItem criarItem(Pagamento pagamento) {
        com.cljtech.clinica.data.PagamentoItem item = new com.cljtech.clinica.data.PagamentoItem();
        item.setPagamento(pagamento);
        item.setProcedimento(procedimento);
        item.setDescricao(procedimento.getNome());
        item.setValorUnitario(procedimento.getPrecoSugerido());
        item.setQuantidade(1);
        return item;
    }
}
