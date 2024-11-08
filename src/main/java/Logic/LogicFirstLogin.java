package Logic;

import Singleton.SingletonUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class LogicFirstLogin {
    private final LogicDatabase dc = new LogicDatabase();
    private final SingletonUser sp = SingletonUser.getInstance();
    private static final Logger log = LogManager.getLogger(LogicFirstLogin.class);

     public void insertInitialBalance(double balance) {
        try(Connection con = dc.getConnection()) {
            String sql = "INSERT INTO konto" + sp.getName() + " VALUES (DEFAULT, DEFAULT, 'initial konto balance', ?, ?, 10, DEFAULT, NULL, NULL)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setDouble(1, balance);
            stmt.setDouble(2, balance);
            stmt.execute();
            log.info("Initial balance inserted successfully");
        } catch (SQLException e) {
            log.error("Couldn't connect to Database", e);
        }
    }

     public boolean isBalanceNumber(String balance) {
        String regex = "^[-]?[0-9]+([.][0-9][0-9]?)?$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(balance);
        return matcher.find();
    }

}
