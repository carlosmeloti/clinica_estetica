package com.cljtech.clinica.mapper;

import com.cljtech.clinica.data.LocalAplicacao;
import com.cljtech.clinica.data.Usuario;
import com.cljtech.clinica.records.LocalAplicacaoRequest;
import com.cljtech.clinica.records.UsuarioRequest;
import com.cljtech.clinica.records.UsuarioResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EntityMapper {

    Usuario toUsuario(UsuarioRequest usuarioRequest);
    UsuarioResponse toUsuarioResponse(Usuario usuario);
    List<UsuarioResponse> toUsuarioResponse(List<Usuario> usuarios);


    LocalAplicacao toLocalAplicacao(LocalAplicacaoRequest request);
    List<LocalAplicacao> toLocalAplicacao(List<LocalAplicacaoRequest> requests);
    LocalAplicacaoRequest toLocalAplicacaoRequest(LocalAplicacao localAplicacao);
    List<LocalAplicacaoRequest> toLocalAplicacaoRequest(List<LocalAplicacao> localAplicacoes);

}
