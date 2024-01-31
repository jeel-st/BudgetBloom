package Logic;

import Singleton.SingletonEditValues;
import Singleton.SingletonUser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;

public class LogicEditEntry extends LogicSuperClass{

    private final LogicDatabase dc = new LogicDatabase();
    private final SingletonUser sp = SingletonUser.getInstance();
    private final SingletonEditValues sev = SingletonEditValues.getInstance();

    private static final Logger log = LogManager.getLogger(LogicEditEntry.class);


    String saveEdit(LocalDate inputDate, String inputReason, int scale, String repeatBox, String inputNumber, String myChoiceBox, String repeatabilityBox, String payment) throws Exception{
        String errorLabel;

        try (Connection con = dc.getConnection()) {
            log.debug("Connection to database succeed");

            String sql = "UPDATE konto" + sp.getName() + " SET edate = ?, note = ?, amount = ?, importance = ? , isregular = ?, frequency = ?, payment = ? WHERE edate= ? AND note = ? AND amount = ? AND bankbalance = ? AND isregular = ? ";
            PreparedStatement stmt = con.prepareStatement(sql);
            try {
                stmt.setDate(1, Date.valueOf(inputDate));
            } catch (Exception e) {
                log.warn("date is wrong");
                errorLabel = "Bitte fügen Sie ein Datum hinzu!";
                return errorLabel;
            }

            if (!inputReason.isEmpty()) {
                stmt.setString(2, inputReason);
            } else {
                log.warn("note is empty");
                errorLabel = "Bitte fügen Sie einen Grund hinzu!";
                return errorLabel;
            }

            try {
                stmt.setDouble(3, LogicFacade.getInstance().accountChangeChecker(Double.parseDouble(inputNumber), myChoiceBox));
                log.info("Account change entry successful");
            } catch (Exception e) {
                log.warn("Account change does not work");
                errorLabel = "Bitte achten Sie bei der Einahme/Ausgabe auf das vorgegebene Format (xxx.xx)!";
                return errorLabel;
            }


            stmt.setInt(4, scale);
            stmt.setBoolean(5, isRegularBool(repeatBox));
            stmt.setString(6, checkFrequency(repeatBox, repeatabilityBox));
            try {
                stmt.setString(7, super.checkPayment(payment, Double.parseDouble(inputNumber)));
            }catch (Exception e){
                log.warn("Your payment method wasn't accepted.");
            }
            log.debug(sev.getDate());
            stmt.setDate(8, Date.valueOf(sev.getDate()));
            stmt.setString(9, sev.getNote());
            stmt.setDouble(10, sev.getAmount());
            stmt.setDouble(11, sev.getAccountBalance());
            stmt.setBoolean(12, isRegularBool(sev.getIsRegular()));
            stmt.executeUpdate();

            errorLabel = "Edit was saved successfully";
            return errorLabel;
        } catch (SQLException e) {
            log.error("Couldn't connect to Database", e);
            errorLabel = "Lost connection to database";
            return errorLabel;
        }


    }


    String showContentOfRepeatabilityBox() throws Exception {

        try (Connection con = dc.getConnection()) {
            log.debug("Connection to database succeed");
            String sql = "SELECT frequency FROM konto" + sp.getName() + " WHERE edate = ? AND note = ? AND amount = ? AND bankBalance = ? AND importance = ? AND isregular = ?";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {

                stmt.setDate(1, Date.valueOf(sev.getDate()));
                stmt.setString(2, sev.getNote());
                stmt.setDouble(3, sev.getAmount());
                stmt.setDouble(4, sev.getAccountBalance());
                stmt.setInt(5, sev.getImportance());
                stmt.setBoolean(6, isRegularBool(sev.getIsRegular()));

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String s = rs.getString("frequency");
                        log.info("The frequency found is: "+s);
                        return s;
                    } else {
                        log.warn("No suitable data set was found");
                        throw new Exception();
                    }
                }
            } catch (Exception e) {
                log.error("Database does not work", e);
                throw e;
            }
        } catch (SQLException e) {
            log.error("Error closing connection", e);
            throw e;
        }
    }
}
