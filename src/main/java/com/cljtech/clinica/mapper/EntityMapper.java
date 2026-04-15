package com.cljtech.clinica.mapper;

import com.cljtech.clinica.data.Usuario;
import com.cljtech.clinica.records.UsuarioRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EntityMapper {

    Usuario toUsuario(UsuarioRequest usuarioRequest);

}
