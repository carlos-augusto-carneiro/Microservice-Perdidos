package com.recupera.item.back.itens.item.dto;

import javax.xml.crypto.Data;

public record CriarItemDto(String nome, String descricao, Data dataCriacao, boolean devolvido) {

}
