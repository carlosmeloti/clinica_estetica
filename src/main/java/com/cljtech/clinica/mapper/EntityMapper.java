package com.cljtech.clinica.mapper;

import com.cljtech.clinica.data.*;
import com.cljtech.clinica.model.records.*;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EntityMapper {

    Usuario toUsuario(UsuarioRequest usuarioRequest);
    UsuarioResponse toUsuarioResponse(Usuario usuario);
    List<UsuarioResponse> toUsuarioResponse(List<Usuario> usuarios);


    LocalAplicacao toLocalAplicacao(LocalAplicacaoRequestResponse request);
    List<LocalAplicacao> toLocalAplicacao(List<LocalAplicacaoRequestResponse> requests);
    LocalAplicacaoRequestResponse toLocalAplicacaoRequest(LocalAplicacao localAplicacao);
    List<LocalAplicacaoRequestResponse> toLocalAplicacaoRequest(List<LocalAplicacao> localAplicacoes);

    List<InsumoRequestResponse> toInsumoRequestResponse(List<Insumo> insumos);
    Insumo toInsumo(InsumoRequestResponse request);

    List<Insumo> toInsumo(List<InsumoRequestResponse> requests);
    InsumoRequestResponse toInsumoRequestResponse(Insumo insumo);

    Procedimento toProcedimento(ProcedimentoRequestResponse request);
    List<Procedimento> toProcedimento(List<ProcedimentoRequestResponse> requests);
    ProcedimentoRequestResponse toProcedimentoRequestResponse(Procedimento procedimento);
    List<ProcedimentoRequestResponse> toProcedimentoRequestResponse(List<Procedimento> procedimentos);

    Paciente toPaciente(PacienteRequestResponse request);
    PacienteRequestResponse toPacienteRequestResponse(Paciente paciente);
    List<PacienteRequestResponse> toPacienteRequestResponse(List<Paciente> pacientes);
    List<Paciente> toPaciente(List<PacienteRequestResponse> requests);


    @Mapping(source = "paciente.id", target = "pacienteId")
    @Mapping(source = "profissional.id", target = "profissionalId")
    @Mapping(source = "procedimento.id", target = "procedimentoId")
    AgendamentoRequestResponse toAgendamentoRequestResponse(Agendamento agendamento);

    @Mapping(source = "pacienteId", target = "paciente.id")
    @Mapping(source = "profissionalId", target = "profissional.id")
    @Mapping(source = "procedimentoId", target = "procedimento.id")
    Agendamento toAgendamento(AgendamentoRequestResponse request);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "pacienteId", target = "paciente.id")
    @Mapping(source = "profissionalId", target = "profissional.id")
    @Mapping(source = "procedimentoId", target = "procedimento.id")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAgendamentoFromRequest(
            AgendamentoRequestResponse request,
            @MappingTarget Agendamento agendamento
    );
    List<AgendamentoRequestResponse> toAgendamentoRequestResponse(List<Agendamento> agendamentos);
    List<Agendamento> toAgendamento(List<AgendamentoRequestResponse> requests);

    List<EvolucaoEsteticaRequestResponse> toEvolucaoClinicaRequestResponseList(List<EvolucaoClinica> evolucaoClinicas);
    @Mapping(source = "agendamento.id", target = "agendamentoId")
    @Mapping(source = "paciente.id", target = "pacienteId")
    @Mapping(source = "profissional.id", target = "profissionalId")
    EvolucaoEsteticaRequestResponse toEvolucaoClinicaRequestRessponse(EvolucaoEstetica evolucaoEstetica);

    @Mapping(source = "agendamentoId", target = "agendamento.id")
    @Mapping(source = "pacienteId", target = "paciente.id")
    @Mapping(source = "profissionalId", target = "profissional.id")
    EvolucaoEstetica toEvolucaoClinica(EvolucaoEsteticaRequestResponse request);
    List<EvolucaoClinica> toEvolucaoClinica(List<EvolucaoEsteticaRequestResponse> requests);

}
