package flashcardsbackend.infra.exceptions;

public class LimiteQuestoes extends RuntimeException{
    public LimiteQuestoes(String message){
        super(message);
    }
}
