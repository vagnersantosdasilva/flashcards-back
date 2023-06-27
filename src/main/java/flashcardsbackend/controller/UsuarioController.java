package flashcardsbackend.controller;

import flashcardsbackend.domain.usuario.DadosUsuarioDTO;
import flashcardsbackend.domain.usuario.DadosUsuarioResponseDTO;
import flashcardsbackend.domain.usuario.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;
    @PostMapping("/usuario")
    public ResponseEntity criarNovoUsuario(@RequestBody @Valid DadosUsuarioDTO dados , UriComponentsBuilder uriBuilder){
        DadosUsuarioResponseDTO dadosResponse = usuarioService.criarUsuario(dados);
        var uri = uriBuilder.path("/usuario/{id}").buildAndExpand(dadosResponse.id()).toUri();
        return ResponseEntity.created(uri).body(dadosResponse);
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity obterUmUsuario(@PathVariable UUID id){
        DadosUsuarioResponseDTO dadosResponse = usuarioService.findUsuarioById(id);
        return ResponseEntity.ok(dadosResponse);
    }

}
