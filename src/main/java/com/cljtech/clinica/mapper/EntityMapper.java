package com.cljtech.clinica.mapper;

import com.cljtech.clinica.data.Insumo;
import com.cljtech.clinica.data.LocalAplicacao;
import com.cljtech.clinica.data.Usuario;
import com.cljtech.clinica.model.records.InsumoRequestResponse;
import com.cljtech.clinica.model.records.LocalAplicacaoRequestResponse;
import com.cljtech.clinica.model.records.UsuarioRequest;
import com.cljtech.clinica.model.records.UsuarioResponse;
import org.mapstruct.Mapper;

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

}
