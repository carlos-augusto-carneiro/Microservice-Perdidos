package com.recupera.item.back.email.email.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.recupera.item.back.email.email.model.EmailConfirmacaoToken;

@Repository
public interface IEmailConfirmacaoTokenRepository extends JpaRepository<EmailConfirmacaoToken, Long> {

    Optional<EmailConfirmacaoToken> findByToken(String token);

    Optional<EmailConfirmacaoToken> findByUsuarioId(Long usuarioId);

    boolean existsByUsuarioId(Long usuarioId);

}
