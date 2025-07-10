package com.recupera.item.back.email.email.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.commodto.common.dto.UsuarioDTO;
import com.recupera.item.back.email.email.service.EmailConfirmacaoTokenService;
import com.recupera.item.back.email.email.service.TokenRecuperacaoSenhaService;

@Component
public class EmailConsumer {

    @Autowired
    private TokenRecuperacaoSenhaService tokenRecuperacaoSenhaService;
    @Autowired
    private final EmailConfirmacaoTokenService emailConfirmacaoTokenService;

    public EmailConsumer(EmailConfirmacaoTokenService emailConfirmacaoTokenService, TokenRecuperacaoSenhaService tokenRecuperacaoSenhaService) {
        this.emailConfirmacaoTokenService = emailConfirmacaoTokenService;
        this.tokenRecuperacaoSenhaService = tokenRecuperacaoSenhaService;
    }

    @KafkaListener(topics = "usuario-criado-topic", groupId = "email_group")
    public void consumirUsuarioCriado(UsuarioDTO usuarioDTO) {

        System.out.println("Recebido do Kafka: " + usuarioDTO);
        emailConfirmacaoTokenService.enviarConfirmacaoEmail(usuarioDTO);
    }

    @KafkaListener(topics = "email-reenvio-confirmacao-topic", groupId = "email_group")
    public void consumirEmailReenvioConfirmacao(UsuarioDTO usuarioDTO) {
        System.out.println("Recebido do Kafka: " + usuarioDTO);
        emailConfirmacaoTokenService.enviarConfirmacaoEmail(usuarioDTO);
    }

    @KafkaListener(topics = "email-recuperacao-topic", groupId = "email_group")
    public void consumirEmailRecuperacao(UsuarioDTO usuarioDTO) {
        System.out.println("Recebido do Kafka: " + usuarioDTO);
        tokenRecuperacaoSenhaService.solicitarRecuperacao(usuarioDTO);
    }
}
