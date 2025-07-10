package com.recupera.item.back.email.email.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.recupera.item.back.email.email.model.TokenRecuperacaoSenha;

@Repository
public interface ITokenRecuperacaoSenha extends JpaRepository<TokenRecuperacaoSenha, Long> {

    Optional<TokenRecuperacaoSenha> findByUsuarioId(Long usuarioId);

}
