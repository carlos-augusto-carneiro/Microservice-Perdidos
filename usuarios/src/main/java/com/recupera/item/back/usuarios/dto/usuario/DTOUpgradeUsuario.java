package com.recupera.item.back.usuarios.dto.usuario;

public record DTOUpgradeUsuario(String nome, String email, String senha) {

    public DTOUpgradeUsuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

}
