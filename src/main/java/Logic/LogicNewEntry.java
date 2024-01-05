package Logic;

import LocalExceptions.NewEntryExceptions.AmountChangeIsNullException;
import LocalExceptions.NewEntryExceptions.NoteIsNullException;
import LocalExceptions.NewEntryExceptions.ParseDateException;
import LocalExceptions.NewEntryExceptions.ParseDoubleException;
import Singleton.SingletonUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;

public class LogicNewEntry {
    public static Logger log = LogManager.getLogger(LogicNewEntry.class);
    LogicDatabase lg = new LogicDatabase();
    SingletonUser sp = SingletonUser.getInstance();
    private final String localUsername = sp.getName();


    void changedAccount(double amountChange, String choiceBoxValue, int sliderValue, String note, Date date, String repetitionFrequency, String repeatBoxValue) throws SQLException {
        try (Connection con = lg.getConnection()) {
            log.info("Connection to database succeed");

            String sql = "INSERT INTO konto" + localUsername + " VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            try {
                stmt.setDouble(3, accountChangeChecker(amountChange, choiceBoxValue));
                log.info("Kontoänderungseingabe erfolgreich");
            } catch (Exception e) {
                log.error("Kontoänderungseingabe hat nicht geklappt");

            }
            stmt.setString(2, note);
            stmt.setDate(1, date);
            try {
                double newAccountBalance = currentAccountBalance(amountChange, choiceBoxValue);
                log.info("Neuer Kontostand: " + newAccountBalance);
                stmt.setDouble(4, newAccountBalance);
            } catch (Exception e) {
                log.error("Couldn't connect to Database");
            }
            stmt.setInt(5, sliderValue);
            stmt.setBoolean(6, LogicFacade.getInstance().isRegularBool(repeatBoxValue));
            log.info(repeatBoxValue);
            log.info(repetitionFrequency);
            stmt.setString(7, LogicFacade.getInstance().checkFrequency(repeatBoxValue, repetitionFrequency));
            try {
                stmt.executeUpdate();
            } catch (Exception e) {
                log.info("Eingabe konnte nicht hinzugefügt werden");
            }
        } catch (SQLException e) {
            log.error("Couldn't connect to Database", e);
            throw e;
        }
    }

    private double currentAccountBalance(double amountChange, String choiceBoxValue) throws Exception {
        try (Connection con = lg.getConnection()) {
            log.info("Connection to database succeed");

            String sql = "SELECT bankBalance FROM konto" + localUsername + " ORDER BY edate DESC, id DESC LIMIT 1";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            try {
                while (rs.next()) {
                    double accountBalance = rs.getDouble("bankBalance");

                    log.info(accountBalance);
                    double newAccountBalance = accountBalance + accountChangeChecker(amountChange, choiceBoxValue);
                    newAccountBalance = Math.round(newAccountBalance * 100.0) / 100.0;
                    return newAccountBalance;
                }
            } catch (Exception e) {
                log.error("kein Kontostand gefunden");
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
