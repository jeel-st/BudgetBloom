package Logic;

import Exceptions.NewEntryExceptions.AmountChangeIsNullException;
import Exceptions.NewEntryExceptions.NoteIsNullException;
import Exceptions.NewEntryExceptions.ParseDateException;
import Exceptions.NewEntryExceptions.ParseDoubleException;
import Singleton.SingletonUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;

public class LogicNewEntry extends LogicSuperClass{
    public static Logger log = LogManager.getLogger(LogicNewEntry.class);
    private final LogicDatabase lg = new LogicDatabase();
    private final SingletonUser sp = SingletonUser.getInstance();
    private final String localUsername = sp.getName();


    public void changedAccount(double amountChange, String choiceBoxValue, int sliderValue, String note, Date date, String repetitionFrequency, String repeatBoxValue, String payment) throws Exception {

        try (Connection con = lg.getConnection()) {
            log.debug("Connection to database succeed");

            String sql = "INSERT INTO konto" + localUsername + " VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            try {
                stmt.setDouble(3, accountChangeChecker(amountChange, choiceBoxValue));
                log.debug("Kontoänderungseingabe erfolgreich");
            } catch (Exception e) {
                log.error("Kontoänderungseingabe hat nicht geklappt", e);

            }
            stmt.setString(2, note);
            stmt.setDate(1, date);
            try {
                double newAccountBalance = currentAccountBalance(amountChange, choiceBoxValue);
                log.info("Neuer Kontostand: " + newAccountBalance);
                stmt.setDouble(4, newAccountBalance);
            } catch (Exception e) {
                log.error("Couldn't connect to Database", e);
            }
            stmt.setInt(5, sliderValue);
            stmt.setBoolean(6, super.isRegularBool(repeatBoxValue));
            stmt.setString(7, super.checkFrequency(repeatBoxValue, repetitionFrequency));
            try {
                stmt.setString(8, super.checkPayment(payment, amountChange));
            }catch (Exception e){
                log.warn("Your payment method wasn't accepted.");
            }
            try {
                stmt.executeUpdate();
            } catch (Exception e) {
                log.warn("Input could not be added");
            }
        } catch (SQLException e) {
            log.error("Couldn't connect to Database", e);
            throw e;
        }
    }

    private double currentAccountBalance(double amountChange, String choiceBoxValue) throws Exception {
        try (Connection con = lg.getConnection()) {
            log.debug("Connection to database succeed");

            String sql = "SELECT bankBalance FROM konto" + localUsername + " ORDER BY edate DESC, id DESC LIMIT 1";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            try {
                while (rs.next()) {
                    double accountBalance = rs.getDouble("bankBalance");


                    double newAccountBalance = accountBalance + accountChangeChecker(amountChange, choiceBoxValue);
                    newAccountBalance = Math.round(newAccountBalance * 100.0) / 100.0;
                    return newAccountBalance;
                }
            } catch (Exception e) {
                log.error("kein Kontostand gefunden", e);
            }

            throw new Exception();
        } catch (SQLException e) {
            log.error("Couldn't connect to Database", e);
            throw e;
        }
    }

    double accountChangeChecker(double amountChange, String choiceBoxValue) {
        if (!choiceBoxValue.equals("Einnahme") && amountChange > 0) {
            return -amountChange;
        } else {
            return amountChange;
        }
    }


    boolean checkingFormats(String amountChange, String note, LocalDate date) throws NoteIsNullException, ParseDoubleException, ParseDateException, AmountChangeIsNullException {
        if (note.isEmpty()) {
            throw new NoteIsNullException();
        }
        if(amountChange.isEmpty()){
            throw new AmountChangeIsNullException();
        }
        try {
            Double.parseDouble(amountChange);
        } catch (Exception e) {
            throw new ParseDoubleException();
        }
        try {
            Date.valueOf(date.toString());
        } catch (Exception e){
            throw new ParseDateException();
        }
        return true;
    }
}
