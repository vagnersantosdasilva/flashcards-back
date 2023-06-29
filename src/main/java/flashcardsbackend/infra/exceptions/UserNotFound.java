package flashcardsbackend.infra.exceptions;

public class UserNotFound extends RuntimeException{
    public UserNotFound(String message){
        super (message);
    }
}
