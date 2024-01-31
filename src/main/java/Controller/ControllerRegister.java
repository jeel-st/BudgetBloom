package Controller;

import Logic.LogicFacade;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import mainpackage.Driver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class ControllerRegister {
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
    private static final Logger log = LogManager.getLogger(ControllerRegister.class);
    private final Driver d = new Driver();

    public void userToLogin(ActionEvent event) throws IOException {
        d.changeScene("/FXML/login.fxml");
    }

    public void userCreate(ActionEvent event) throws IOException{
        log.info("User creation started");
        String LabelText = LogicFacade.getInstance().proveRegisterTextFields(username.getText().toLowerCase(),password.getText(), password2.getText(),email.getText());
        log.info(LabelText);
        if(!(LabelText.equals("completed"))){
            wrongRegister.setText(LabelText);
        } else {
            d.changeScene("/FXML/login.fxml");
        }
    }
}
