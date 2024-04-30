package flashcardsbackend.infra.util.FileUtil;

import flashcardsbackend.infra.email.EmailService;
import flashcardsbackend.infra.email.SmtpEmailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileUtilConfiguration {
    @Bean
    FileUtil fileUtil(){
        return new FileUtilImplementacao();
    };
}
