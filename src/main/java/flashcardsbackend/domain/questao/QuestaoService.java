package flashcardsbackend.domain.questao;

import flashcardsbackend.domain.categoria.Categoria;
import flashcardsbackend.domain.categoria.CategoriaRepository;
import flashcardsbackend.domain.questao.validation.ValidacaoHtml;
import flashcardsbackend.domain.questao.dto.DadosContagem;
import flashcardsbackend.domain.questao.dto.DadosQuestao;
import flashcardsbackend.domain.questao.dto.DadosQuestaoResposta;
import flashcardsbackend.domain.questao.dto.DadosQuestaoUpdate;
import flashcardsbackend.domain.relatorios.TentativaEtapa;
import flashcardsbackend.domain.relatorios.TentativaEtapaRepository;
import flashcardsbackend.domain.usuario.UsuarioRepository;
import flashcardsbackend.infra.exceptions.LimiteQuestoes;
import flashcardsbackend.infra.exceptions.ResourceNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class QuestaoService {
    @Value("${dominio.regra.limite-questoes}")
    private Integer limitQuestoes;
    @Autowired
    QuestaoRepository questaoRepository;

    @Autowired
    TentativaEtapaRepository tentativaEtapaRepository;
    @Autowired
    CategoriaRepository categoriaRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    List<ValidacaoHtml> validacoes = new ArrayList<>();
    @Transactional
    public DadosQuestao criar(DadosQuestao dto, UUID idUsuario){

        validacoes.forEach(v-> v.validar(dto.pergunta(),"Pergunta"));
        validacoes.forEach(v-> v.validar(dto.resposta(),"Resposta"));
        Integer contagem = questaoRepository.getCountQuestaoByUsuario(idUsuario);
        if (contagem >= limitQuestoes) throw new LimiteQuestoes("A quantidade máxima de questões foi atingida!");
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

        validacoes.forEach(v-> v.validar(dto.pergunta(),"Pergunta"));
        validacoes.forEach(v-> v.validar(dto.resposta(),"Resposta"));

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

    public void responderPergunta(DadosQuestaoResposta dto, UUID idUsuario){
        Categoria categoria = obterCategoriaValidada(dto.categoriaId(),idUsuario);
        Optional<Questao> questaoOpt =  questaoRepository.findById(dto.id());
        if (questaoOpt.isEmpty()) throw new ResourceNotFound("Questão não encontrada!");

        TentativaEtapa tentativaEtapa = new TentativaEtapa();
        tentativaEtapa.setAcerto(dto.acerto());
        tentativaEtapa.setDataTentativa(questaoOpt.get().getDataCriacao());
        tentativaEtapa.setEtapa(questaoOpt.get().getEtapa().getDescricao()=="Etapa 0"?"Etapa 1":questaoOpt.get().getEtapa().getDescricao());
        tentativaEtapa.setCategoria(categoria);
        tentativaEtapa.setUsuario(usuarioRepository.getReferenceById(idUsuario));
        tentativaEtapaRepository.save(tentativaEtapa);

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
                //System.out.println(d.getId() + " | nova questao:" + novaQuestao(d) + " | habilitada revisão:" + habilitadaParaRevisao(d) + " | Duracao dias: " + d.getEtapa().getDuracaoDias()+" | proxima :"+proximaRevisao);
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
