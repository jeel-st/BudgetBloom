package LocalExceptions.RegisterExceptions;

public class EmailNullException extends Exception{
    public EmailNullException(){
        super("Email is null");
    }
}
