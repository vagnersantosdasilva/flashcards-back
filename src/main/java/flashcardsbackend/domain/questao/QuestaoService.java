package flashcardsbackend.domain.questao;

import flashcardsbackend.domain.categoria.Categoria;
import flashcardsbackend.domain.categoria.CategoriaRepository;
import flashcardsbackend.domain.categoria.DadosCategoria;
import flashcardsbackend.domain.usuario.UsuarioRepository;
import flashcardsbackend.infra.exceptions.ResourceNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class QuestaoService {

    @Autowired
    QuestaoRepository questaoRepository;
    @Autowired
    CategoriaRepository categoriaRepository;

    @Autowired
    UsuarioRepository usuarioRepository;
    @Transactional
    public DadosQuestao criar(DadosQuestao dto, UUID idUsuario){
        Categoria categoria = obterCategoriaValidada(dto.categoriaId(),idUsuario);
        Questao questao = new Questao();
        questao.setAcerto(dto.acerto());
        questao.setCategoria(categoria);
        questao.setPergunta(dto.pergunta());
        questao.setResposta(dto.resposta());
        questao.setDataCriacao(LocalDateTime.now());
        questao.setEtapa(Etapa.ETAPA0);
        return new DadosQuestao(questaoRepository.save(questao));
    }

    public DadosQuestao atualizar(DadosQuestaoUpdate dto, UUID idUsuario) {
        Categoria categoria = obterCategoriaValidada(dto.categoriaId(),idUsuario);
        Optional<Questao> questaoOpt =  questaoRepository.findById(dto.id());
        if (questaoOpt.isEmpty()) throw new ResourceNotFound("Questão não encontrada!");
        Questao questao = new Questao();
        questao.setId(dto.id());
        questao.setCategoria(categoria);
        questao.setPergunta(dto.pergunta());
        questao.setResposta(dto.resposta());
        return new DadosQuestao(questaoRepository.save(questao));
    }

    public void responderPergunta(DadosQuestaoResposta dto,UUID idUsuario){
        Categoria categoria = obterCategoriaValidada(dto.categoriaId(),idUsuario);
        Optional<Questao> questaoOpt =  questaoRepository.findById(dto.id());
        if (questaoOpt.isEmpty()) throw new ResourceNotFound("Questão não encontrada!");
        Questao questao = questaoOpt.get();
        questao.setAcerto(dto.acerto());
        Etapa etapa = this.verificarEtapa(questao);
        questao.setEtapa(etapa);
        questaoRepository.save(questao);
    }

    private Etapa verificarEtapa(Questao questao) {
        if (questao.getAcerto()) {
            Etapa etapaAtual = questao.getEtapa();
            if (etapaAtual == Etapa.ETAPA6) {
                return Etapa.ETAPA6;
            } else {
                int proximaEtapaOrdinal = etapaAtual.ordinal() + 1;
                return Etapa.values()[proximaEtapaOrdinal];
            }
        }
        return Etapa.ETAPA1;

    }

    public DadosQuestao obterQuestaoPorId( Long idQuestao,  Long idCategoria, UUID idUsuario) {
        Questao questao = questaoRepository.findByIdAndCategoriaIdAndCategoriaUsuarioId(idQuestao,idCategoria,idUsuario);
        if (questao!=null) return new DadosQuestao(questao);
        throw new ResourceNotFound("Questão não encontrada!");
    }

    public List<DadosQuestao> obterQuestaoPorCategoria(Long idCategoria,UUID idUsuario){
        return questaoRepository.findAllByCategoriaIdAndCategoriaUsuarioId(idCategoria,idUsuario)
                .stream()
                .map(e->new DadosQuestao(e)).collect(Collectors.toList());
    }

    private Categoria obterCategoriaValidada(Long categoriaId, UUID idUsuario){
        Optional<Categoria> categoriaOpt = categoriaRepository.findById(categoriaId);
        if (categoriaOpt.isEmpty())throw new ResourceNotFound("Categoria não encontrada!");
        if (!categoriaOpt.get().getUsuario().getId().equals(idUsuario)) throw new ResourceNotFound("Usuário incorreto!");
        return categoriaOpt.get();
    }


    public void remover(Long idQuestao, Long idCategoria, UUID idUsuario) {
        Questao questao = questaoRepository.findByIdAndCategoriaIdAndCategoriaUsuarioId(idQuestao,idCategoria,idUsuario);
        if (questao==null) throw new ResourceNotFound("Questão não encontrada!");
        questaoRepository.delete(questao);
    }
}
