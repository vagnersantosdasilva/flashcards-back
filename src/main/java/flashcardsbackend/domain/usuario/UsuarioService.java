package flashcardsbackend.domain.usuario;

import flashcardsbackend.domain.usuario.dto.*;
import flashcardsbackend.infra.email.Constants;
import flashcardsbackend.infra.email.EmailService;
import flashcardsbackend.infra.exceptions.DuplicateUser;
import flashcardsbackend.infra.exceptions.UserNotFound;
import flashcardsbackend.infra.exceptions.ValidPasswordException;
import flashcardsbackend.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
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

    @Value("${frontend.uri}")
    private String frontendUri;

    @Transactional
    public DadosUsuarioResponseDTO criarUsuario(DadosUsuarioCriacao dados) throws InterruptedException {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByUsername(dados.username());
        if(usuarioOptional.isPresent()) throw new DuplicateUser("Usuário já cadastrado!");
        if (!dados.password().equals(dados.confirmPassword())) throw new ValidPasswordException("Campos Password e confirmPassword devem ser iguais!");

        Usuario usuario = new Usuario(dados.username(), encoder.encode(dados.password()));
        usuario.setEnabled(false);
        Usuario response = usuarioRepository.save(usuario);

        var token = tokenService.gerarToken(usuario);
        emailService.sendConfirmationHtmlEmail(usuario,token, Constants.EMAIL_CONFIRMACAO_CADASTRO_USUARIO);

        return new DadosUsuarioResponseDTO(response);
    }

    public DadosUsuarioResponseDTO findUsuarioById(UUID id){
        Usuario usuario = usuarioRepository.findById(id).get();
        return new DadosUsuarioResponseDTO(usuario);
    }
    @Transactional
    public String validarCriacao(String token) {
        //TODO: Obter usuario de token usando um novo service para extrair informacao
        String username = tokenService.getSubject(token);
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);
        if (usuarioOpt.isPresent()) {
            usuarioOpt.get().setEnabled(true);
            String tokenResponse = tokenService.gerarToken(usuarioOpt.get());
            String urlResponse = frontendUri+"/?token="+tokenResponse;
            return urlResponse;
        }
        else throw new UserNotFound("Usuario não encontrado");
    }

    public Page<DadosUsuarioResponseDTO> findAll(Pageable paginacao) {
        return usuarioRepository.findAll(paginacao).map(e->new DadosUsuarioResponseDTO(e));
    }

    public void habilitarResetPassword(String email) throws RuntimeException {
        Optional<Usuario> user = usuarioRepository.findByUsername(email);
        if (user.isPresent()){
            var newPassword = createPassword();
            Usuario usuario = user.get();
            usuario.setPassword(encoder.encode(newPassword));
            usuario.setEnabled(true);
            usuarioRepository.save(usuario);
            //var token = tokenService.gerarToken(user.get());
            emailService.sendConfirmationHtmlEmail(usuario,newPassword, Constants.EMAIL_CONFIRMACAO_RESET_PASSWORD);
        }
        else throw new UserNotFound("Usuário não encontrado!");
    }
    @Transactional
    public void resetPassword(DadosUsuarioResetPasswordDTO dadosDTO) {
        if (!dadosDTO.isPasswordsEquals()) throw new ValidPasswordException("Campo password e confirmPassword devem ser iguais");
        Optional<Usuario> user = usuarioRepository.findByUsername(dadosDTO.username());
        if (user.isPresent()){
            Usuario usuario = user.get();
            //System.out.println(user.get().getUsername());
            usuario.setPassword(encoder.encode(dadosDTO.password()));
            usuario.setEnabled(true);
        }
        else throw new UserNotFound("Usuário não encontrado!");
    }

    private String createPassword(){
        String caracteres = "*!Aa@BbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789#$%";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 9; i++) {
            int index = random.nextInt(caracteres.length());
            password.append(caracteres.charAt(index));
        }

        return password.toString();
    }
    @Transactional
    private Usuario resetPasswordPublic(Usuario user, String newPassword){

        user.setPassword(encoder.encode(newPassword));
        user.setEnabled(true);
        return user;
    }


    public DadosUsuarioSocialResponseDTO criarUsuarioSocial(DadosUsuarioSocialCriacao dadosUsuarioCriacao) throws GeneralSecurityException, IOException {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(dadosUsuarioCriacao.username());
        if(usuarioOpt.isEmpty()) {
            Usuario newUser = new Usuario(dadosUsuarioCriacao.username(),true,true,dadosUsuarioCriacao.provedor(),dadosUsuarioCriacao.idSocial());
            newUser.setPassword(encoder.encode(createPassword()));
            Usuario userResponse = usuarioRepository.save(newUser);
            var tokenResponse = tokenService.gerarToken(newUser);
            return new DadosUsuarioSocialResponseDTO(userResponse,tokenResponse);
        }
        var tokenResponse  = tokenService.gerarToken(usuarioOpt.get());
        return new DadosUsuarioSocialResponseDTO(usuarioOpt.get(),tokenResponse);
    }

    public Optional<Usuario> findByIdSocial(String idSocial){
        return usuarioRepository.findByIdSocial(idSocial);
    }
}
