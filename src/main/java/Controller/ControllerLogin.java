package Controller;

import Logic.LogicDatabase;
import Logic.LogicFacade;
import Logic.LogicFirstLogin;
import Logic.LogicLogin;
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

public class ControllerLogin {

    @FXML
    private Button login;
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
    private static Logger log = LogManager.getLogger(ControllerLogin.class);
    public String localUsername;

    @FXML public void userLogin(ActionEvent event) throws IOException {


        if (username.getText().isEmpty() || password.getText().isEmpty()) {
            log.warn("Password or username is empty");
            wrongLogin.setText("Please enter your username and password.");

        } else {

            if (LogicFacade.getInstance().isValidUser(username.getText(), password.getText())) {
                wrongLogin.setText("Success!");
                setLocalUsername();

                if (LogicFacade.getInstance().isFirstLogin(username.getText())) {
                    log.debug("Scene changed to firstLogin.fxml successfully");
                    d.changeScene("/FXML/firstLogin.fxml");

                } else {
                    log.debug("Scene changed to overview.fxml successfully");
                    d.changeScene("/FXML/overview.fxml");
                }

            } else {
                wrongLogin.setText("Wrong username or password.");
            }
        }
    }


    @FXML public void registerButton(ActionEvent event) throws IOException {

        log.debug("Change scene to registration");
        d.changeScene("/FXML/register.fxml");
    }
    public void setLocalUsername(){
        SingletonUser s = SingletonUser.getInstance();
        s.setName(username.getText());
        localUsername = s.getName();
    }
}
