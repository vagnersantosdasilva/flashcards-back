package flashcardsbackend.domain.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
