package flashcardsbackend.infra.exceptions;

public class TokenException extends RuntimeException{
    public TokenException(String message) {
        super(message);
    }

}
