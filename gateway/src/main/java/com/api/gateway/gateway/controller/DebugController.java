package com.api.gateway.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/debug")
@Tag(name = "Debug", description = "Endpoints de debug para verificar configuração")
public class DebugController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/services")
    @Operation(
        summary = "Listar Serviços Descobertos", 
        description = "Lista todos os serviços registrados no Eureka"
    )
    public List<String> getDiscoveredServices() {
        return discoveryClient.getServices();
    }

    @GetMapping("/gateway-info")
    @Operation(
        summary = "Informações do Gateway", 
        description = "Retorna informações de configuração do Gateway"
    )
    public String getGatewayInfo() {
        return "Gateway está funcionando! Serviços descobertos: " + discoveryClient.getServices().size();
    }
}
