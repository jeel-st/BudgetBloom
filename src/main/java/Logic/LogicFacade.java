package Logic;

import LocalExceptions.NewEntryExceptions.NoteIsNullException;
import LocalExceptions.NewEntryExceptions.ParseDateException;
import LocalExceptions.NewEntryExceptions.ParseDoubleException;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

public class LogicFacade {

    private static LogicFacade instance;
    private final LogicEditEntry editEntry;
    private final LogicBalance balance;
    private final LogicDatabase database;
    private final LogicOverview overview;
    private final LogicRegister register;
    private final LogicNewEntry newEntry;
    //private LogicTableEntry tableEntry;


    private LogicFacade(){
        editEntry = new LogicEditEntry();
        balance = new LogicBalance();
        database = new LogicDatabase();
        overview = new LogicOverview();
        register = new LogicRegister();
        newEntry = new LogicNewEntry();
        // ??? tableEntry = new LogicTableEntry();
    }

    public static LogicFacade getInstance() {
        if (instance == null){
            instance = new LogicFacade();
        }
        return instance;
    }

    public String saveEdit(LocalDate eingabeDatum, String eingabeGrund, int skala, String repeatBox, String eingabeZahl, String myChoiceBox, String wiederholungshaeufigkeitBox) {
           return editEntry.saveEdit(eingabeDatum, eingabeGrund, skala, repeatBox, eingabeZahl, myChoiceBox, wiederholungshaeufigkeitBox);
    }

    public double kontoVeränderungsÜberprüfer(String eingabeZahl, String myChoiceBox) {
        return newEntry.kontoVeränderungsÜberprüferEdit(eingabeZahl, myChoiceBox);
    }

    public String showContentOfWiederholungshaeufigkeitBox() throws Exception {
        return editEntry.showContentOfWiederholungshaeufigkeitBox();
    }

    public String proveRegisterTextFields(String username, String password, String password2, String email){
        return register.proveRegisterTextFields(username, password, password2, email);
    }
    public ObservableList<LogicTableEntry> datenbank() throws Exception{
        return overview.datenbank();
    }
    public int deleteRowInDatabase(double betrag, String datum, String grund, double kontostand, int wichtigkeit, Boolean regelmäßigkeitBool) throws SQLException{
        return overview.deleteRowInDatabase(betrag, datum, grund, kontostand, wichtigkeit, regelmäßigkeitBool);
    }
    public void saveValues(double betrag, String datum, String grund, double kontostand, int wichtigkeit, String regelmäßigkeit){
        overview.saveValues(betrag, datum, grund, kontostand, wichtigkeit, regelmäßigkeit);
    }
    public boolean checkIsRegularBoolean(String s){
        return newEntry.checkIsRegularBoolean(s);
    }
    public void kontoVeränderung(double amountChange, String choiceBoxValue, int sliderValue, String note, Date date, String repetitionFrequency, Boolean repeatBool) throws SQLException{
        newEntry.kontoVeränderung(amountChange, choiceBoxValue, sliderValue, note, date, repetitionFrequency, repeatBool);
    }
    public Boolean checkingFormats(String amountChange, String note, LocalDate date) throws NoteIsNullException, ParseDoubleException, ParseDateException{
        return newEntry.checkingFormats(amountChange, note, date);
    }
}
