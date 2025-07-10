package com.recupera.item.back.email.email.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.commodto.common.dto.UsuarioDTO;
import com.recupera.item.back.email.email.model.EmailConfirmacaoToken;
import com.recupera.item.back.email.email.repository.IEmailConfirmacaoTokenRepository;
    

@Service
public class EmailConfirmacaoTokenService {

    @Autowired
    private final IEmailConfirmacaoTokenRepository emailConfirmacaoTokenRepository;
    @Autowired
    private final EmailService emailService;
    @Autowired
    private final CorpoEmailService corpoEmailService;

    public EmailConfirmacaoTokenService(IEmailConfirmacaoTokenRepository emailConfirmacaoTokenRepository, EmailService emailService, CorpoEmailService corpoEmailService) {
        this.emailConfirmacaoTokenRepository = emailConfirmacaoTokenRepository;
        this.emailService = emailService;
        this.corpoEmailService = corpoEmailService;
    }

    
    public void enviarConfirmacaoEmail(UsuarioDTO usuario) {
        System.out.println("[EmailConfirmacaoTokenService] Iniciando envio de confirmação para usuário: " + usuario);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo.");
        }
        long usuarioId = usuario.id();
        var token = EmailConfirmacaoToken.criarPara(usuarioId);
        
        emailConfirmacaoTokenRepository.findByUsuarioId(usuarioId)
            .ifPresent(existingToken -> {
                System.out.println("[EmailConfirmacaoTokenService] Atualizando token existente para usuário: " + usuarioId);
                existingToken.setToken(token.getToken());
                existingToken.setDataExpiracao(token.getDataExpiracao());
                emailConfirmacaoTokenRepository.save(existingToken);
            });
            
        if (!emailConfirmacaoTokenRepository.existsByUsuarioId(usuarioId)) {
            System.out.println("[EmailConfirmacaoTokenService] Salvando novo token para usuário: " + usuarioId);
            emailConfirmacaoTokenRepository.save(token);
        }

        if (usuario.email() == null || usuario.email().isEmpty()) {
            throw new IllegalArgumentException("O email do usuário não pode ser nulo ou vazio.");
        }

        String link = "http://localhost:8080/confirmar?token=" + token.getToken();
        String corpoEmail = corpoEmailService.gerarCorpoEmailConfirmacao(link);
        System.out.println("[EmailConfirmacaoTokenService] Enviando email de confirmação para: " + usuario.email());
        emailService.enviarEmail(usuario.email(), "Confirme seu e-mail", corpoEmail);
    }

    public Optional<EmailConfirmacaoToken> findByToken(String token) {
        return emailConfirmacaoTokenRepository.findByToken(token);
    }

    public void delete(EmailConfirmacaoToken token) {
        emailConfirmacaoTokenRepository.delete(token);
    }
}
