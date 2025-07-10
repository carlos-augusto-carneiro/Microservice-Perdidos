package com.recupera.item.back.usuarios.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.commodto.common.dto.UsuarioDTO;

@Service
public class UsuarioProducer {

    @Autowired
    private KafkaTemplate<String, UsuarioDTO> kafkaTemplate;

    public UsuarioProducer(KafkaTemplate<String, UsuarioDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void enviarUsuarioCriado(UsuarioDTO usuarioDTO) {
        kafkaTemplate.send("usuario-criado-topic", usuarioDTO);
    }

    public void ReenviarEmailConfirmacao(UsuarioDTO usuarioDTO) {
        kafkaTemplate.send("email-reenvio-confirmacao-topic", usuarioDTO);
    }

    public void RecuperarSenha(UsuarioDTO usuarioDTO) {
        kafkaTemplate.send("email-recuperacao-topic", usuarioDTO);
    }
}
