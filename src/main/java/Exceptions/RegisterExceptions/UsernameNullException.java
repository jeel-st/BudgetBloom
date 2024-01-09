package Exceptions.RegisterExceptions;

public class UsernameNullException extends Exception {
    public UsernameNullException(){
        super("Username is null");
    }
}
