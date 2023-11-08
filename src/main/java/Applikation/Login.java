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
import java.sql.SQLException;

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

    public void registerButton(ActionEvent event) throws IOException{
        d.changeScene("/FXML/register.fxml");
    }

    public void checkLogin() throws IOException, SQLException {





        if(username.getText().toString().equals("javacoding") && password.getText().toString().equals("123456789")){
            wrongLogin.setText("Success!");

            d.changeScene("/FXML/übersicht.fxml");
        }else if(username.getText().isEmpty() && password.getText().isEmpty()){
            wrongLogin.setText("Please enter your data.");
        }else {
            wrongLogin.setText("Wrong username or password.");
        }


    }

}
