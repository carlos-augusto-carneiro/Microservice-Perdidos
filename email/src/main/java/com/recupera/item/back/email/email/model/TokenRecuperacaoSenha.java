package com.recupera.item.back.email.email.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.time.LocalDateTime;
import jakarta.persistence.Column;

@Entity
@Table(name = "token_recuperacao_senha")
public class TokenRecuperacaoSenha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private LocalDateTime dataExpiracao;

    @Column(name = "usuario_id")
    private Long usuarioId;
    

    public TokenRecuperacaoSenha() {
    }

    public boolean estaExpirado() {
        return dataExpiracao.isBefore(LocalDateTime.now());
    }

    public boolean pertenceAoUsuario(Long usuarioId) {
        return this.usuarioId.equals(usuarioId);
    }

    public static TokenRecuperacaoSenha gerarPara(Long usuarioId) {
        TokenRecuperacaoSenha token = new TokenRecuperacaoSenha();
        token.token = java.util.UUID.randomUUID().toString();
        token.dataExpiracao = LocalDateTime.now().plusHours(1);
        token.usuarioId = usuarioId;
        return token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getDataExpiracao() {
        return dataExpiracao;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setDataExpiracao(LocalDateTime dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

}