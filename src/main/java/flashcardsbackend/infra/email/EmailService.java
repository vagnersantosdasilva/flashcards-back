package flashcardsbackend.infra.email;

import flashcardsbackend.domain.usuario.Usuario;
import flashcardsbackend.infra.security.TokenService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public interface EmailService {

    void sendHtmlEmail(MimeMessage msg);
    void sendConfirmationHtmlEmail(Usuario user, String token,Integer emailVerificacao);
}
