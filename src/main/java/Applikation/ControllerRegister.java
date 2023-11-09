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

public class ControllerRegister {

    @FXML
    private Button back_to_login;
    @FXML
    private Button create_account;
    @FXML
    public TextField email;
    @FXML
    public TextField username;
    @FXML
    public TextField password;
    @FXML
    private Label wrongRegister;

    Driver d = new Driver();

    public void userToLogin(ActionEvent event) throws IOException{
        d.changeScene("/FXML/sample.fxml");
    }

    public void userCreate(ActionEvent event) throws IOException{

    }

}
