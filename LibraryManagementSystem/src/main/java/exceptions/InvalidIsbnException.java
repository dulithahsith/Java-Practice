package exceptions;

public class InvalidIsbnException extends Exception{
    public InvalidIsbnException(String message){
        super(message);
    }
}