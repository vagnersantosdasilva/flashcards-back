package flashcardsbackend.infra.email;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
public class EmailConfiguration {

    @Bean
    EmailService emailService(){
        return new SmtpEmailService();
    };
}
