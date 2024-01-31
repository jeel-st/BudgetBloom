package Logic;

import Singleton.SingletonUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

 public class LogicLogin {

    private final LogicDatabase dc = new LogicDatabase();
    private final SingletonUser sp = SingletonUser.getInstance();
    private static final Logger log = LogManager.getLogger(LogicLogin.class);

    boolean isValidUser(String username, String password) {
        try(Connection con = dc.getConnection()) {
            log.debug("Connection to database succeed");
            try {
                String sql = "SELECT * FROM users WHERE username = ? AND pword = ?";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setString(1, username);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    log.debug("Username " + username + " found in database");
                    updateLDate();
                    updateNumLogin(username);
                    return true;
                } else {
                    log.warn("Wrong username or password");
                    return false;
                }

            } catch (SQLException e ) {
                log.error("SQL query failed", e);
            }

        } catch (SQLException e) {
            log.error("Couldn't connect to Database", e);
        }
        return false;
    }

    private void updateLDate() {
        try(Connection con = dc.getConnection()) {
            log.debug("Connection to database succeed");
            try {
                String sql2 = "UPDATE users SET ldate = CURRENT_DATE WHERE username = ?";
                PreparedStatement stmt2 = con.prepareStatement(sql2);
                stmt2.setString(1, sp.getName());
                stmt2.executeQuery();

            } catch (SQLException e) {
                //exception fliegt immer, da die UPDATE Abfrage kein Ergebnis liefert
                log.debug("last login date updated successfully");
            }
        } catch (SQLException e) {
            log.error("Couldn't connect to Database", e);
        }
    }

    private void updateNumLogin(String username) {
        try(Connection con = dc.getConnection()) {
            log.debug("Connection to database succeed");
            try {
                String sql3 = "UPDATE users SET numlogin = numlogin + 1 WHERE username = ?";
                PreparedStatement stmt3 = con.prepareStatement(sql3);
                stmt3.setString(1, username);
                stmt3.executeQuery();
            } catch (SQLException e) {
                //exception fliegt immer, da die UPDATE Abfrage kein Ergebnis liefert
                log.debug("number of logins updated successfully");
            }
        } catch (SQLException e) {
            log.error("Couldn't connect to Database", e);
        }
    }

    boolean isFirstLogin(String username) {
        try(Connection con = dc.getConnection()) {
            log.debug("Connection to database succeed");
            try {
                String sql4 = "SELECT * FROM users WHERE username = ? AND numlogin = 1";
                PreparedStatement stmt4 = con.prepareStatement(sql4);
                stmt4.setString(1, username);
                ResultSet rs2 = stmt4.executeQuery();
                return rs2.next();
            } catch (SQLException e) {
                log.error("SQL Exception while finding out if is first login", e);
            }
        } catch (SQLException e) {
            log.error("Couldn't connect to Database", e);
        }
        return false;
    }
}
