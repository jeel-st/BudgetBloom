package Exceptions.RegisterExceptions;

public class PasswordLengthException extends Exception{
    public PasswordLengthException(){
        super("Password has to be between 6 and 30 Characters");
    }
}
