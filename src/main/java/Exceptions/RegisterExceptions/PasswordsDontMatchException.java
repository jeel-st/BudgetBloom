package Exceptions.RegisterExceptions;

public class PasswordsDontMatchException extends Exception{
    public PasswordsDontMatchException(){
        super("Passwords do not match");
    }
}
