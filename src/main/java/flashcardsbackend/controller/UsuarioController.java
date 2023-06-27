package flashcardsbackend.controller;

import flashcardsbackend.domain.usuario.DadosUsuarioDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UsuarioController {

    @PostMapping("/usuario")
    public ResponseEntity criarNovoUsuario(@RequestBody DadosUsuarioDTO dados){

        return null;
    }
}
