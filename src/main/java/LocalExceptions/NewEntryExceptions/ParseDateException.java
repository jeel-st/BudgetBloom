package LocalExceptions.NewEntryExceptions;

public class ParseDateException extends Exception{
    public ParseDateException(){super("String cannot be parsed into date");}
}
