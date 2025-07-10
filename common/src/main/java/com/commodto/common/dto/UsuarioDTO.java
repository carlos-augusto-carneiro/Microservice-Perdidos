package com.commodto.common.dto;

public record UsuarioDTO(Long id,
    String nome,
    String email,
    boolean emailConfirmado,
    String perfil
) {

}
