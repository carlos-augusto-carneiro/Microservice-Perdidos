package com.recupera.item.back.itens.item.service;

import java.util.List;

import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.recupera.item.back.itens.item.model.Item;
import com.recupera.item.back.itens.item.repository.ItemRepository;
import com.commodto.common.dto.UsuarioLogadoDto;
import org.springframework.cache.annotation.Cacheable;


import com.recupera.item.back.itens.item.dto.CriarItemDto;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public List<Item> listarItensPorUsuario(long usuarioid) {
        return itemRepository.findByUsuarioid(usuarioid);
    }

    public List<Item> listarItensNaoDevolvidos() {
        return itemRepository.findByDevolvido(false);
    }

    public List<Item> listarItensDevolvidos() {
        return itemRepository.findByDevolvido(true);
    }

    public Item adicionarItem(CriarItemDto itemDto, long usuarioIdLogado, String perfil) {
        if (itemDto == null || itemDto.nome() == null || itemDto.nome().isEmpty()) {
            throw new IllegalArgumentException("Dados do item inválidos");
        }
        if (perfil == null || perfil.isEmpty() || perfil.equals("Aluno")) {
            throw new IllegalArgumentException("Perfil do usuário inválido");
        }
        if (usuarioIdLogado == 0) {
            throw new RuntimeException("Usuário não está logado ou não encontrado no cache");
        }

        Item novoItem = new Item(usuarioIdLogado, itemDto.nome(), itemDto.descricao());

        return itemRepository.save(novoItem);
    }

    @Cacheable(value = "usuariosLogados", key = "#usuarioLogadoDto.usuarioId()")
    public void processarUsuarioLogado(UsuarioLogadoDto usuarioLogadoDto) {
        if (usuarioLogadoDto == null || usuarioLogadoDto.usuarioId() <= 0) {
            throw new IllegalArgumentException("Dados do usuário logado inválidos");
        }
    }
}
