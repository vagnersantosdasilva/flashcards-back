package flashcardsbackend.domain.categoria;

import flashcardsbackend.domain.categoria.dto.ContagemEtapaDTO;
import flashcardsbackend.domain.categoria.dto.DadosCategoria;
import flashcardsbackend.domain.categoria.dto.DashboardCategoria;
import flashcardsbackend.domain.categoria.utils.Contador;
import flashcardsbackend.domain.categoria.utils.ContadorComparator;
import flashcardsbackend.domain.usuario.Usuario;
import flashcardsbackend.domain.usuario.UsuarioRepository;
import flashcardsbackend.infra.exceptions.ResourceNotFound;
import flashcardsbackend.infra.exceptions.UserNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;


    public DadosCategoria criar(DadosCategoria dados){
        Optional<Usuario> userOpt = usuarioRepository.findById(dados.usuarioId());
        if (userOpt.isPresent()) {
             Categoria categoria =  new Categoria();
             categoria.setNome(dados.nome());
             categoria.setUsuario(userOpt.get());
             Categoria categoriaResponse =  categoriaRepository.save(categoria);
             DadosCategoria dadosResponse = new DadosCategoria(categoriaResponse);
             return dadosResponse;
        }
        else{
            throw new UserNotFound("Usuário não informado!");
        }

    }

    @Transactional
    public DadosCategoria atualizar(DadosCategoria dto) {

        Optional<Usuario> userOpt = usuarioRepository.findById(dto.usuarioId());
        if (dto.id()==null) throw new ResourceNotFound("Id de categoria é obrigatório");
        if (userOpt.isPresent()){
            Optional <Categoria> catOpt = categoriaRepository.findById(dto.id());
            if (catOpt.isPresent()){
                Categoria categoria = new Categoria();
                categoria.setUsuario(userOpt.get());
                categoria.setNome(dto.nome());
                categoria.setId(dto.id());
                return new DadosCategoria(categoriaRepository.save(categoria));
            }
            throw new ResourceNotFound("Categoria não encontrada");
        }
        throw new UserNotFound("Usuário não encontrado");
    }


    public List<DadosCategoria> listarCategorias(UUID idUsuario) {
        List<Categoria> listaCategoria = categoriaRepository.findByUsuarioId(idUsuario);
        return listaCategoria.stream().map(e->new DadosCategoria(e)).collect(Collectors.toList());
    }

    public void removerCategoria(Long id,UUID idUsuario) {
        Optional<Categoria> categoriaOptional = categoriaRepository.findById(id);
        if (categoriaOptional.isPresent()) {
            if (!idUsuario.equals(categoriaOptional.get().getUsuario().getId()))
                throw new ResourceNotFound("Usuário não encontrado");
            categoriaRepository.delete(categoriaOptional.get());
        }
        else{
            throw new ResourceNotFound("Categoria não encontrada!");
        }
    }

    public DadosCategoria obterCategoria( Long idCategoria) {
        Optional<Categoria> categoriaOptional = categoriaRepository.findById(idCategoria);
        if (categoriaOptional.isPresent()){
            return new DadosCategoria(categoriaOptional.get());
        }
        throw new ResourceNotFound("Categoria não encontrada!");
    }
    public List<DashboardCategoria> obterDashboard(UUID idUsuario){
        return convertList(categoriaRepository.getCountEtapaByCategoria(idUsuario));
    }

    private  List<DashboardCategoria> convertList(List<ContagemEtapaDTO> originalList) {
        Map<Long, DashboardCategoria> idToContagemMap = new HashMap<>();

        for (ContagemEtapaDTO item : originalList) {
            Long id =  item.getId();
            String etapa = item.getEtapa();
            Integer quantidade = item.getQuantidade();
            String nome = item.getNome();

            DashboardCategoria contagem = idToContagemMap.computeIfAbsent(id,
                    k -> new DashboardCategoria(id, nome, null, null, null, null, null, null));

            switch (etapa) {
                case "ETAPA0" -> contagem.setEtapa0(quantidade);
                case "ETAPA1" -> contagem.setEtapa1(quantidade);
                case "ETAPA2" -> contagem.setEtapa2(quantidade);
                case "ETAPA3" -> contagem.setEtapa3(quantidade);
                case "ETAPA4" -> contagem.setEtapa4(quantidade);
                case "ETAPA5" -> contagem.setEtapa5(quantidade);
                case "ETAPA6" -> contagem.setEtapa6(quantidade);
            }
            idToContagemMap.put(id, contagem);

        }
        idToContagemMap.values().forEach(e -> {
            setNivel(e);
            setTotalQuestoes(e);
        });
        /*List<Dashboard> novaLista = new ArrayList<>(idToContagemMap.values());
        return novaLista.stream().map(e-> setTotalQuestoes(e)).collect(Collectors.toList());*/
        return new ArrayList<>(idToContagemMap.values());
    }

    private DashboardCategoria setNivel(DashboardCategoria contagem){
        Contador c0 = new Contador("Iniciante",contagem.getEtapa0()==null?0:contagem.getEtapa0()*7);
        Contador c1 = new Contador("Iniciante",contagem.getEtapa1()==null?0:contagem.getEtapa1()*7);
        Contador c2 = new Contador("Básico",contagem.getEtapa2()==null?0:contagem.getEtapa2()*6);
        Contador c3 = new Contador("Básico",contagem.getEtapa3()==null?0:contagem.getEtapa3()*6);
        Contador c4 = new Contador("Intermediário",contagem.getEtapa4()==null?0:contagem.getEtapa4()*5);
        Contador c5 = new Contador("Avançado",contagem.getEtapa5()==null?0:contagem.getEtapa5()*4);
        Contador c6 = new Contador("Fluente",contagem.getEtapa6()==null?0:contagem.getEtapa6()*3);
        List<Contador> listaContadores = new ArrayList<>(Arrays.asList(c0, c1, c2, c3, c4, c5, c6));
        listaContadores.sort(new ContadorComparator());
        Contador maiorContador = listaContadores.get(listaContadores.size() - 1);
        contagem.setNivel(maiorContador.getDescricao());
        return contagem;
    }

    private DashboardCategoria setTotalQuestoes(DashboardCategoria contagem) {
        int total = 0;
        if (contagem.getTotalQuestoes()!=null ) total = contagem.getTotalQuestoes();

        if (contagem.getEtapa0()!=null) total+=contagem.getEtapa0().intValue();
        if (contagem.getEtapa1()!=null) total+=contagem.getEtapa1().intValue();
        if (contagem.getEtapa2()!=null) total+=contagem.getEtapa2().intValue();
        if (contagem.getEtapa3()!=null) total+=contagem.getEtapa3().intValue();
        if (contagem.getEtapa4()!=null) total+=contagem.getEtapa4().intValue();
        if (contagem.getEtapa5()!=null) total+=contagem.getEtapa5().intValue();
        if (contagem.getEtapa6()!=null) total+=contagem.getEtapa6().intValue();
        contagem.setTotalQuestoes(total);
        return  contagem;
    }


}
