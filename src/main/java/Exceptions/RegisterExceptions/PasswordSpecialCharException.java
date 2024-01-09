package Exceptions.RegisterExceptions;

public class PasswordSpecialCharException extends Exception{
    public PasswordSpecialCharException(){
        super("Password doesn't contain special characters");
    }
}
