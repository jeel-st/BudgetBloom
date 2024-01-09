package Logic;

import Controller.ControllerEditEntry;
import Interfaces.Payment;
import PaymentMethod.PaymentFactory;
import Singleton.SingletonEditValues;
import Singleton.SingletonUser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;

public class LogicEditEntry extends LogicSuperClass{

    LogicDatabase dc = new LogicDatabase();
    SingletonUser sp = SingletonUser.getInstance();
    SingletonEditValues sev = SingletonEditValues.getInstance();
    String date = sev.getDate();
    String note = sev.getNote();
    double bankBalance = sev.getAccountBalance();
    double amount = sev.getAmount();
    int importance = sev.getImportance();
    String isRegular = sev.getIsRegular();
    private final String localUsername = sp.getName();
    public static Logger log = LogManager.getLogger(LogicEditEntry.class);


    public String saveEdit(LocalDate inputDate, String inputReason, int scale, String repeatBox, String inputNumber, String myChoiceBox, String repeatabilityBox, String payment) {
        String errorLabel;

        try (Connection con = dc.getConnection()) {
            log.info("Connection to database succeed");

            String sql = "UPDATE konto" + localUsername + " SET edate = ?, note = ?, amount = ?, importance = ? , isregular = ?, frequency = ?, payment = ? WHERE edate= ? AND note = ? AND amount = ? AND bankbalance = ? AND isregular = ? ";
            PreparedStatement stmt = con.prepareStatement(sql);
            try {
                stmt.setDate(1, Date.valueOf(inputDate));
            } catch (Exception e) {
                log.error("Datum geht nicht");
                errorLabel = "Bitte fügen Sie ein Datum hinzu!";
                return errorLabel;
            }

            if (!inputReason.isEmpty()) {
                stmt.setString(2, inputReason);
            } else {
                log.error("Grund leer");
                errorLabel = "Bitte fügen Sie einen Grund hinzu!";
                return errorLabel;
            }

            try {
                stmt.setDouble(3, LogicFacade.getInstance().accountChangeChecker(Double.parseDouble(inputNumber), myChoiceBox));
                log.info("Kontoänderungseingabe erfolgreich");
            } catch (Exception e) {
                log.error("Kontoänderung geht nicht");
                errorLabel = "Bitte achten Sie bei der Einahme/Ausgabe auf das vorgegebene Format (xxx.xx)!";
                return errorLabel;
            }


            stmt.setInt(4, scale);
            stmt.setBoolean(5, isRegularBool(repeatBox));
            stmt.setString(6, checkFrequency(repeatBox, repeatabilityBox));
            stmt.setString(7, super.checkPayment(payment, Double.parseDouble(inputNumber)));
            stmt.setDate(8, Date.valueOf(date));
            stmt.setString(9, note);
            stmt.setDouble(10, amount);
            stmt.setDouble(11, bankBalance);
            stmt.setBoolean(12, isRegularBool(isRegular));
            stmt.executeUpdate();

            errorLabel = "Edit was saved successfully";
            return errorLabel;
        } catch (SQLException e) {
            log.error("Couldn't connect to Database");
            errorLabel = "Lost connection to database";
            return errorLabel;
        }


    }


    public String showContentOfRepeatabilityBox() throws Exception {

        try (Connection con = dc.getConnection()) {
            log.info("Connection to database succeed");
            log.info("Der gespeicherte Wert lautet: "+isRegular);
            log.info("Die gespeicherte Ausgabe lautet: "+ amount);
            log.info(isRegularBool(isRegular));
            String sql = "SELECT frequency FROM konto" + localUsername + " WHERE edate = ? AND note = ? AND amount = ? AND bankBalance = ? AND importance = ? AND isregular = ?";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {

                stmt.setDate(1, Date.valueOf(date));
                stmt.setString(2, note);
                stmt.setDouble(3, amount);
                stmt.setDouble(4, bankBalance);
                stmt.setInt(5, importance);
                stmt.setBoolean(6, isRegularBool(isRegular));

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String s = rs.getString("frequency");
                        log.info("Die gefundene Frequenz lautet: "+s);
                        return s;
                    } else {
                        log.error("Es wurde kein passender Datensatz gefunden");
                        throw new Exception();
                    }
                }
            } catch (Exception e) {
                log.error("Datenbank funktioniert nicht", e);
                throw e;
            }
        } catch (SQLException e) {
            log.error("Error closing connection", e);
            throw e;
        }
    }
}
