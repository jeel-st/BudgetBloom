package LocalExceptions.NewEntryExceptions;

public class ParseDoubleException extends Exception {
    public ParseDoubleException(){
        super("String cannot be parsed into double");
    }
}
