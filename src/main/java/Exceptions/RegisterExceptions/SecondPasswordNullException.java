package Exceptions.RegisterExceptions;

public class SecondPasswordNullException extends Exception{
    public SecondPasswordNullException(){
        super("Password is null");
    }
}
