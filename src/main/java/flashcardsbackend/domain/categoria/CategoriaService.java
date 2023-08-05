package flashcardsbackend.domain.categoria;

import flashcardsbackend.domain.usuario.Usuario;
import flashcardsbackend.domain.usuario.UsuarioRepository;
import flashcardsbackend.infra.exceptions.ResourceNotFound;
import flashcardsbackend.infra.exceptions.UserNotFound;
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
}
