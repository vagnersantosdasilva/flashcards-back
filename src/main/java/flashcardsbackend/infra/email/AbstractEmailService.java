package flashcardsbackend.infra.email;



import flashcardsbackend.domain.usuario.Usuario;
import flashcardsbackend.domain.usuario.UsuarioService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

public abstract class AbstractEmailService implements EmailService {

    @Value("${default.sender}")
    private String sender;
    @Value("${default.url}")
    private String contextPath;

    @Value("${frontend.uri}")
    private String frontend;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private JavaMailSender javaMailSender;
    @Lazy
    @Autowired
    private UsuarioService userService;

    @Override
    public void sendConfirmationHtmlEmail(Usuario user, String vToken, Integer confirmacao){
        try {
            MimeMessage mimeMessage = prepareMimeMessageFromUser(user, vToken,confirmacao);
            sendHtmlEmail(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException msg) {
            throw new RuntimeException("Objeto não encontrado");
        }
    }

    protected MimeMessage prepareMimeMessageFromUser(Usuario user, String vToken, Integer confirmacao) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(user.getUsername());
        mimeMessageHelper.setFrom(this.sender);
        mimeMessageHelper.setSubject("Confirmação de Registro");
        mimeMessageHelper.setSentDate(new Date((System.currentTimeMillis())));
        mimeMessageHelper.setText(htmlFromTemplateUser(user,vToken,confirmacao), true);
        return mimeMessage;
    }

    protected String htmlFromTemplateUser(Usuario user, String vToken,Integer confirmacao) throws UnsupportedEncodingException {
        String encodedToken = URLEncoder.encode(vToken, StandardCharsets.UTF_8.toString());
        String confirmationUrl = this.contextPath + "/api/public/usuario?token="+encodedToken;
        String resetUrl = this.frontend+"/reset?token="+encodedToken;
        Context context = new Context();
        context.setVariable("user", user);
        if (confirmacao.equals(Constants.EMAIL_CONFIRMACAO_CADASTRO_USUARIO)){
            context.setVariable("confirmationUrl", confirmationUrl);
            return templateEngine.process("email/registerUser", context);
        }
        else{
            context.setVariable("confirmationUrl", resetUrl);
            return templateEngine.process("email/resetPassword", context);
        }

    }
}
