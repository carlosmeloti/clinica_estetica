package com.cljtech.clinica.service.impl;

import com.cljtech.clinica.data.*;
import com.cljtech.clinica.data.repository.AgendamentoRepository;
import com.cljtech.clinica.data.repository.PagamentoRepository;
import com.cljtech.clinica.data.repository.ProcedimentoRepository;
import com.cljtech.clinica.data.repository.UsuarioRepository;
import com.cljtech.clinica.exception.RecursoNaoEncontradoException;
import com.cljtech.clinica.exception.RegraNegocioException;
import com.cljtech.clinica.model.enuns.FormaPagamento;
import com.cljtech.clinica.model.enuns.StatusAgendamento;
import com.cljtech.clinica.model.enuns.StatusPagamento;
import com.cljtech.clinica.model.records.*;
import com.cljtech.clinica.security.CustomUserDetails;
import com.cljtech.clinica.service.CaixaService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CaixaServiceImpl implements CaixaService {

    private static final int MAX_DIAS_RELATORIO = 366;
    private static final Set<StatusAgendamento> STATUS_ELEGIVEIS_COBRANCA = EnumSet.of(
            StatusAgendamento.AGENDADO,
            StatusAgendamento.CONFIRMADO,
            StatusAgendamento.EM_ATENDIMENTO,
            StatusAgendamento.CONCLUIDO
    );

    private final PagamentoRepository pagamentoRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final ProcedimentoRepository procedimentoRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public PagamentoResponse registrarPagamento(PagamentoRequest request) {
        Agendamento agendamento = agendamentoRepository.findById(request.agendamentoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Agendamento não encontrado"));

        if (agendamento.getStatus() == StatusAgendamento.CANCELADO
                || agendamento.getStatus() == StatusAgendamento.NAO_COMPARECEU) {
            throw new RegraNegocioException("Não é possível registrar pagamento para agendamento cancelado ou com não comparecimento.");
        }

        List<PagamentoItem> itens;
        if (request.itens() == null || request.itens().isEmpty()) {
            itens = montarItensEntidadeDoAgendamento(agendamento);
        } else {
            itens = request.itens().stream().map(this::criarItem).toList();
        }

        BigDecimal valorBruto = request.valorBruto() != null
                ? request.valorBruto()
                : somarItensEntidade(itens);

        BigDecimal desconto = request.desconto() != null ? request.desconto() : BigDecimal.ZERO;
        if (desconto.compareTo(valorBruto) > 0) {
            throw new RegraNegocioException("Desconto não pode ser maior que o valor bruto.");
        }

        BigDecimal valorLiquido = valorBruto.subtract(desconto);
        BigDecimal valorPago = request.valorPago();
        StatusPagamento status = request.status() != null
                ? request.status()
                : calcularStatus(valorPago, valorLiquido);

        Pagamento pagamento = new Pagamento();
        pagamento.setAgendamento(agendamento);
        pagamento.setValorBruto(valorBruto);
        pagamento.setDesconto(desconto);
        pagamento.setValorPago(valorPago);
        pagamento.setFormaPagamento(request.formaPagamento());
        pagamento.setStatus(status);
        pagamento.setDataPagamento(
                request.dataPagamento() != null ? request.dataPagamento() : LocalDateTime.now()
        );
        pagamento.setObservacao(request.observacao());
        pagamento.setRegistradoPor(obterUsuarioLogado().orElse(null));

        for (PagamentoItem item : itens) {
            pagamento.adicionarItem(item);
        }

        return toResponse(pagamentoRepository.save(pagamento));
    }

    @Override
    @Transactional(readOnly = true)
    public PagamentoResponse buscarPorId(Long id) {
        return toResponse(buscarPagamento(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagamentoResponse> listarPagamentos(
            LocalDate dataInicio,
            LocalDate dataFim,
            Long profissionalId,
            FormaPagamento formaPagamento,
            StatusPagamento status
    ) {
        validarPeriodo(dataInicio, dataFim);

        return pagamentoRepository
                .findAll(
                        criarSpecPagamentos(dataInicio, dataFim, profissionalId, formaPagamento, status),
                        Sort.by(Sort.Direction.ASC, "dataPagamento")
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public PagamentoResponse estornar(Long id, String motivo) {
        Pagamento pagamento = buscarPagamento(id);
        if (pagamento.getStatus() == StatusPagamento.ESTORNADO) {
            throw new RegraNegocioException("Pagamento já está estornado.");
        }
        pagamento.setStatus(StatusPagamento.ESTORNADO);
        if (motivo != null && !motivo.isBlank()) {
            String obs = pagamento.getObservacao() == null ? "" : pagamento.getObservacao() + " | ";
            pagamento.setObservacao(obs + "Estorno: " + motivo.trim());
        }
        pagamento.setDataAtualizacao(LocalDateTime.now());
        return toResponse(pagamentoRepository.save(pagamento));
    }

    @Override
    @Transactional(readOnly = true)
    public SugestaoCobrancaResponse sugerirCobranca(Long agendamentoId) {
        Agendamento agendamento = agendamentoRepository.findById(agendamentoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Agendamento não encontrado"));

        List<PagamentoItemRequest> itens = montarItensDoAgendamento(agendamento);
        BigDecimal valorSugerido = somarItens(itens);
        if (valorSugerido.compareTo(BigDecimal.ZERO) == 0 && agendamento.getValorPrevisto() != null) {
            valorSugerido = agendamento.getValorPrevisto();
        }

        BigDecimal totalPago = totalPagoAgendamento(agendamentoId);
        BigDecimal saldo = valorSugerido.subtract(totalPago).max(BigDecimal.ZERO);

        return new SugestaoCobrancaResponse(
                agendamento.getId(),
                agendamento.getPaciente().getId(),
                agendamento.getPaciente().getNome(),
                agendamento.getProfissional().getId(),
                agendamento.getProfissional().getNome(),
                agendamento.getValorPrevisto(),
                valorSugerido,
                totalPago,
                saldo,
                itens.stream().map(this::toItemResponseFromRequest).toList(),
                agendamento.getDataHoraInicio()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContaPendenteResponse> listarContasPendentes(
            LocalDate dataInicio,
            LocalDate dataFim,
            Long profissionalId
    ) {
        validarPeriodo(dataInicio, dataFim);

        List<Agendamento> agendamentos = agendamentoRepository.findAll(
                criarSpecAgendamentosPeriodo(dataInicio, dataFim, profissionalId),
                Sort.by(Sort.Direction.ASC, "dataHoraInicio")
        );

        List<ContaPendenteResponse> pendentes = new ArrayList<>();
        for (Agendamento agendamento : agendamentos) {
            if (!STATUS_ELEGIVEIS_COBRANCA.contains(agendamento.getStatus())) {
                continue;
            }
            BigDecimal sugerido = calcularValorSugerido(agendamento);
            BigDecimal pago = totalPagoAgendamento(agendamento.getId());
            BigDecimal saldo = sugerido.subtract(pago);
            if (saldo.compareTo(BigDecimal.ZERO) > 0) {
                pendentes.add(new ContaPendenteResponse(
                        agendamento.getId(),
                        agendamento.getPaciente().getId(),
                        agendamento.getPaciente().getNome(),
                        agendamento.getProfissional().getId(),
                        agendamento.getProfissional().getNome(),
                        agendamento.getStatus(),
                        agendamento.getDataHoraInicio(),
                        sugerido,
                        pago,
                        saldo
                ));
            }
        }
        return pendentes;
    }

    @Override
    @Transactional(readOnly = true)
    public RelatorioCaixaResponse gerarRelatorio(LocalDate dataInicio, LocalDate dataFim, Long profissionalId) {
        validarPeriodo(dataInicio, dataFim);

        List<Pagamento> pagamentos = pagamentoRepository.findAll(
                criarSpecPagamentos(dataInicio, dataFim, profissionalId, null, null),
                Sort.by(Sort.Direction.ASC, "dataPagamento")
        );

        List<Pagamento> efetivos = pagamentos.stream()
                .filter(p -> p.getStatus() != StatusPagamento.ESTORNADO)
                .toList();

        BigDecimal totalBruto = efetivos.stream()
                .map(Pagamento::getValorBruto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalDescontos = efetivos.stream()
                .map(Pagamento::getDesconto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalRecebido = efetivos.stream()
                .map(Pagamento::getValorPago)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long qtd = efetivos.size();
        BigDecimal ticketMedio = qtd == 0
                ? BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
                : totalRecebido.divide(BigDecimal.valueOf(qtd), 2, RoundingMode.HALF_UP);

        Map<FormaPagamento, List<Pagamento>> porForma = efetivos.stream()
                .collect(Collectors.groupingBy(Pagamento::getFormaPagamento));
        List<RelatorioCaixaResponse.TotalPorChave> totaisForma = porForma.entrySet().stream()
                .map(e -> RelatorioCaixaResponse.TotalPorChave.deForma(
                        e.getKey(),
                        e.getValue().stream().map(Pagamento::getValorPago).reduce(BigDecimal.ZERO, BigDecimal::add),
                        e.getValue().size()
                ))
                .sorted(Comparator.comparing(RelatorioCaixaResponse.TotalPorChave::chave))
                .toList();

        Map<String, Agg> porProc = new LinkedHashMap<>();
        for (Pagamento pagamento : efetivos) {
            for (PagamentoItem item : pagamento.getItens()) {
                String chave = item.getProcedimento() != null
                        ? String.valueOf(item.getProcedimento().getId())
                        : item.getDescricao();
                String rotulo = item.getDescricao();
                BigDecimal subtotal = item.getValorUnitario()
                        .multiply(BigDecimal.valueOf(item.getQuantidade()));
                porProc.computeIfAbsent(chave, k -> new Agg(rotulo)).add(subtotal);
            }
        }
        List<RelatorioCaixaResponse.TotalPorChave> totaisProc = porProc.entrySet().stream()
                .map(e -> new RelatorioCaixaResponse.TotalPorChave(
                        e.getKey(), e.getValue().rotulo, e.getValue().total, e.getValue().quantidade
                ))
                .sorted(Comparator.comparing(RelatorioCaixaResponse.TotalPorChave::total).reversed())
                .toList();

        Map<Long, Agg> porProf = new LinkedHashMap<>();
        for (Pagamento pagamento : efetivos) {
            Usuario prof = pagamento.getAgendamento().getProfissional();
            porProf.computeIfAbsent(prof.getId(), k -> new Agg(prof.getNome()))
                    .add(pagamento.getValorPago());
        }
        List<RelatorioCaixaResponse.TotalPorChave> totaisProf = porProf.entrySet().stream()
                .map(e -> new RelatorioCaixaResponse.TotalPorChave(
                        String.valueOf(e.getKey()), e.getValue().rotulo, e.getValue().total, e.getValue().quantidade
                ))
                .sorted(Comparator.comparing(RelatorioCaixaResponse.TotalPorChave::total).reversed())
                .toList();

        Map<LocalDate, Agg> porDiaMap = new TreeMap<>();
        for (Pagamento pagamento : efetivos) {
            LocalDate dia = pagamento.getDataPagamento().toLocalDate();
            porDiaMap.computeIfAbsent(dia, d -> new Agg(d.toString())).add(pagamento.getValorPago());
        }
        List<RelatorioCaixaResponse.TotalPorDia> porDia = porDiaMap.entrySet().stream()
                .map(e -> new RelatorioCaixaResponse.TotalPorDia(e.getKey(), e.getValue().total, e.getValue().quantidade))
                .toList();

        List<ContaPendenteResponse> pendentes = listarContasPendentes(dataInicio, dataFim, profissionalId);
        BigDecimal valorPendente = pendentes.stream()
                .map(ContaPendenteResponse::saldoEmAberto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new RelatorioCaixaResponse(
                dataInicio,
                dataFim,
                totalBruto,
                totalDescontos,
                totalRecebido,
                qtd,
                ticketMedio,
                pendentes.size(),
                valorPendente,
                totaisForma,
                totaisProc,
                totaisProf,
                porDia,
                efetivos.stream().map(this::toResponse).toList()
        );
    }

    private Pagamento buscarPagamento(Long id) {
        return pagamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pagamento não encontrado"));
    }

    private PagamentoItem criarItem(PagamentoItemRequest itemReq) {
        PagamentoItem item = new PagamentoItem();
        item.setDescricao(itemReq.descricao());
        item.setValorUnitario(itemReq.valorUnitario());
        item.setQuantidade(itemReq.quantidade() != null ? itemReq.quantidade() : 1);

        if (itemReq.procedimentoId() != null) {
            Procedimento procedimento = procedimentoRepository.findById(itemReq.procedimentoId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException(
                            "Procedimento não encontrado: " + itemReq.procedimentoId()
                    ));
            item.setProcedimento(procedimento);
            if (item.getDescricao() == null || item.getDescricao().isBlank()) {
                item.setDescricao(procedimento.getNome());
            }
        }
        return item;
    }

    private List<PagamentoItemRequest> montarItensDoAgendamento(Agendamento agendamento) {
        if (agendamento.getProcedimentos() == null || agendamento.getProcedimentos().isEmpty()) {
            if (agendamento.getValorPrevisto() != null
                    && agendamento.getValorPrevisto().compareTo(BigDecimal.ZERO) > 0) {
                return List.of(new PagamentoItemRequest(
                        null,
                        "Atendimento",
                        agendamento.getValorPrevisto(),
                        1
                ));
            }
            return List.of();
        }

        return agendamento.getProcedimentos().stream()
                .map(p -> new PagamentoItemRequest(
                        p.getId(),
                        p.getNome(),
                        p.getPrecoSugerido() != null ? p.getPrecoSugerido() : BigDecimal.ZERO,
                        1
                ))
                .toList();
    }

    private List<PagamentoItem> montarItensEntidadeDoAgendamento(Agendamento agendamento) {
        if (agendamento.getProcedimentos() == null || agendamento.getProcedimentos().isEmpty()) {
            if (agendamento.getValorPrevisto() != null
                    && agendamento.getValorPrevisto().compareTo(BigDecimal.ZERO) > 0) {
                PagamentoItem item = new PagamentoItem();
                item.setDescricao("Atendimento");
                item.setValorUnitario(agendamento.getValorPrevisto());
                item.setQuantidade(1);
                return List.of(item);
            }
            return List.of();
        }

        return agendamento.getProcedimentos().stream()
                .map(p -> {
                    PagamentoItem item = new PagamentoItem();
                    item.setProcedimento(p);
                    item.setDescricao(p.getNome());
                    item.setValorUnitario(p.getPrecoSugerido() != null ? p.getPrecoSugerido() : BigDecimal.ZERO);
                    item.setQuantidade(1);
                    return item;
                })
                .toList();
    }

    private BigDecimal calcularValorSugerido(Agendamento agendamento) {
        BigDecimal soma = somarItens(montarItensDoAgendamento(agendamento));
        if (soma.compareTo(BigDecimal.ZERO) == 0 && agendamento.getValorPrevisto() != null) {
            return agendamento.getValorPrevisto();
        }
        return soma;
    }

    private BigDecimal somarItens(List<PagamentoItemRequest> itens) {
        return itens.stream()
                .map(i -> i.valorUnitario().multiply(BigDecimal.valueOf(i.quantidade() != null ? i.quantidade() : 1)))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal somarItensEntidade(List<PagamentoItem> itens) {
        return itens.stream()
                .map(i -> i.getValorUnitario().multiply(BigDecimal.valueOf(i.getQuantidade() != null ? i.getQuantidade() : 1)))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal totalPagoAgendamento(Long agendamentoId) {
        return pagamentoRepository.somarValorPagoPorAgendamento(agendamentoId, StatusPagamento.ESTORNADO);
    }

    private StatusPagamento calcularStatus(BigDecimal valorPago, BigDecimal valorLiquido) {
        if (valorLiquido.compareTo(BigDecimal.ZERO) == 0) {
            return StatusPagamento.PAGO;
        }
        if (valorPago.compareTo(valorLiquido) >= 0) {
            return StatusPagamento.PAGO;
        }
        if (valorPago.compareTo(BigDecimal.ZERO) > 0) {
            return StatusPagamento.PARCIAL;
        }
        return StatusPagamento.PENDENTE;
    }

    private void validarPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio == null || dataFim == null) {
            throw new RegraNegocioException("dataInicio e dataFim são obrigatórias.");
        }
        if (dataInicio.isAfter(dataFim)) {
            throw new RegraNegocioException("dataInicio deve ser anterior ou igual a dataFim.");
        }
        long dias = ChronoUnit.DAYS.between(dataInicio, dataFim) + 1;
        if (dias > MAX_DIAS_RELATORIO) {
            throw new RegraNegocioException(
                    "O intervalo do relatório não pode ultrapassar " + MAX_DIAS_RELATORIO + " dias."
            );
        }
    }

    private Specification<Pagamento> criarSpecPagamentos(
            LocalDate dataInicio,
            LocalDate dataFim,
            Long profissionalId,
            FormaPagamento formaPagamento,
            StatusPagamento status
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.greaterThanOrEqualTo(root.get("dataPagamento"), dataInicio.atStartOfDay()));
            predicates.add(cb.lessThanOrEqualTo(root.get("dataPagamento"), dataFim.atTime(LocalTime.MAX)));

            if (profissionalId != null) {
                Join<Pagamento, Agendamento> agendamento = root.join("agendamento");
                predicates.add(cb.equal(agendamento.get("profissional").get("id"), profissionalId));
            }
            if (formaPagamento != null) {
                predicates.add(cb.equal(root.get("formaPagamento"), formaPagamento));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Specification<Agendamento> criarSpecAgendamentosPeriodo(
            LocalDate dataInicio,
            LocalDate dataFim,
            Long profissionalId
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.greaterThanOrEqualTo(root.get("dataHoraInicio"), dataInicio.atStartOfDay()));
            predicates.add(cb.lessThanOrEqualTo(root.get("dataHoraInicio"), dataFim.atTime(LocalTime.MAX)));
            if (profissionalId != null) {
                predicates.add(cb.equal(root.get("profissional").get("id"), profissionalId));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Optional<Usuario> obterUsuarioLogado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return Optional.empty();
        }
        Object principal = auth.getPrincipal();
        if (principal instanceof Usuario usuario) {
            return Optional.of(usuario);
        }
        if (principal instanceof CustomUserDetails details) {
            return Optional.of(details.getUsuario());
        }
        return usuarioRepository.findByLogin(auth.getName());
    }

    private PagamentoResponse toResponse(Pagamento pagamento) {
        Agendamento agendamento = pagamento.getAgendamento();
        BigDecimal valorLiquido = pagamento.getValorBruto().subtract(pagamento.getDesconto());

        List<PagamentoItemResponse> itens = pagamento.getItens().stream()
                .map(item -> new PagamentoItemResponse(
                        item.getId(),
                        item.getProcedimento() != null ? item.getProcedimento().getId() : null,
                        item.getDescricao(),
                        item.getValorUnitario(),
                        item.getQuantidade(),
                        item.getValorUnitario().multiply(BigDecimal.valueOf(item.getQuantidade()))
                ))
                .toList();

        return new PagamentoResponse(
                pagamento.getId(),
                agendamento.getId(),
                agendamento.getPaciente().getId(),
                agendamento.getPaciente().getNome(),
                agendamento.getProfissional().getId(),
                agendamento.getProfissional().getNome(),
                pagamento.getValorBruto(),
                pagamento.getDesconto(),
                valorLiquido,
                pagamento.getValorPago(),
                pagamento.getFormaPagamento(),
                pagamento.getStatus(),
                pagamento.getDataPagamento(),
                pagamento.getObservacao(),
                pagamento.getRegistradoPor() != null ? pagamento.getRegistradoPor().getId() : null,
                pagamento.getRegistradoPor() != null ? pagamento.getRegistradoPor().getNome() : null,
                itens
        );
    }

    private PagamentoItemResponse toItemResponseFromRequest(PagamentoItemRequest item) {
        int qtd = item.quantidade() != null ? item.quantidade() : 1;
        return new PagamentoItemResponse(
                null,
                item.procedimentoId(),
                item.descricao(),
                item.valorUnitario(),
                qtd,
                item.valorUnitario().multiply(BigDecimal.valueOf(qtd))
        );
    }

    private static final class Agg {
        private final String rotulo;
        private BigDecimal total = BigDecimal.ZERO;
        private long quantidade;

        private Agg(String rotulo) {
            this.rotulo = rotulo;
        }

        private void add(BigDecimal valor) {
            total = total.add(valor);
            quantidade++;
        }
    }
}
