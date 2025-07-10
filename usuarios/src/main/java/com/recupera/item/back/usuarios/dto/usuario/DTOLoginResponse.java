package com.recupera.item.back.usuarios.dto.usuario;

public record DTOLoginResponse(String acessString, Long expiresIn) {

    public DTOLoginResponse(String acessString, Long expiresIn) {
        this.acessString = acessString;
        this.expiresIn = expiresIn;
    }
}
