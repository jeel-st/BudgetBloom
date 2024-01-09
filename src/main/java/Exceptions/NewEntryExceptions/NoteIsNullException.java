package Exceptions.NewEntryExceptions;

public class NoteIsNullException extends Exception{
    public NoteIsNullException(){super("Note is a required field");}
}
