package com.recupera.item.back.itens.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.oauth2.jwt.Jwt;

import com.recupera.item.back.itens.item.dto.CriarItemDto;
import com.recupera.item.back.itens.item.service.ItemService;
import com.recupera.item.back.itens.item.model.Item;
import com.commodto.common.dto.UsuarioLogadoDto;


@RestController
@RequestMapping("/itens")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PROFESSOR') or hasAuthority('GUARDA')")
    @PostMapping("/adicionar")
    public ResponseEntity<Item> adicionarItem(@RequestBody CriarItemDto itemDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof Jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Jwt jwt = (Jwt) authentication.getPrincipal();
        long usuarioId = Long.parseLong(jwt.getSubject()); 
        String perfil = jwt.getClaimAsStringList("authorities").get(0);

        UsuarioLogadoDto usuarioLogadoDto = new UsuarioLogadoDto(usuarioId, perfil);

        // Verifique o usuário logado (irá lançar exceção se inválido)
        itemService.processarUsuarioLogado(usuarioLogadoDto);

        Item novoItem = itemService.adicionarItem(itemDto, usuarioId, perfil);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoItem);
    }

}
