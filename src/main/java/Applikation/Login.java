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

    public void userLogin(ActionEvent event) throws IOException, SQLException {
        checkLogin();
    }

    public void registerButton(ActionEvent event) throws IOException,SQLException{
        checkRegister();
    }

    public void checkRegister()throws IOException{

        d.changeScene("/FXML/register.fxml");
    }

    public void checkLogin() throws IOException, SQLException {


        String url = "jdbc:postgresql://foo.mi.hdm-stuttgart.de/js486";
        String pass = "(JJS)2003";
        String user = "js486";

        Connection con = DriverManager.getConnection(url, user, pass);
        if (username.getText().isEmpty() && password.getText().isEmpty()) {
            wrongLogin.setText("Please enter your data.");
        } else {
            try {
                String sql = "SELECT * FROM users WHERE username = ? AND pword = ?";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setString(1, String.valueOf(username));
                stmt.setString(2, String.valueOf(password));
                ResultSet rs = stmt.executeQuery();


            } catch (  SQLException e ) {
                wrongLogin.setText("Wrong username or password.");
            }
        }

    }


       /* if(username.getText().toString().equals("javacoding") && password.getText().toString().equals("123456789")){
            wrongLogin.setText("Success!");

            d.changeScene("/FXML/übersicht.fxml");
        }else if(username.getText().isEmpty() && password.getText().isEmpty()){
            wrongLogin.setText("Please enter your data.");
        }else {
            wrongLogin.setText("Wrong username or password.");
        } */


    }
