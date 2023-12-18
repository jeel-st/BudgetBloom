package Applikation;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatenbankConnector {



    private static final String URL = "jdbc:postgresql://foo.mi.hdm-stuttgart.de/js486";
    private static final String USER = "js486";
    private static final String PASSWORD = "(JJS)2003ab";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }


}
