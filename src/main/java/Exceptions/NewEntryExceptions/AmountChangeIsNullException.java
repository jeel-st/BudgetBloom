package Exceptions.NewEntryExceptions;

public class AmountChangeIsNullException extends Exception{
    public AmountChangeIsNullException(){
        super("Eingabe/ Ausgabe Feld ist leer");
    }
}
