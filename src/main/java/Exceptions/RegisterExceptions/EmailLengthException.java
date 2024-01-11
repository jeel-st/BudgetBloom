package Exceptions.RegisterExceptions;

public class EmailLengthException extends Exception{
    public EmailLengthException(){super("Email has to be between 6 and 69 characters");};
}
