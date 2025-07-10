package com.recupera.item.back.email.email.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commodto.common.dto.RedefinirSenhaDto;
import org.springframework.http.ResponseEntity;
import com.recupera.item.back.email.email.service.TokenRecuperacaoSenhaService;

@RestController
@RequestMapping("/api/email/recuperar-senha")
public class RecuperarSenhaController {

    @Autowired
    private TokenRecuperacaoSenhaService tokenRecuperacaoSenhaService;

    public RecuperarSenhaController(TokenRecuperacaoSenhaService tokenRecuperacaoSenhaService) {
        this.tokenRecuperacaoSenhaService = tokenRecuperacaoSenhaService;
    }

    @PostMapping("/solicitar")
    public ResponseEntity<Void> solicitarRecuperacao(@RequestBody RedefinirSenhaDto redefinirSenhaDto) {
        tokenRecuperacaoSenhaService.RedefinirSenha(redefinirSenhaDto);
        return ResponseEntity.ok().build();
    }

}
