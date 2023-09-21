package flashcardsbackend.controller;

import flashcardsbackend.domain.usuario.dto.DadosUsuarioCriacao;
import flashcardsbackend.domain.usuario.dto.DadosUsuarioLoginDTO;
import flashcardsbackend.domain.usuario.dto.DadosUsuarioResetPasswordDTO;
import flashcardsbackend.domain.usuario.dto.DadosUsuarioResponseDTO;
import flashcardsbackend.domain.usuario.Usuario;
import flashcardsbackend.domain.usuario.UsuarioService;
import flashcardsbackend.infra.security.DadosTokenResponse;
import flashcardsbackend.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
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



    @PostMapping("public/usuario")
    public ResponseEntity criarNovoUsuario(@RequestBody @Valid DadosUsuarioCriacao dados , UriComponentsBuilder uriBuilder){
        DadosUsuarioResponseDTO dadosResponse = usuarioService.criarUsuario(dados);
        var uri = uriBuilder.path("/usuario/{id}").buildAndExpand(dadosResponse.id()).toUri();
        return ResponseEntity.created(uri).body(dadosResponse);
    }

    @PostMapping("public/login")
    public ResponseEntity login(@RequestBody @Valid DadosUsuarioLoginDTO dados){
        DadosUsuarioResponseDTO dadosResponse =null;
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.username(),dados.password());
        var authentication = manager.authenticate(authenticationToken);
        var tokenJWT = "Bearer "+tokenService.gerarToken((Usuario) authentication.getPrincipal());

        return ResponseEntity.ok(new DadosTokenResponse(tokenJWT));
    }

    //TODO: Provisoriamente até implementar token jwt
    @GetMapping("/public/usuario")
    public ResponseEntity registro(@RequestParam("token") String token){
        System.out.println(token);
        String urlResponse = usuarioService.validarCriacao(token);
        return ResponseEntity.status(HttpStatus.SEE_OTHER).location(URI.create(urlResponse)).build();

    }

    @GetMapping("/public/reset/usuario")
    public ResponseEntity habilitarResetPassword(@RequestParam("email") String email){
        usuarioService.habilitarResetPassword(email);
        return ResponseEntity.noContent().build();

    }

    @PreAuthorize("#dadosDTO.idUsuario.toString().equals(authentication.principal.get().id.toString())")
    @PostMapping("/reset/usuario")
    public ResponseEntity resetPassword(@RequestBody @Valid DadosUsuarioResetPasswordDTO dadosDTO){
        usuarioService.resetPassword(dadosDTO);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity obterUmUsuario(@PathVariable UUID id){
        DadosUsuarioResponseDTO dadosResponse = usuarioService.findUsuarioById(id);
        return ResponseEntity.ok(dadosResponse);
    }

}
