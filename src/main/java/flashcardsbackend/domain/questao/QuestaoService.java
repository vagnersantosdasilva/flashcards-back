package flashcardsbackend.domain.questao;

import flashcardsbackend.domain.categoria.Categoria;
import flashcardsbackend.domain.categoria.CategoriaRepository;
import flashcardsbackend.domain.categoria.DadosCategoria;
import flashcardsbackend.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return new DadosQuestao(questaoRepository.save(questao));
    }


    public DadosQuestao atualizar(DadosQuestaoUpdate dto, UUID idUsuario) {
        Categoria categoria = obterCategoriaValidada(dto.categoriaId(),idUsuario);
        Optional<Questao> questaoOpt =  questaoRepository.findById(dto.id());
        if (questaoOpt.isEmpty()) throw new RuntimeException("Questão não encontrada!");
        Questao questao = new Questao();
        questao.setAcerto(dto.acerto());
        questao.setId(dto.id());
        questao.setCategoria(categoria);
        questao.setPergunta(dto.pergunta());
        questao.setResposta(dto.resposta());
        return new DadosQuestao(questaoRepository.save(questao));
    }

    public DadosQuestao obterQuestaoPorId( Long idQuestao,  Long idCategoria, UUID idUsuario) {
        Questao questao = questaoRepository.findByIdAndCategoriaIdAndCategoriaUsuarioId(idQuestao,idCategoria,idUsuario);
        if (questao!=null) return new DadosQuestao(questao);
        throw new RuntimeException("Questão não encontrada!");
    }

    public List<DadosQuestao> obterQuestaoPorCategoria(Long idCategoria,UUID idUsuario){
        return questaoRepository.findAllByCategoriaIdAndCategoriaUsuarioId(idCategoria,idUsuario)
                .stream()
                .map(e->new DadosQuestao(e)).collect(Collectors.toList());
    }

    private Categoria obterCategoriaValidada(Long categoriaId, UUID idUsuario){
        Optional<Categoria> categoriaOpt = categoriaRepository.findById(categoriaId);
        if (categoriaOpt.isEmpty())throw new RuntimeException("Categoria não encontrada!");
        if (!categoriaOpt.get().getUsuario().getId().equals(idUsuario)) throw new RuntimeException("Usuário incorreto!");
        return categoriaOpt.get();
    }


    public void remover(Long idQuestao, Long idCategoria, UUID idUsuario) {
        Questao questao = questaoRepository.findByIdAndCategoriaIdAndCategoriaUsuarioId(idQuestao,idCategoria,idUsuario);
        if (questao==null) throw new RuntimeException("Questão não encontrada!");
        questaoRepository.delete(questao);
    }
}
