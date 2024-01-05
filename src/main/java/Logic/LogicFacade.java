package Logic;
import LocalExceptions.NewEntryExceptions.*;
import javafx.collections.ObservableList;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;


public class LogicFacade {

    private static LogicFacade instance;
    private final LogicEditEntry editEntry;
    private final LogicFirstLogin firstLogin;

    private final LogicLogin login;
    private final LogicOverview overview;
    private final LogicRegister register;
    private final LogicNewEntry newEntry;
    //private LogicTableEntry tableEntry;


    private LogicFacade(){
        editEntry = new LogicEditEntry();
        firstLogin = new LogicFirstLogin();
        login = new LogicLogin();
        overview = new LogicOverview();
        register = new LogicRegister();
        newEntry = new LogicNewEntry();
    }

    public static LogicFacade getInstance() {
        if (instance == null){
            instance = new LogicFacade();
        }
        return instance;
    }

    public static void updateBalance() {
        LogicBalance.updateBalance();
    }


    public double accountChangeChecker(double inputNumber, String myChoiceBox) {
        return newEntry.accountChangeChecker(inputNumber, myChoiceBox);
    }

    public String showContentOfRepeatabilityBox() throws Exception {
        return editEntry.showContentOfRepeatabilityBox();
    }

    public void insertInitialBalance(double balance) {
        firstLogin.insertInitialBalance(balance);
    }

    public boolean isBalanceNumber(String balance) {
        return firstLogin.isBalanceNumber(balance);
    }

    public boolean isValidUser(String username, String password) throws IOException {
        return login.isValidUser(username, password);
    }

    public boolean isFirstLogin() {
        return login.isFirstLogin();
    }

    public String proveRegisterTextFields(String username, String password, String password2, String email){
        return register.proveRegisterTextFields(username, password, password2, email);
    }
    public ObservableList<LogicTableEntry> database() throws Exception{
        return overview.database();
    }
    public int deleteRowInDatabase(double amount, String date, String reason, double accountBalance, int importance, Boolean regularityBool) throws SQLException{
        return overview.deleteRowInDatabase(amount, date, reason, accountBalance, importance, regularityBool);
    }
    public void saveValues(double amount, String date, String reason, double accountBalance, int importance, String regularity){
        overview.saveValues(amount, date, reason, accountBalance, importance, regularity);
    }
    public void changedAccount(double amountChange, String choiceBoxValue, int sliderValue, String note, Date date, String repetitionFrequency, String repeatBoxValue) throws SQLException{
        newEntry.changedAccount(amountChange, choiceBoxValue, sliderValue, note, date, repetitionFrequency, repeatBoxValue);
    }
    public boolean checkingFormats(String amountChange, String note, LocalDate date) throws NoteIsNullException, ParseDoubleException, ParseDateException, AmountChangeIsNullException {
        return newEntry.checkingFormats(amountChange, note, date);
    }
    public String checkFrequency (String repeatBox, String repeatabilityBox){
        return editEntry.checkFrequency(repeatBox, repeatabilityBox);
    }
    public boolean isRegularBool(String s){
        return editEntry.isRegularBool(s);
    }
}
