package flashcardsbackend.infra.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class SMTPException extends RuntimeException{

    public SMTPException(String message){
         super(message);
    }
}
