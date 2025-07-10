package com.recupera.item.back.itens.item.exception;

public class NomeItemInvalidoException extends DomainException {

    public NomeItemInvalidoException() {
        super("O nome do item não pode ser vazio.");
    }

}
