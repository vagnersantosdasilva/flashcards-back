package flashcardsbackend.domain.questao;

import flashcardsbackend.domain.categoria.Categoria;
import flashcardsbackend.domain.categoria.CategoriaRepository;
import flashcardsbackend.domain.categoria.DadosCategoria;
import flashcardsbackend.domain.usuario.UsuarioRepository;
import flashcardsbackend.infra.exceptions.ResourceNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
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
        questao.setEtapa(Etapa.ETAPA0);
        questao.setAcerto(null);
        questao.setDataCriacao(questaoOpt.get().getDataCriacao());
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
        questao.setDataCriacao(LocalDateTime.now());
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

    public List<Questao> obterRevisaoPorCategoria(Long idCategoria){

        return null;
    }

    private Boolean habilitadaParaRevisao(Questao questao){
        LocalDateTime criacao = questao.getDataCriacao();
        Long diasEtapa  = (long) questao.getEtapa().getDuracaoDias();
        LocalDateTime proximaRevisao = criacao.plusDays(diasEtapa);

        LocalDate dataRevisao = proximaRevisao.toLocalDate();
        LocalDate.now();

        if ((dataRevisao.isBefore(LocalDate.now()) || dataRevisao.equals(LocalDate.now()))) return true;
        return false;
    }

    private Boolean novaQuestao(Questao questao){
        if (questao.getEtapa() ==null || questao.getEtapa().getDuracaoDias()<1) return true;
        return false;
    }

    public DadosQuestao obterQuestaoPorId( Long idQuestao,  Long idCategoria, UUID idUsuario) {
        Questao questao = questaoRepository.findByIdAndCategoriaIdAndCategoriaUsuarioId(idQuestao,idCategoria,idUsuario);

        if (questao!=null) return new DadosQuestao(questao);
        throw new ResourceNotFound("Questão não encontrada!");
    }

   /* public List<DadosQuestao> obterNovasQuestoes(Long idCategoria,UUID idUsuario){
        return questaoRepository.findAllByCategoriaIdAndCategoriaUsuarioId(idCategoria,idUsuario)
                .stream()
                .filter(e->novaQuestao(e))
                .map(e->new DadosQuestao(e)).collect(Collectors.toList());
    }*/

    public List<DadosQuestao> obterQuestaoParaRevisao(Long idCategoria, UUID idUsuario){
        return  questaoRepository.findAllByCategoriaIdAndCategoriaUsuarioId(idCategoria,idUsuario)
                .stream()
                .filter(e->habilitadaParaRevisao(e))
                .map(e->new DadosQuestao(e)).collect(Collectors.toList());
    }

    public List<DadosQuestao> obterQuestaoPorCategoria(Long idCategoria,UUID idUsuario){

        List<Questao> listaDados = questaoRepository.findAllByCategoriaIdAndCategoriaUsuarioId(idCategoria,idUsuario);
        for(Questao d: listaDados){
            if(d.getEtapa() != null && d.getDataCriacao()!=null) {
                LocalDateTime dataInicio = d.getDataCriacao();
                Long diasEtapa  = (long) d.getEtapa().getDuracaoDias();
                LocalDateTime proximaRevisao = dataInicio.plusDays(diasEtapa);
                System.out.println(d.getId() + " | nova questao:" + novaQuestao(d) + " | habilitada revisão:" + habilitadaParaRevisao(d) + " | Duracao dias: " + d.getEtapa().getDuracaoDias()+" | proxima :"+proximaRevisao);
            }
        }
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

    public DadosContagem obterContagemNovaQuestao(Long idCategoria, UUID idUsuario){
        return new DadosContagem(questaoRepository.countByEtapa0AndCategoriaIdAndCategoriaUsuarioId(idCategoria,idUsuario));
    }

    public List<Object> obterDataRevisaoPorCategoria(Long idCategoria) {
        List<Object> lista = questaoRepository.getDataRevisaoPorCategoria(idCategoria);
        return lista;
    }
}
