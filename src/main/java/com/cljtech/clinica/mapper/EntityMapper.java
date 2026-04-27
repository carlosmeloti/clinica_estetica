package com.cljtech.clinica.mapper;

import com.cljtech.clinica.data.LocalAplicacao;
import com.cljtech.clinica.data.Usuario;
import com.cljtech.clinica.records.LocalAplicacaoRequestResponse;
import com.cljtech.clinica.records.UsuarioRequest;
import com.cljtech.clinica.records.UsuarioResponse;
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

}
