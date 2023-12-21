package LocalExceptions.RegisterExceptions;

public class SecondPasswordNullException extends Exception{
    public SecondPasswordNullException(){
        super("Password is null");
    }
}
