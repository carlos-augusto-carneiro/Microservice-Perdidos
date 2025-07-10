package com.recupera.item.back.usuarios.config;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;

import com.commodto.common.dto.UsuarioDTO;
import com.recupera.item.back.usuarios.consumer.UsuarioConsumer;

@TestConfiguration
@Profile("test")
public class TestConfig {

    @Bean
    @Primary
    @SuppressWarnings("unchecked")
    public KafkaTemplate<String, UsuarioDTO> kafkaTemplate() {
        return Mockito.mock(KafkaTemplate.class);
    }

    @Bean
    @Primary
    public UsuarioConsumer usuarioConsumer() {
        return Mockito.mock(UsuarioConsumer.class);
    }
}
