package Logic;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LogicDatabase {

    public Connection getConnection() throws SQLException {
        String URL = "jdbc:postgresql://foo.mi.hdm-stuttgart.de/js486";
        String USER = "js486";
        String PASSWORD = "(JJS)2003ab";
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }


}
