package flashcardsbackend.domain.categoria;

import flashcardsbackend.domain.usuario.Usuario;
import flashcardsbackend.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;


    public DadosCategoria criar(DadosCategoria dados){
        Optional<Usuario> userOpt = usuarioRepository.findById(dados.usuarioId());
        System.out.println("pre do método");
        if (userOpt.isPresent()) {
            System.out.println("inicio do método");
             Categoria categoria =  new Categoria();
             categoria.setNome(dados.nome());
             categoria.setUsuario(userOpt.get());
             Categoria categoriaResponse =  categoriaRepository.save(categoria);
             DadosCategoria dadosResponse = new DadosCategoria(categoriaResponse);
             System.out.println("fim do método");
             return dadosResponse;
        }
        else{
            throw new RuntimeException("Usuário não informado!");
        }

    }

    @Transactional
    public DadosCategoria atualizar(DadosCategoria dto) {
        Optional<Usuario> userOpt = usuarioRepository.findById(dto.usuarioId());
        if (dto.id()==null) throw new RuntimeException("Id de categoria é obrigatório");
        if (userOpt.isPresent()){
            Optional <Categoria> catOpt = categoriaRepository.findById(dto.id());
            if (catOpt.isPresent()){
                Categoria categoria = new Categoria();
                categoria.setUsuario(userOpt.get());
                categoria.setNome(dto.nome());
                categoria.setId(dto.id());
                return new DadosCategoria(categoriaRepository.save(categoria));
            }
            throw new RuntimeException("Categoria não encontrada");
        }
        throw new RuntimeException("Usuário não encontrado");
    }


    public List<DadosCategoria> listarCategorias(UUID idUsuario) {
        List<Categoria> listaCategoria = categoriaRepository.findByUsuarioId(idUsuario);
        //System.out.println(listaCategoria.toString());
        return listaCategoria.stream().map(e->new DadosCategoria(e)).collect(Collectors.toList());
    }

    public void removerCategoria(Long id,UUID idUsuario) {
        Optional<Categoria> categoriaOptional = categoriaRepository.findById(id);

        if (categoriaOptional.isPresent()) {
            System.out.println(categoriaOptional.get().getUsuario().getId());
            System.out.println(idUsuario);
            if (!idUsuario.equals(categoriaOptional.get().getUsuario().getId()))
                throw new RuntimeException("Usuário não encontrado");
            categoriaRepository.delete(categoriaOptional.get());
        }
        else{
            throw new RuntimeException("Categoria não encontrada!");
        }
    }

    public DadosCategoria obterCategoria( Long idCategoria) {
        Optional<Categoria> categoriaOptional = categoriaRepository.findById(idCategoria);
        if (categoriaOptional.isPresent()){
            return new DadosCategoria(categoriaOptional.get());
        }
        throw new RuntimeException("Categoria não encontrada!");
    }
}