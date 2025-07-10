package com.recupera.item.back.itens.item.exception;

public class ItemJaDevolvidoException extends DomainException {

    public ItemJaDevolvidoException() {
        super("Este item já foi devolvido.");
    }
}
