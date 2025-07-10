package com.recupera.item.back.email.email.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.commodto.common.dto.RedefinirSenhaDto;

@Component
public class EmailProducer {

    @Autowired
    private KafkaTemplate<String, RedefinirSenhaDto> kafkaTemplate;

    public void senhaNova(Long usuarioId, String novaSenha) {
        RedefinirSenhaDto dto = new RedefinirSenhaDto(usuarioId, novaSenha);
        kafkaTemplate.send("senha-nova-topic", dto);
    }
}
