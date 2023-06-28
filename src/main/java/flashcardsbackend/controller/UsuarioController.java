package flashcardsbackend.controller;

import flashcardsbackend.domain.usuario.DadosUsuarioDTO;
import flashcardsbackend.domain.usuario.DadosUsuarioResponseDTO;
import flashcardsbackend.domain.usuario.Usuario;
import flashcardsbackend.domain.usuario.UsuarioService;
import flashcardsbackend.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    AuthenticationManager manager;

    @Autowired
    TokenService tokenService;

    @Autowired
    PasswordEncoder encoder;

    @PostMapping("public/usuario")
    public ResponseEntity criarNovoUsuario(@RequestBody @Valid DadosUsuarioDTO dados , UriComponentsBuilder uriBuilder){
        DadosUsuarioResponseDTO dadosResponse = usuarioService.criarUsuario(dados);
        var uri = uriBuilder.path("/usuario/{id}").buildAndExpand(dadosResponse.id()).toUri();
        return ResponseEntity.created(uri).body(dadosResponse);
    }

    @PostMapping("public/login")
    public ResponseEntity login(@RequestBody @Valid DadosUsuarioDTO dados){
        DadosUsuarioResponseDTO dadosResponse =null;
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.username(),dados.password());
        var authentication = manager.authenticate(authenticationToken);
        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());

        return ResponseEntity.ok(tokenJWT);
    }

    //TODO: Provisoriamente até implementar token jwt
    @PostMapping("registrar")
    public ResponseEntity registro(@RequestBody @Valid DadosUsuarioDTO dados){
        DadosUsuarioResponseDTO dadosResponse = usuarioService.validarCriacao(dados);
        return ResponseEntity.ok(dadosResponse);
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity obterUmUsuario(@PathVariable UUID id){
        DadosUsuarioResponseDTO dadosResponse = usuarioService.findUsuarioById(id);
        return ResponseEntity.ok(dadosResponse);
    }



}
