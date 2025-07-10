package com.recupera.item.back.itens.item.exception;

public class UsuarioInvalidoException extends DomainException {

    public UsuarioInvalidoException() {
        super("O ID do usuário é inválido.");
    }

}
