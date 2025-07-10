package com.recupera.item.back.itens.item.repository;

import java.util.List;
import com.recupera.item.back.itens.item.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByUsuarioid(long usuarioid);

    List<Item> findByDevolvido(boolean devolvido);

}
