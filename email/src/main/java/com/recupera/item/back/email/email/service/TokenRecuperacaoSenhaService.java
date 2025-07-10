package com.recupera.item.back.email.email.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;
import java.util.UUID;
import java.time.LocalDateTime;
import jakarta.persistence.EntityNotFoundException;
import com.commodto.common.dto.RedefinirSenhaDto;

import com.recupera.item.back.email.email.model.TokenRecuperacaoSenha;
import com.recupera.item.back.email.email.repository.ITokenRecuperacaoSenha;
import com.commodto.common.dto.UsuarioDTO;
import com.recupera.item.back.email.email.producer.EmailProducer;


@Service
public class TokenRecuperacaoSenhaService {


    @Autowired
    private ITokenRecuperacaoSenha tokenRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private CorpoEmailService corpoEmailService;
    @Autowired
    private EmailProducer kafkaProducerService;

    public TokenRecuperacaoSenhaService(ITokenRecuperacaoSenha tokenRepository, EmailService emailService, CorpoEmailService corpoEmailService, EmailProducer kafkaProducerService) {
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.corpoEmailService = corpoEmailService;
        this.kafkaProducerService = kafkaProducerService;
    }

    public CompletableFuture<Void> solicitarRecuperacao(UsuarioDTO usuarioDTO) {

        String token = UUID.randomUUID().toString();
        LocalDateTime expiracao = LocalDateTime.now().plusHours(1);

        TokenRecuperacaoSenha tokenSenha = new TokenRecuperacaoSenha();
        tokenSenha.setToken(token);
        tokenSenha.setUsuarioId(usuarioDTO.id());
        tokenSenha.setDataExpiracao(expiracao);

        tokenRepository.save(tokenSenha);

        String link = "http://localhost:8080/recuperar?token=" + token;
        String corpo = corpoEmailService.gerarCorpoEmailRecuperacao(link);

        emailService.enviarEmailRecuperacao(usuarioDTO.email(), "Recuperação de senha", corpo);
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture<Void> RedefinirSenha(RedefinirSenhaDto redefinirSenhaDto) {
        TokenRecuperacaoSenha tokenSenha = tokenRepository.findByUsuarioId(redefinirSenhaDto.usuarioId())
            .orElseThrow(() -> new EntityNotFoundException("Token inválido"));
        
        if (tokenSenha.getDataExpiracao().isBefore(LocalDateTime.now())) {
            throw new EntityNotFoundException("Token expirado");
        }   

        kafkaProducerService.senhaNova(redefinirSenhaDto.usuarioId(), redefinirSenhaDto.novaSenha());

        tokenRepository.delete(tokenSenha);
        return CompletableFuture.completedFuture(null);
    }

}
