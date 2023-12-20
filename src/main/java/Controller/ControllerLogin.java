package Controller;

import Logic.LogicDatabase;
import Singleton.SingletonUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import mainpackage.Driver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.*;

public class ControllerLogin {

    @FXML
    private Button button;
    @FXML
    private Label wrongLogin;
    @FXML
    public TextField username;
    @FXML
    public PasswordField password;
    @FXML
    private Button register;
    Driver d = new Driver();
    LogicDatabase dc = new LogicDatabase();
    public static Logger log = LogManager.getLogger(ControllerLogin.class);
    public void userLogin(ActionEvent event) throws SQLException, ClassNotFoundException{
        log.info("Login button pushed");
        setLocalUsername();
        checkLogin();

    }

    public void registerButton(ActionEvent event) throws IOException,SQLException{
        log.info("Registration button pushed");
        checkRegister();
    }
    public String localUsername;
    public void checkRegister()throws IOException{
        log.info("Change scene to registration");
        d.changeScene("/FXML/register.fxml");
    }

    public void setLocalUsername(){
        SingletonUser s = SingletonUser.getInstance();
        s.setName(username.getText());
        localUsername = s.getName();
    }
    public void checkLogin() throws SQLException {

        try(Connection con = dc.getConnection()) {

            log.info("Connection to database succeed");
            if (username.getText().isEmpty() || password.getText().isEmpty()) {
                log.warn("Password or username is empty");
                wrongLogin.setText("Please enter your username and password.");
            } else {
                try {
                    String sql = "SELECT * FROM users WHERE username = ? AND pword = ?";
                    PreparedStatement stmt = con.prepareStatement(sql);
                    stmt.setString(1, username.getText());
                    stmt.setString(2, password.getText());
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        wrongLogin.setText("Success!");
                        log.info("Username " + localUsername + " found in database");
                        try {
                            String sql2 = "UPDATE users SET ldate = CURRENT_DATE WHERE username = ?";

                            PreparedStatement stmt2 = con.prepareStatement(sql2);

                            stmt2.setString(1, localUsername);

                            stmt2.executeQuery();

                        } catch (SQLException e) {
                            //exception fliegt immer, da die UPDATE Abfrage kein Ergebnis liefert
                            log.info("last login date updated successfully");

                        }
                        try {
                            String sql3 = "UPDATE users SET numlogin = numlogin + 1 WHERE username = ?";
                            PreparedStatement stmt3 = con.prepareStatement(sql3);
                            stmt3.setString(1, localUsername);
                            stmt3.executeQuery();
                        } catch (SQLException e) {
                            //exception fliegt immer, da die UPDATE Abfrage kein Ergebnis liefert
                            log.info("number of logins updated successfully");
                        }
                        try {
                            String sql4 = "SELECT * FROM users WHERE username = ? AND numlogin = 1";
                            PreparedStatement stmt4 = con.prepareStatement(sql4);
                            stmt4.setString(1, localUsername);
                            ResultSet rs2 = stmt4.executeQuery();
                            if (rs2.next()) {
                                log.info("Scene changed to firstLogin.fxml successfully");
                                d.changeScene("/FXML/firstLogin.fxml");
                            } else {
                                log.info("Scene changed to overview.fxml successfully");
                                d.changeScene("/FXML/overview.fxml");
                            }
                        } catch (SQLException e) {
                            log.error("SQL Exception " + e + " while finding out if is first login");
                        }



                    } else {
                        log.warn("Wrong username or password");
                        wrongLogin.setText("Wrong username or password.");
                    }


                } catch (SQLException e ) {
                    log.error("SQL query failed");
                    throw new SQLException(e);
                } catch (IOException e) {
                    log.error("JavaFX failed");
                    throw new RuntimeException(e);
                }
            }

        }catch (SQLException e) {
            log.error("Couldn't connect to Database", e);
            throw e;
        }
    }
}