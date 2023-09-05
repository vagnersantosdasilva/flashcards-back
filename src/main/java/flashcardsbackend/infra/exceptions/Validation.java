package flashcardsbackend.infra.exceptions;

public class Validation extends RuntimeException{
    public Validation(String message){
        super(message);
    }
}
