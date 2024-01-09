package Exceptions.RegisterExceptions;

public class PasswordNullException extends Exception {
    public PasswordNullException(){
        super("Password is null");
    }
}
