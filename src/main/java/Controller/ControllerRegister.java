package Controller;

import Logic.LogicDatabase;
import Logic.LogicFacade;
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

public class ControllerRegister {


    @FXML
    private Button backToLogin;
    @FXML
    private Button createAccount;
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


    public void userToLogin(ActionEvent event) throws IOException {
        d.changeScene("/FXML/sample.fxml");
    }

    public void userCreate(ActionEvent event) throws IOException{
        log.info("User creation started");
        String LabelText = LogicFacade.getInstance().proveRegisterTextFields(username.getText(),password.getText(), password2.getText(),email.getText());
        log.info(LabelText);
        if(!(LabelText.equals("completed"))){
            wrongRegister.setText(LabelText);
        }else {
            d.changeScene("/FXML/sample.fxml");
        }

    }


}
