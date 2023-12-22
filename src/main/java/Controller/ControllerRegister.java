package Controller;

import Logic.LogicDatabase;
import Logic.LogicRegister;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public PasswordField password;
    @FXML
    public Label wrongRegister;
    @FXML
    public PasswordField password2;
    public static Logger log = LogManager.getLogger(ControllerRegister.class);
    Driver d = new Driver();
    LogicDatabase dc = new LogicDatabase();
    LogicRegister lg = new LogicRegister();

    public void userToLogin(ActionEvent event) throws IOException {
        d.changeScene("/FXML/sample.fxml");
    }

    public void userCreate(ActionEvent event) throws IOException, SQLException {
        log.info("User creation started");
        String LabelText = lg.proveRegisterTextFields(username.getText(), password.getText(), password2.getText(), email.getText());
        log.info(LabelText);
        if(!(LabelText.equals("completed"))){
            wrongRegister.setText(LabelText);
        }else {
            d.changeScene("/FXML/sample.fxml");
        }

    }


}
