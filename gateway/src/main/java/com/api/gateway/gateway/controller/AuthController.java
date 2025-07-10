package com.api.gateway.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoints de autenticação")
public class AuthController {

    @GetMapping("/login-info")
    @Operation(
        summary = "Informações de Login", 
        description = "Retorna informações sobre como fazer login e obter o token JWT"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Informações retornadas com sucesso")
    })
    public String loginInfo() {
        return "Como fazer login e obter o token JWT:\n\n" +
               "1. Faça uma requisição POST para: /api/usuarios/login\n" +
               "2. Envie o JSON:\n" +
               "   {\n" +
               "     \"email\": \"seu@email.com\",\n" +
               "     \"password\": \"sua_senha\"\n" +
               "   }\n\n" +
               "3. Você receberá uma resposta como:\n" +
               "   {\n" +
               "     \"acessString\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\",\n" +
               "     \"expiresIn\": 300\n" +
               "   }\n\n" +
               "4. Use o valor de 'acessString' como Bearer Token no Swagger UI\n" +
               "   Formato: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\n\n" +
               "5. Clique no botão 'Authorize' no Swagger UI e insira o token";
    }

    @GetMapping("/public/status")
    @Operation(
        summary = "Status Público", 
        description = "Endpoint público para verificar se o gateway está funcionando"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Gateway funcionando")
    })
    public String publicStatus() {
        return "API Gateway está funcionando! Este endpoint é público e não requer autenticação.";
    }
} 