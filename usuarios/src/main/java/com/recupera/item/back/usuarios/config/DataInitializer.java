package com.recupera.item.back.usuarios.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.recupera.item.back.usuarios.dto.usuario.DTOCreatedUsuario;
import com.recupera.item.back.usuarios.enums.Perfis;
import com.recupera.item.back.usuarios.service.UsuarioService;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private Environment environment;

    @Override
    public void run(String... args) throws Exception {
        // Não executar durante os testes
        String[] activeProfiles = environment.getActiveProfiles();
        for (String profile : activeProfiles) {
            if ("test".equals(profile)) {
                return;
            }
        }
        
        if (usuarioService.listarUsuarios().isEmpty()) {
            // Assuming DTOCreatedUsuario has similar fields and a suitable constructor or setters
            DTOCreatedUsuario adminDto = new DTOCreatedUsuario(
                "admin",
                "carlosaugustocarneiro@alu.ufc.br",
                "Admin1234!",
                Perfis.Administrador
            );

            var usuario = usuarioService.createUsuario(adminDto);

            System.out.println("Administrador iniciado com sucesso! ID: " + usuario.getId());
        } else {
            System.out.println("Administrador já existe.");
        }
    }
}
