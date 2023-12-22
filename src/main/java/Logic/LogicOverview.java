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
    public ObservableList<LogicTableEntry> datenbank() throws Exception {

        try (Connection con = lg.getConnection()) {
            if (localUsername != null) {
                try {
                    String sql = "SELECT edate, note, amount, bankbalance, importance, isregular FROM konto" + localUsername + " ORDER BY edate DESC, id DESC";

                    PreparedStatement stmt = con.prepareStatement(sql);
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        String datum = (rs.getDate("edate").toString());
                        String grund = (rs.getString("note"));
                        double betrag = (rs.getDouble("amount"));
                        double kontostand = (rs.getDouble("bankbalance"));
                        Integer wichtigkeit = (rs.getInt("importance"));
                        Boolean regelmäßigkeitBool = (rs.getBoolean("isregular"));
                        String regelmäßigkeit;
                        if (regelmäßigkeitBool) {
                            regelmäßigkeit = "Regelmäßig";
                        } else {
                            regelmäßigkeit = "Einmalig";
                        }
                        list.add(new LogicTableEntry(datum, grund, betrag, kontostand, wichtigkeit, regelmäßigkeit));
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
    public int deleteRowInDatabase(double betrag, String datum, String grund, double kontostand, int wichtigkeit, Boolean regelmäßigkeitBool) throws SQLException {
        try (Connection con = lg.getConnection()) {
            log.info("Connection to database succeed");

            String sql = "DELETE FROM konto" + localUsername + " WHERE edate = ? AND note = ? AND amount = ? AND bankbalance = ? AND importance = ? AND isregular = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setDate(1, Date.valueOf(datum));
            stmt.setString(2, grund);
            stmt.setDouble(3, betrag);
            stmt.setDouble(4, kontostand);
            stmt.setInt(5, wichtigkeit);
            stmt.setBoolean(6, regelmäßigkeitBool);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected;
        }catch (SQLException e) {
            log.error("Couldn't connect to Database", e);
            throw e;
        }
    }
    public void saveValues(double betrag, String datum, String grund, double kontostand, int wichtigkeit, String regelmäßigkeit){
        SingletonEditValues sev = SingletonEditValues.getInstance();
        sev.setAmount(betrag);
        sev.setDate(datum);
        sev.setNote(grund);
        sev.setBankbalance(kontostand);
        sev.setImportance(wichtigkeit);
        sev.setIsregular(regelmäßigkeit);
    }
}
