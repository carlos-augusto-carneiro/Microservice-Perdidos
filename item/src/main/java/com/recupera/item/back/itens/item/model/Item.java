package com.recupera.item.back.itens.item.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.Date;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import com.recupera.item.back.itens.item.exception.NomeItemInvalidoException;
import com.recupera.item.back.itens.item.exception.UsuarioInvalidoException;
import com.recupera.item.back.itens.item.exception.ItemJaDevolvidoException;


@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    public long usuarioid;

    @Temporal(TemporalType.TIMESTAMP)
    public Date dataCriacao;

    public String nome;
    public String descricao;

    public boolean devolvido;


    public Item() {
    }

    public Item(long usuarioid, String nome, String descricao) {
        if (usuarioid <= 0) {
            throw new UsuarioInvalidoException();
        }
        if (nome == null || nome.isEmpty()) {
            throw new NomeItemInvalidoException();
        }
        this.usuarioid = usuarioid;
        this.nome = nome;
        this.descricao = descricao;
        this.dataCriacao = new Date();
        this.devolvido = false;
    }

    public void marcarComoDevolvido() {
        if (this.devolvido) {
            throw new ItemJaDevolvidoException();
        }
        this.devolvido = true;
    }

    

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getUsuarioid() {
        return usuarioid;
    }
    public void setUsuarioid(long usuarioid) {
        this.usuarioid = usuarioid;
    }
    public Date getDataCriacao() {
        return dataCriacao;
    }
    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public boolean isDevolvido() {
        return devolvido;
    }
    public void setDevolvido(boolean devolvido) {
        this.devolvido = devolvido;
    }

}
