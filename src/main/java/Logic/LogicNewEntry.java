package Logic;

import Controller.ControllerEditEntry;
import LocalExceptions.NewEntryExceptions.NoteIsNullException;
import LocalExceptions.NewEntryExceptions.ParseDateException;
import LocalExceptions.NewEntryExceptions.ParseDoubleException;
import Singleton.SingletonUser;
import javafx.css.StyleableStringProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;

public class LogicNewEntry {
    public static Logger log = LogManager.getLogger(LogicNewEntry.class);
    LogicDatabase lg = new LogicDatabase();

    public double kontoVeränderungsÜberprüferEdit(String eingabeZahl, String myChoiceBox) {


        double d = Double.parseDouble(eingabeZahl);
        if (myChoiceBox.equals("Einnahme")) {

            log.info(d);
            return d;
        } else {

            log.info(d);
            if (d == 0) {
                return d;
            } else if (d > 0) {
                return -d;
            } else {
                return d;
            }

        }

    }

    public boolean checkIsRegularBoolean(String s) {
        if (s.equals("Einmalig")) {
            return false;
        } else {
            return true;
        }
    }

    public void kontoVeränderung(double amountChange, String choiceBoxValue, int sliderValue, String note, Date date, String repetitionFrequency, Boolean repeatBool) throws SQLException {
        try (Connection con = lg.getConnection()) {
            log.info("Connection to database succeed");

            String sql = "INSERT INTO konto" + SingletonUser.getInstance().getName() + " VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            try {
                stmt.setDouble(3, kontoVeränderungsÜberprüfer(amountChange, choiceBoxValue));
                log.info("Kontoänderungseingabe erfolgreich");
            } catch (Exception e) {
                log.error("Kontoänderungseingabe hat nicht geklappt");

            }

            try {
                if (note.isEmpty()) {
                    throw new IllegalArgumentException("Grund ist ein Pflichtfeld");
                }
                stmt.setString(2, note);
            } catch (Exception e) {
                log.error("Grund geht nicht");

            }

            try {
                stmt.setDate(1, date);
            } catch (Exception e) {
                log.error("Datum geht nicht");

                return;
            }


            try {
                double neuerKontostand = aktuellerKontostand(amountChange, choiceBoxValue);
                log.info("Neuer Kontostand: " + neuerKontostand);
                stmt.setDouble(4, neuerKontostand);
            } catch (Exception e) {
                log.error("Aktueller Kontostand  konnte nicht aufgerufen werden");
            }


            stmt.setInt(5, sliderValue);

            try {
                stmt.setBoolean(6, repeatBool);
                stmt.setString(7, checkFrequency(repetitionFrequency, repeatBool));
                stmt.executeUpdate();


            } catch (Exception e) {
                log.info("Eingabe konnte nicht hinzugefügt werden");
            }
        } catch (SQLException e) {
            log.error("Couldn't connect to Database", e);
            throw e;
        }
    }


    public String checkFrequency(String repetitionFrequency, Boolean repeatBool) {
        if (repeatBool && repetitionFrequency.equals("täglich")) {
            return "täglich";
        } else if (repeatBool && repetitionFrequency.equals("monatlich")) {
            return "monatlich";
        } else if (repeatBool && repetitionFrequency.equals("jährlich")) {
            return "jährlich";
        } else {
            return null;
        }
    }


    public double aktuellerKontostand(double amountChange, String choiceBoxValue) throws Exception, SQLException {
        try (Connection con = lg.getConnection()) {
            log.info("Connection to database succeed");

            String sql = "SELECT bankBalance FROM konto" + SingletonUser.getInstance().getName() + " ORDER BY edate DESC, id DESC LIMIT 1";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            try {
                while (rs.next()) {
                    double Kontostand = rs.getDouble("bankBalance");

                    log.info(Kontostand);
                    double neuerKontostand = Kontostand + kontoVeränderungsÜberprüfer(amountChange, choiceBoxValue);
                    neuerKontostand = Math.round(neuerKontostand * 100.0) / 100.0;
                    return neuerKontostand;
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

    public double kontoVeränderungsÜberprüfer(double amountChange, String choiceBoxValue) {

        //Methode steht bereits in LogicNew Entry !!!


        if (choiceBoxValue.equals("Einnahme")) {

            log.info(amountChange);
            return amountChange;
        } else {

            log.info(amountChange);
            if (amountChange == 0) {
                return amountChange;
            } else if (amountChange > 0) {
                return -amountChange;
            } else {
                return amountChange;
            }

        }

    }

    public boolean überprüfungDatentypDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean checkingFormats(String amountChange, String note, LocalDate date) throws NoteIsNullException, ParseDoubleException, ParseDateException {
        if (note.equals("") || note == null) {
            throw new NoteIsNullException();
        }
        try {
            Double.parseDouble(amountChange);
        } catch (Exception e) {
            throw new ParseDoubleException();
        }
        try {
            Date.valueOf(date);
        }catch (Exception e){
            throw new ParseDateException();
        }
        return true;
    }
}
