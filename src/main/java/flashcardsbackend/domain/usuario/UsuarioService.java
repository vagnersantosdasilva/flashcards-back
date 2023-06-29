package flashcardsbackend.domain.usuario;

import flashcardsbackend.infra.email.Constants;
import flashcardsbackend.infra.email.EmailService;
import flashcardsbackend.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    EmailService emailService;

    @Autowired
    TokenService tokenService;

    @Transactional
    public DadosUsuarioResponseDTO criarUsuario(DadosUsuarioDTO dados){
        Usuario usuario = new Usuario(dados.username(), encoder.encode(dados.password()));
        usuario.setEnabled(true);
        Usuario response = usuarioRepository.save(usuario);
        System.out.println("Senha gerada : "+response.getPassword());
        //TODO: Chamar serviço de entrega de email
        var token = tokenService.gerarToken(usuario);
        emailService.sendConfirmationHtmlEmail(usuario,token, Constants.EMAIL_CONFIRMACAO_CADASTRO_USUARIO);

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

    public Page<DadosUsuarioResponseDTO> findAll(Pageable paginacao) {
        return usuarioRepository.findAll(paginacao).map(e->new DadosUsuarioResponseDTO(e));
    }
}
