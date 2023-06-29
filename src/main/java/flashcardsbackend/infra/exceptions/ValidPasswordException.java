package flashcardsbackend.infra.exceptions;

public class ValidPasswordException extends RuntimeException{
    public ValidPasswordException(String message){
        super(message);
    }

}
