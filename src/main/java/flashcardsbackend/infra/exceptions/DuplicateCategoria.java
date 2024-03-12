package flashcardsbackend.infra.exceptions;

public class DuplicateCategoria extends RuntimeException{
    public DuplicateCategoria(String message){
        super(message);
    }
}
