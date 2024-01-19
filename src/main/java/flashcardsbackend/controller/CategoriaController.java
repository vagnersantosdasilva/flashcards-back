package flashcardsbackend.controller;

import flashcardsbackend.domain.categoria.CategoriaService;
import flashcardsbackend.domain.categoria.DadosCategoria;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class CategoriaController {
    @Autowired
    CategoriaService categoriaService;
    @PostMapping("categoria")
    @PreAuthorize("#dados.usuarioId.toString().equals(authentication.principal.get().id.toString())")
    public ResponseEntity criarCategoria(
                                         @RequestBody @Valid DadosCategoria dados,
                                         UriComponentsBuilder uriComponentsBuilder){
        DadosCategoria dadosResponse = categoriaService.criar(dados);
        var uri = uriComponentsBuilder.path("usuario/categoria/{id}").buildAndExpand(dadosResponse.id()).toUri();
        return ResponseEntity.created(uri).body(dadosResponse);
    }

    @PutMapping("categoria")
    @PreAuthorize("#dados.usuarioId.toString().equals(authentication.principal.get().id.toString())")
    public ResponseEntity<?> atualizaCategoria(@RequestBody @Valid DadosCategoria dados){

        DadosCategoria response = categoriaService.atualizar(dados);
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("#idUsuario.toString().equals(authentication.principal.get().id.toString())")
    @DeleteMapping("usuario/{idUsuario}/categoria/{id}")
    public ResponseEntity<?> excluirCategoria(
            @PathVariable("id") Long id ,
            @PathVariable("idUsuario") UUID idUsuario){

        categoriaService.removerCategoria(id,idUsuario);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("#idUsuario.toString().equals(authentication.principal.get().id.toString())")
    @GetMapping("/usuario/{idUsuario}/categoria")
    public ResponseEntity listarCategorias(@PathVariable UUID idUsuario){
        List<DadosCategoria> categorias = categoriaService.listarCategorias(idUsuario);
        return ResponseEntity.ok().body(categorias);
    }

    @PreAuthorize("#idUsuario.toString().equals(authentication.principal.get().id.toString())")
    @GetMapping("/usuario/{idUsuario}/categoria/{idCategoria}")
    public ResponseEntity listarCategorias(@PathVariable UUID idUsuario,
                                           @PathVariable Long idCategoria){
        DadosCategoria categoriaResponse = categoriaService.obterCategoria(idCategoria);
        return ResponseEntity.ok().body(categoriaResponse);
    }
    @PreAuthorize("#idUsuario.toString().equals(authentication.principal.get().id.toString())")
    @GetMapping("/usuario/{idUsuario}/categoria/etapas")
    public ResponseEntity listarContagemEtapasPorCategoria(@PathVariable UUID idUsuario ){
        return ResponseEntity.ok().body(categoriaService.obterDashboard(idUsuario));
    }



}
