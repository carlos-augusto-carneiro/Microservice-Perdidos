package com.recupera.item.back.usuarios.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import com.recupera.item.back.usuarios.dto.usuario.DTOCreatedUsuario;
import com.recupera.item.back.usuarios.dto.usuario.DTOLoginRequest;
import com.recupera.item.back.usuarios.dto.usuario.DTOLoginResponse;
import com.recupera.item.back.usuarios.dto.usuario.DTOUpgradeUsuario;
import com.recupera.item.back.usuarios.enums.Perfis;
import com.recupera.item.back.usuarios.model.usuario.Usuario;
import com.recupera.item.back.usuarios.service.UsuarioService;

class UsuarioControllerTest {

    private UsuarioService usuarioService;
    private PasswordEncoder passwordEncoder;
    private JwtEncoder jwtEncoder;
    private UsuarioController usuarioController;

    @BeforeEach
    void setUp() {
        usuarioService = mock(UsuarioService.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtEncoder = mock(JwtEncoder.class);
        usuarioController = new UsuarioController(usuarioService, passwordEncoder, jwtEncoder);
    }

    @Test
    void loginUser_shouldReturnJwt_whenCredentialsAreCorrect() {
        // Arrange
        String email = "test@example.com";
        String password = "password";
        DTOLoginRequest loginRequest = new DTOLoginRequest(email, password);

        Usuario user = mock(Usuario.class);
        when(usuarioService.buscarUsuarioPorEmail(email)).thenReturn(user);
        when(user.LoginCorrect(eq(loginRequest), eq(passwordEncoder))).thenReturn(true);
        when(user.getId()).thenReturn(1L);
        when(user.getPerfil()).thenReturn(Perfis.Aluno);
        when(user.getEmail()).thenReturn(email);

        var jwtTokenValue = "jwt-token";
        var jwtEncoderResult = mock(org.springframework.security.oauth2.jwt.Jwt.class);
        when(jwtEncoderResult.getTokenValue()).thenReturn(jwtTokenValue);
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwtEncoderResult);

        // Act
        ResponseEntity<DTOLoginResponse> response = usuarioController.loginUser(loginRequest);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(jwtTokenValue, response.getBody().acessString());
        assertEquals(300L, response.getBody().expiresIn());
    }

    @Test
    void createdUser_shouldReturnOk_whenUserCreatedSuccessfully() {
        // Arrange
        DTOCreatedUsuario dto = new DTOCreatedUsuario("Test User", "test@email.com", "Password@123", Perfis.Aluno);
        Usuario usuario = mock(Usuario.class);
        when(usuarioService.createUsuario(dto)).thenReturn(usuario);

        // Act
        ResponseEntity<Void> response = usuarioController.createdUser(dto);

        // Assert
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void createdUser_shouldReturnBadRequest_whenExceptionOccurs() {
        // Arrange
        DTOCreatedUsuario dto = new DTOCreatedUsuario("Test User", "test@email.com", "password", Perfis.Aluno);
        when(usuarioService.createUsuario(dto)).thenThrow(new RuntimeException("Erro ao criar usuário"));

        // Act
        ResponseEntity<Void> response = usuarioController.createdUser(dto);

        // Assert
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void upgradeUsuario_shouldReturnOk_whenUserUpdatedSuccessfully() {
        // Arrange
        String email = "test@example.com";
        DTOUpgradeUsuario dto = new DTOUpgradeUsuario("Novo Nome", email, "NovaSenha@123");
        Usuario usuario = mock(Usuario.class);
        
        // Mock Security Context
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);
        SecurityContextHolder.setContext(securityContext);
        
        when(usuarioService.atualizarUsuario(email, dto)).thenReturn(usuario);

        // Act
        ResponseEntity<Void> response = usuarioController.upgradeUsuario(dto);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        
        // Cleanup
        SecurityContextHolder.clearContext();
    }

    @Test
    void upgradeUsuario_shouldReturnBadRequest_whenExceptionOccurs() {
        // Arrange
        String email = "test@example.com";
        DTOUpgradeUsuario dto = new DTOUpgradeUsuario("Novo Nome", "test@example.com", "NovaSenha@123");
        
        // Mock Security Context
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);
        SecurityContextHolder.setContext(securityContext);
        
        when(usuarioService.atualizarUsuario(email, dto)).thenThrow(new RuntimeException("Erro ao atualizar usuário"));

        // Act
        ResponseEntity<Void> response = usuarioController.upgradeUsuario(dto);

        // Assert
        assertEquals(400, response.getStatusCode().value());
        
        // Cleanup
        SecurityContextHolder.clearContext();
    }

    @Test
    void confirmarEmail_shouldReturnOk_whenCalled() {
        // Arrange
        String token = "any-token";

        // Act
        ResponseEntity<String> response = usuarioController.confirmarEmail(token);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Email confirmado com sucesso!", response.getBody());
    }

    @Test
    void esqueciSenha_shouldReturnOk_whenCalled() {
        // Arrange
        String email = "test@example.com";

        // Act
        ResponseEntity<Void> response = usuarioController.esqueciSenha(email);

        // Assert
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void redefinirSenha_shouldReturnOk_whenCalled() {
        // Arrange
        String token = "token";
        String novaSenha = "NovaSenha@123";

        // Act
        ResponseEntity<Void> response = usuarioController.redefinirSenha(token, novaSenha);

        // Assert
        assertEquals(200, response.getStatusCode().value());
    }
   
}