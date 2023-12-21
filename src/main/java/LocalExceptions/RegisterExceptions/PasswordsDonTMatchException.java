package LocalExceptions.RegisterExceptions;

public class PasswordsDonTMatchException extends Exception{
    public PasswordsDonTMatchException(){
        super("Passwords do not match");
    }
}
