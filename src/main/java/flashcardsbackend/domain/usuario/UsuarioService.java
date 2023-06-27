package flashcardsbackend.domain.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    PasswordEncoder encoder;

    @Transactional
    public DadosUsuarioResponseDTO criarUsuario(DadosUsuarioDTO dados){
        Usuario usuario = new Usuario(dados.username(), encoder.encode(dados.password()));
        usuario.setEnabled(false);
        Usuario response = usuarioRepository.save(usuario);
        System.out.println("Senha gerada : "+response.getPassword());
        return new DadosUsuarioResponseDTO(response);
    }

    public DadosUsuarioResponseDTO findUsuarioById(UUID id){
        Usuario usuario = usuarioRepository.findById(id).get();
        return new DadosUsuarioResponseDTO(usuario);
    }
    @Transactional
    public DadosUsuarioResponseDTO validarCriacao(DadosUsuarioDTO dados) {
        //TODO: Obter usuario de token usando um novo service para extrair informacao
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(dados.username());
        if (usuarioOpt.isPresent()) {
            usuarioOpt.get().setEnabled(true);
            return new DadosUsuarioResponseDTO(usuarioOpt.get());
        }
        throw new RuntimeException("Usuario não cadastrado");
    }
}
