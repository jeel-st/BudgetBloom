package Logic;

import Controller.ControllerOverview;
import Singleton.SingletonUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class LogicBalance {

    public static Logger log = LogManager.getLogger(ControllerOverview.class);

    public static void updateBalance() {
        SingletonUser sp = SingletonUser.getInstance();
        String localUsername = sp.getName();
        LogicDatabase dc = new LogicDatabase();
        try(Connection con = dc.getConnection()){
            try {
                String sql = "SELECT id, amount, bankbalance FROM konto" + localUsername + " ORDER BY edate ASC, id ASC";

                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();

                int id2;
                double betrag2, kontostand1 = 0;
                int i = 1;

                while (rs.next()) {
                    if (i == 1) {
                        kontostand1 = (rs.getDouble("bankbalance"));
                    } else {
                        id2 = (rs.getInt("id"));
                        betrag2 = (rs.getDouble("amount"));

                        double neuerKontostand2 = kontostand1 + betrag2;

                        try {
                            String sql2 = "UPDATE konto" + localUsername + " SET bankbalance = ? WHERE id = ?";
                            PreparedStatement stmt2 = con.prepareStatement(sql2);
                            stmt2.setDouble(1, neuerKontostand2);
                            stmt2.setInt(2, id2);
                            stmt2.executeQuery();
                        } catch (SQLException e) {
                            //exception fliegt immer, da die UPDATE Abfrage kein Ergebnis liefert
                            log.trace("bankbalance updated successfully");
                        }

                        kontostand1 = neuerKontostand2;
                    }

                    i++;

                }
                rs.close();
            } catch (Exception e) {
                log.error("Failed to transfer data from database");
            }
        } catch (SQLException e) {
            log.error("Couldn't connect to Database");
        }




    }
}
