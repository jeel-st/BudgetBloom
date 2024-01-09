package Logic;

import Controller.ControllerLogin;
import Singleton.SingletonUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
class LogicFirstLogin {
    LogicDatabase dc = new LogicDatabase();
    SingletonUser sp = SingletonUser.getInstance();
    private String localUsername = sp.getName();
    public static Logger log = LogManager.getLogger(LogicFirstLogin.class);

     void insertInitialBalance(double balance) {

        try(Connection con = dc.getConnection()) {
            String sql = "INSERT INTO konto" + localUsername + " VALUES (DEFAULT, DEFAULT, 'initial konto balance', ?, ?, 10)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setDouble(1, balance);
            stmt.setDouble(2, balance);
            stmt.execute();
            log.info("Initial balance inserted successfully");
        } catch (SQLException e) {
            log.error("Couldn't connect to Database", e);
        }
    }

     boolean isBalanceNumber(String balance) {
        String regex = "^[-]?[0-9]+([.][0-9][0-9]?)?$";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(balance);

        return matcher.find();
    }

}
