package Controller;

import Logic.LogicFacade;
import Logic.LogicFirstLogin;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import mainpackage.Driver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;

public class ControllerFirstLogin extends Application {
    @FXML
    public Button ok;
    @FXML
    public Label welcomeText;
    @FXML
    public Label labelBalance;
    @FXML
    public Label wrongBalance;
    @FXML
    public TextField balanceField;
    @FXML
    public Button skip;
    @FXML
    public Label skipText;
    @FXML
    public Image image;
    private final Driver d = new Driver();
    public static Logger log = LogManager.getLogger(ControllerLogin.class);

    @Override
    public void start(Stage stage) throws Exception {
        Parent par = FXMLLoader.load(getClass().getResource("/resources/FXML/firstLogin.fxml"));
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML private void skipBalance(ActionEvent event) throws IOException {
        LogicFacade.getInstance().insertInitialBalance(0);
        d.changeScene("/FXML/overview.fxml");
    }

    @FXML private void checkBalance(ActionEvent event) throws IOException {
        String balanceString = balanceField.getText();
        if (LogicFacade.getInstance().isBalanceNumber(balanceString)) {
            double balance = Double.parseDouble(balanceString);
            log.debug("Found Double is " + balance);
            LogicFacade.getInstance().insertInitialBalance(balance);
            d.changeScene("/FXML/overview.fxml");
        } else {
            wrongBalance.setText("Please enter a number in one of the following formats: xxxxx.yy or xxx.y or xxx");
        }
    }
}
