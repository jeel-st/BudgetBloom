package Exceptions.RegisterExceptions;

public class UsernameTakenException extends Exception{
    public UsernameTakenException(){
        super("Username is not available");
    }
}
