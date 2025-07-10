package com.recupera.item.back.usuarios.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.commodto.common.dto.UsuarioDTO;
import com.recupera.item.back.usuarios.dto.usuario.DTOCreatedUsuario;
import com.recupera.item.back.usuarios.dto.usuario.DTOUpgradeUsuario;
import com.recupera.item.back.usuarios.enums.Perfis;
import com.recupera.item.back.usuarios.exception.usuario.UsuarioException;
import com.recupera.item.back.usuarios.model.usuario.Usuario;
import com.recupera.item.back.usuarios.producer.UsuarioProducer;
import com.recupera.item.back.usuarios.producer.UsuarioLogadoProducer;
import com.recupera.item.back.usuarios.repository.IUsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired  
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioProducer kafkaProducerService;

    @Autowired
    private UsuarioLogadoProducer kafkaProducerLogadoService;


    public Usuario createUsuario(DTOCreatedUsuario usuario) {
        if (usuario == null) {
            throw new UsuarioException("Dados do usuário não podem ser nulos");
        }
        Usuario usuarioConvert = Usuario.fromDTO(usuario);
        usuarioConvert.Validar();
        usuarioConvert.EmailValido(usuarioConvert.getEmail());   
        usuarioConvert.Senhaforte(usuarioConvert.getSenha());

        if(usuarioRepository.existsByEmail(usuarioConvert.getEmail())) {
            throw new UsuarioException("Email já cadastrado");
        }

        String senhaCodificada = passwordEncoder.encode(usuarioConvert.getSenha());
        usuarioConvert.setSenha(senhaCodificada);

        Usuario usuarioSalvo = usuarioRepository.save(usuarioConvert);
            var evento = new UsuarioDTO(
            usuarioSalvo.getId(),
            usuarioSalvo.getNome(),
            usuarioSalvo.getEmail(),
            usuarioSalvo.isEmailConfirmado(),
            usuarioSalvo.getPerfil().name()
        );
        kafkaProducerService.enviarUsuarioCriado(evento);

        return usuarioSalvo;
    }

    public Usuario ReenviarEmailConfirmacao(String email) {
        Usuario usuario = buscarUsuarioPorEmail(email);
        if(usuario.isEmailConfirmado()) {
            throw new UsuarioException("Email já confirmado");
        }
        usuario.setEmailConfirmado(false);
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        var evento = new UsuarioDTO(
        usuarioSalvo.getId(),
        usuarioSalvo.getNome(),
        usuarioSalvo.getEmail(),
        usuarioSalvo.isEmailConfirmado(),
        usuarioSalvo.getPerfil().name()
        );
        kafkaProducerService.ReenviarEmailConfirmacao(evento);
        return usuario;
    }

    public Usuario confirmarEmail(String email) {
        Usuario usuario = buscarUsuarioPorEmail(email);
        usuario.setEmailConfirmado(true);
        usuarioRepository.save(usuario);
        return usuario;
    }

    public Usuario solicitarRecuperacao(String email) {
        Usuario usuario = buscarUsuarioPorEmail(email);
        if(usuario == null) {
            throw new UsuarioException("Usuário não encontrado com o email: " + email);
        }
        var evento = new UsuarioDTO(
            usuario.getId(),
            usuario.getNome(),
            usuario.getEmail(),
            usuario.isEmailConfirmado(),
            usuario.getPerfil().name()
        );
        kafkaProducerService.RecuperarSenha(evento);
        return usuario;
    }

    public void publicarLogin(Long usuarioId, String nome, String perfil) {
        kafkaProducerLogadoService.publicarLogin(new com.commodto.common.dto.UsuarioLogadoDto(usuarioId, nome, perfil));
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }


    public Usuario buscarUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioException("Usuário não encontrado com o email: " + email));
    }


    public void deletarUsuarioPorEmail(String email) {
        if (!usuarioRepository.existsByEmail(email)) {
            throw new UsuarioException("Usuário não encontrado com o email: " + email);
        }
        usuarioRepository.deleteByEmail(email);
    }

    public Usuario promoverParaGuarda(String email) {
        Usuario usuario = buscarUsuarioPorEmail(email);
        usuario.promover(Perfis.Guarda);
        return usuarioRepository.save(usuario);
    }
    
    public Usuario promoverParaProfessor(String email) {
        Usuario usuario = buscarUsuarioPorEmail(email);
        usuario.promover(Perfis.Professor);
        return usuarioRepository.save(usuario);
    }

    public Usuario promoverParaAluno(String email) {
        Usuario usuario = buscarUsuarioPorEmail(email);
        usuario.promover(Perfis.Aluno);
        return usuarioRepository.save(usuario);
    }

    public Usuario promoverParaAdministrador(String email) {
        Usuario usuario = buscarUsuarioPorEmail(email);
        usuario.promover(Perfis.Administrador);
        return usuarioRepository.save(usuario);
    }

    public Usuario atualizarUsuario(String email, DTOUpgradeUsuario dto) {
        Usuario usuario = buscarUsuarioPorEmail(email);

        if (dto.nome() != null && !dto.nome().isEmpty()) {
            usuario.setNome(dto.nome());
        }
        if (dto.email() != null && !dto.email().isEmpty()) {
            if (usuarioRepository.existsByEmail(dto.email()) && !usuario.getEmail().equals(dto.email())) {
                throw new UsuarioException("Email já cadastrado");
            }
            usuario.setEmail(dto.email());
        }
        if (dto.senha() != null && !dto.senha().isEmpty()) {
            usuario.Senhaforte(dto.senha());
            String senhaCodificada = passwordEncoder.encode(dto.senha());
            usuario.setSenha(senhaCodificada);
        }

        usuario.Validar();
        usuarioRepository.save(usuario);
        return usuario;
    }

    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public void redefinirSenha(Long usuarioId, String novaSenha) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new UsuarioException("Usuário não encontrado"));
        usuario.setSenha(novaSenha);
        usuarioRepository.save(usuario);
    }

}

