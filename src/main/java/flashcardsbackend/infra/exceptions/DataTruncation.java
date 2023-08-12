package flashcardsbackend.infra.exceptions;

public class DataTruncation extends Exception{
    DataTruncation(String message){
         super(message);
    }
}
