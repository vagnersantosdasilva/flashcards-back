package flashcardsbackend.controller;

import flashcardsbackend.domain.questao.DadosQuestao;
import flashcardsbackend.domain.questao.DadosQuestaoUpdate;
import flashcardsbackend.domain.questao.QuestaoService;
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
public class QuestaoController {

    @Autowired
    QuestaoService service;

    @PostMapping("usuario/{idUsuario}/questao")
    @PreAuthorize("#dto.usuarioId.toString().equals(authentication.principal.get().id.toString())")
    public ResponseEntity criarQuestao(
            @PathVariable("idUsuario") UUID idUsuario,
            @RequestBody @Valid DadosQuestao dto,
            UriComponentsBuilder uriComponentsBuilder){

        DadosQuestao questaoResponse =  service.criar(dto,idUsuario);
        var uri = uriComponentsBuilder.path("usuario/{idUsuario}/categoria/{idCategoria}/questao/{idQuestao}")
                .buildAndExpand(idUsuario,questaoResponse.categoriaId(),questaoResponse.id()).toUri();
        return ResponseEntity.created(uri).body(questaoResponse);
    }

    @PutMapping("usuario/{idUsuario}/questao")
    @PreAuthorize("#dto.usuarioId.toString().equals(authentication.principal.get().id.toString())")
    public ResponseEntity atualizarQuestao(
            @PathVariable("idUsuario") UUID idUsuario,
            @RequestBody @Valid DadosQuestaoUpdate dto){

        DadosQuestao questaoResponse =  service.atualizar(dto,idUsuario);
        return ResponseEntity.ok().body(questaoResponse);
    }

    @GetMapping("usuario/{idUsuario}/categoria/{idCategoria}/questao/{idQuestao}")
    @PreAuthorize("#idUsuario.toString().equals(authentication.principal.get().id.toString())")
    public ResponseEntity obterQuestaoPorId(
            @PathVariable("idUsuario") UUID idUsuario,
            @PathVariable("idCategoria") Long idCategoria,
            @PathVariable("idQuestao") Long idQuestao){
        DadosQuestao questao = service.obterQuestaoPorId(idQuestao,idCategoria, idUsuario);
        return ResponseEntity.ok().body(questao);
    }

    @GetMapping("usuario/{idUsuario}/categoria/{idCategoria}/questao")
    @PreAuthorize("#idUsuario.toString().equals(authentication.principal.get().id.toString())")
    public ResponseEntity obterListaQuestao(
            @PathVariable("idUsuario") UUID idUsuario,
            @PathVariable("idCategoria") Long idCategoria){
        List<DadosQuestao> listaQuestao =  service.obterQuestaoPorCategoria(idCategoria,idUsuario);

        return ResponseEntity.ok().body(listaQuestao);
    }

    @DeleteMapping("usuario/{idUsuario}/categoria/{idCategoria}/questao/{idQuestao}")
    @PreAuthorize("#idUsuario.toString().equals(authentication.principal.get().id.toString())")
    public ResponseEntity removerQuestao(
            @PathVariable("idUsuario") UUID idUsuario,
            @PathVariable("idCategoria") Long idCategoria,
            @PathVariable("idQuestao") Long idQuestao){

        service.remover(idQuestao,idCategoria,idUsuario);
        return ResponseEntity.noContent().build();
    }
}
