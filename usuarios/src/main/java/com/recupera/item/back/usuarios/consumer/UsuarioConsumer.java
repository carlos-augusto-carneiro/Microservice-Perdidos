package com.recupera.item.back.usuarios.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.commodto.common.dto.RedefinirSenhaDto;
import com.recupera.item.back.usuarios.service.UsuarioService;

@Component
public class UsuarioConsumer {

    @Autowired
    private UsuarioService usuarioService;

    @KafkaListener(topics = "senha-nova-topic", groupId = "usuarios_group")
    public void redefinirSenha(RedefinirSenhaDto redefinirSenhaDto) {
        usuarioService.redefinirSenha(redefinirSenhaDto.usuarioId(), redefinirSenhaDto.novaSenha());
    }

}
