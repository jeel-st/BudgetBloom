package Logic;

import Controller.ControllerLogin;
import Singleton.SingletonUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

 class LogicLogin {

    LogicDatabase dc = new LogicDatabase();
    SingletonUser sp = SingletonUser.getInstance();
    private final String localUsername = sp.getName();
    private static Logger log = LogManager.getLogger(ControllerLogin.class);

    boolean isValidUser(String username, String password) {
        try(Connection con = dc.getConnection()) {
            log.info("Connection to database succeed");
            try {
                String sql = "SELECT * FROM users WHERE username = ? AND pword = ?";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setString(1, username);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    log.info("Username " + localUsername + " found in database");
                    updateLDate();
                    updateNumLogin();
                    return true;

                } else {
                    log.warn("Wrong username or password");
                    return false;
                }

            } catch (SQLException e ) {
                log.error("SQL query failed");
            }

        } catch (SQLException e) {
            log.error("Couldn't connect to Database");
        }
        return false;
    }

    private void updateLDate() {
        try(Connection con = dc.getConnection()) {
            log.info("Connection to database succeed");
            try {
                String sql2 = "UPDATE users SET ldate = CURRENT_DATE WHERE username = ?";

                PreparedStatement stmt2 = con.prepareStatement(sql2);

                stmt2.setString(1, localUsername);

                stmt2.executeQuery();

            } catch (SQLException e) {
                //exception fliegt immer, da die UPDATE Abfrage kein Ergebnis liefert
                log.info("last login date updated successfully");

            }
        } catch (SQLException e) {
            log.error("Couldn't connect to Database");
        }
    }

    private void updateNumLogin() {
        try(Connection con = dc.getConnection()) {
            log.info("Connection to database succeed");
            try {
                String sql3 = "UPDATE users SET numlogin = numlogin + 1 WHERE username = ?";
                PreparedStatement stmt3 = con.prepareStatement(sql3);
                stmt3.setString(1, localUsername);
                stmt3.executeQuery();
            } catch (SQLException e) {
                //exception fliegt immer, da die UPDATE Abfrage kein Ergebnis liefert
                log.info("number of logins updated successfully");
            }
        } catch (SQLException e) {
            log.error("Couldn't connect to Database");
        }
    }

    boolean isFirstLogin() {
        try(Connection con = dc.getConnection()) {
            log.info("Connection to database succeed");
            try {
                String sql4 = "SELECT * FROM users WHERE username = ? AND numlogin = 1";
                PreparedStatement stmt4 = con.prepareStatement(sql4);
                stmt4.setString(1, localUsername);
                ResultSet rs2 = stmt4.executeQuery();
                return rs2.next();
            } catch (SQLException e) {
                log.error("SQL Exception " + e + " while finding out if is first login");
            }
        } catch (SQLException e) {
            log.error("Couldn't connect to Database");
        }
        return false;
    }
}
