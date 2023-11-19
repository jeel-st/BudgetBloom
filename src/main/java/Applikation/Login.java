package Applikation;

import Datenbank.Kontodaten;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import mainpackage.Driver;

import java.io.IOException;
import java.sql.*;

public class Login {

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

    public void userLogin(ActionEvent event) throws SQLException, ClassNotFoundException{
        checkLogin();
    }

    public void registerButton(ActionEvent event) throws IOException,SQLException{
        checkRegister();
    }

    public void checkRegister()throws IOException{

        d.changeScene("/FXML/register.fxml");
    }

    public void checkLogin() throws SQLException {


        String url = "jdbc:postgresql://foo.mi.hdm-stuttgart.de/js486";
        String pass = "(JJS)2003ab";
        String user = "js486";

        Connection con = DriverManager.getConnection(url, user, pass);
        if (username.getText().isEmpty() || password.getText().isEmpty()) {
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
                    try {
                        String sql2 = "UPDATE users SET ldate = CURRENT_DATE WHERE username = ?";
                        PreparedStatement stmt2 = con.prepareStatement(sql2);
                        stmt2.setString(1, username.getText());
                        stmt2.executeQuery();
                    } catch (SQLException e) {

                    }


                    d.changeScene("/FXML/übersicht.fxml");
                } else {
                    wrongLogin.setText("Wrong username or password.");
                }


            } catch (SQLException e ) {
                throw new SQLException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }


    }
