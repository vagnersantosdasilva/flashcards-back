package flashcardsbackend.infra.exceptions;

public class DuplicateUser extends RuntimeException{

    public DuplicateUser(String message){
        super(message);
    }
}
