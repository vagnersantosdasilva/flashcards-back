package flashcardsbackend.infra.email;

import flashcardsbackend.infra.exceptions.SMTPException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

import java.net.UnknownHostException;


public class SmtpEmailService extends AbstractEmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    private static final Logger LOG = LoggerFactory.getLogger(SmtpEmailService.class);

    @Override
    public void sendHtmlEmail(MimeMessage msg) {
        try{
            LOG.info("Enviando Email!");
            javaMailSender.send(msg);
            LOG.info("Email enviado com sucesso!");
        }catch (Exception e){
            throw new SMTPException("Não foi possível enviar o e-mail");
        }

    }
}
