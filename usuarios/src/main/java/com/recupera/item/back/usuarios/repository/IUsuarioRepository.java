package com.recupera.item.back.usuarios.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.recupera.item.back.usuarios.enums.Perfis;
import com.recupera.item.back.usuarios.model.usuario.Usuario;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Long>{

    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Usuario> findAllByPerfil(Perfis perfil);
    void deleteByEmail(String email);
}
