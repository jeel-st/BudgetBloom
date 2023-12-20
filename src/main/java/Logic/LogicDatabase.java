package Logic;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LogicDatabase {



    private final String URL = "jdbc:postgresql://foo.mi.hdm-stuttgart.de/js486";
    private final String USER = "js486";
    private final String PASSWORD = "(JJS)2003ab";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }


}
