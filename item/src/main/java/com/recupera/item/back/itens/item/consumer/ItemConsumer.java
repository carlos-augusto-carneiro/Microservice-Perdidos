package com.recupera.item.back.itens.item.consumer;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import com.commodto.common.dto.UsuarioLogadoDto;
import com.recupera.item.back.itens.item.service.ItemService;

@Component
public class ItemConsumer {

    @Autowired
    private ItemService itemService;

    @KafkaListener(topics = "usuario-logado-topic", groupId = "item-group")
    public void consumirUsuarioLogado(UsuarioLogadoDto usuarioLogadoDto) {
        itemService.processarUsuarioLogado(usuarioLogadoDto);
    }
}
