package flashcardsbackend.controller;

import flashcardsbackend.domain.questao.dto.DadosQuestao;
import flashcardsbackend.domain.questao.dto.DadosQuestaoResposta;
import flashcardsbackend.domain.questao.dto.DadosQuestaoUpdate;
import flashcardsbackend.domain.questao.QuestaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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


    @PostMapping("usuario/{idUsuario}/categoria/{idCategoria}/questao/texto")
    @PreAuthorize("#idUsuario.toString().equals(authentication.principal.get().id.toString())")
    public List<DadosQuestao> uploadFile(
            @RequestParam MultipartFile arquivo,
            @RequestParam String lingua,
            @PathVariable("idCategoria") Long idCategoria ,
            @PathVariable("idUsuario") UUID idUsuario,
            @RequestParam("paginas") String paginas
    ) throws IOException {
        return (service.criarQuestoes(idUsuario, idCategoria, lingua, arquivo, paginas));
    }

    @PutMapping("usuario/{idUsuario}/questao")
    @PreAuthorize("#dto.usuarioId.toString().equals(authentication.principal.get().id.toString())")
    public ResponseEntity atualizarQuestao(
            @PathVariable("idUsuario") UUID idUsuario,
            @RequestBody @Valid DadosQuestaoUpdate dto){

        DadosQuestao questaoResponse =  service.atualizar(dto,idUsuario);
        return ResponseEntity.ok().body(questaoResponse);
    }

    @PutMapping("usuario/{idUsuario}/questao/responder")
    @PreAuthorize("#dto.usuarioId.toString().equals(authentication.principal.get().id.toString())")
    public ResponseEntity responderQuestao(
            @PathVariable("idUsuario") UUID idUsuario,
            @RequestBody @Valid DadosQuestaoResposta dto){

        service.responderPergunta(dto,idUsuario);
        return ResponseEntity.noContent().build();
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
    @GetMapping("usuario/{idUsuario}/categoria/{idCategoria}/questao/countnew")
    @PreAuthorize("#idUsuario.toString().equals(authentication.principal.get().id.toString())")
    public ResponseEntity obterContagemNovasQuestoes(
            @PathVariable("idCategoria") Long idCategoria ,
            @PathVariable("idUsuario") UUID idUsuario){
        return ResponseEntity.ok(service.obterContagemNovaQuestao(idCategoria, idUsuario));
    }

    @GetMapping("usuario/{idUsuario}/categoria/{idCategoria}/questao/revisao")
    @PreAuthorize("#idUsuario.toString().equals(authentication.principal.get().id.toString())")
    public ResponseEntity obterListaQuestaoRevisao(
            @PathVariable("idCategoria") Long idCategoria ,
            @PathVariable("idUsuario") UUID idUsuario){
        return ResponseEntity.ok(service.obterQuestaoParaRevisao(idCategoria,idUsuario));
    }

    @GetMapping("usuario/{idUsuario}/categoria/{idCategoria}/questao/datas")
    @PreAuthorize("#idUsuario.toString().equals(authentication.principal.get().id.toString())")
    public ResponseEntity obterDatasDeNovasRevisoes(
            @PathVariable("idUsuario")UUID idUsuario,
            @PathVariable("idCategoria") Long idCategoria
    ){
        return ResponseEntity.ok(service.obterDataRevisaoPorCategoria(idCategoria));
    }

    @GetMapping("usuario/{idUsuario}/categoria/{idCategoria}/download")
    public ResponseEntity donwloadCategoria(
            @PathVariable("idUsuario") UUID idUsuario,
            @PathVariable("idCategoria") Long idCategoria){
        List<DadosQuestao> listaQuestao =  service.obterQuestaoPorCategoria(idCategoria,idUsuario);
        return ResponseEntity.ok().body(listaQuestao);
    }

}
