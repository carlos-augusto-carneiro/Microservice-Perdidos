package com.recupera.item.back.usuarios.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.commodto.common.dto.UsuarioLogadoDto;

@Service
public class UsuarioLogadoProducer {

    @Autowired
    private KafkaTemplate<String, UsuarioLogadoDto> kafkaTemplateLogin;

    public void publicarLogin(UsuarioLogadoDto usuarioLogadoDto) {
        kafkaTemplateLogin.send("usuario-logado-topic", usuarioLogadoDto);
    }
}
