package Logic;

import Singleton.SingletonEditValues;
import Singleton.SingletonUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class LogicOverview {
    public static Logger log = LogManager.getLogger(LogicOverview.class);
    LogicDatabase lg = new LogicDatabase();
    SingletonUser su = SingletonUser.getInstance();
    private final String localUsername = su.getName();
    ObservableList<LogicTableEntry> list = FXCollections.observableArrayList(
            //new User("17.11.2023", "Lebensmittel", -12.3, 123.45)
    );
    public ObservableList<LogicTableEntry> database() throws Exception {

        try (Connection con = lg.getConnection()) {
            if (localUsername != null) {
                try {
                    String sql = "SELECT edate, note, amount, bankbalance, importance, isregular FROM konto" + localUsername + " ORDER BY edate DESC, id DESC";

                    PreparedStatement stmt = con.prepareStatement(sql);
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        String date = (rs.getDate("edate").toString());
                        String reason = (rs.getString("note"));
                        double amount = (rs.getDouble("amount"));
                        double accountBalance = (rs.getDouble("bankbalance"));
                        Integer importance = (rs.getInt("importance"));
                        Boolean regularityBool = (rs.getBoolean("isregular"));
                        String regularity;
                        if (regularityBool) {
                            regularity = "Regelmäßig";
                        } else {
                            regularity = "Einmalig";
                        }
                        list.add(new LogicTableEntry(date, reason, amount, accountBalance, importance, regularity));
                    }
                    return list;
                } catch (Exception e) {
                    log.error("Failed to transfer data from database");
                }
            } else {
                log.error("Username is null");
            }
        }catch (SQLException e) {
            log.error("Couldn't connect to Database", e);
            throw e;
        }
        throw new Exception("Searching for data failed");
    }
    public int deleteRowInDatabase(double amount, String date, String reason, double accountBalance, int importance, Boolean regularityBool) throws SQLException {
        try (Connection con = lg.getConnection()) {
            log.info("Connection to database succeed");

            String sql = "DELETE FROM konto" + localUsername + " WHERE edate = ? AND note = ? AND amount = ? AND bankbalance = ? AND importance = ? AND isregular = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setDate(1, Date.valueOf(date));
            stmt.setString(2, reason);
            stmt.setDouble(3, amount);
            stmt.setDouble(4, accountBalance);
            stmt.setInt(5, importance);
            stmt.setBoolean(6, regularityBool);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected;
        }catch (SQLException e) {
            log.error("Couldn't connect to Database", e);
            throw e;
        }
    }
    public void saveValues(double amount, String date, String reason, double accountBalance, int importance, String regularity){
        SingletonEditValues sev = SingletonEditValues.getInstance();
        sev.setAmount(amount);
        sev.setDate(date);
        sev.setNote(reason);
        sev.setAccountBalance(accountBalance);
        sev.setImportance(importance);
        sev.setIsRegular(regularity);
    }
}
