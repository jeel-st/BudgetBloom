package Applikation;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import mainpackage.Driver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class FirstLogin extends Application {
    @FXML
    public Button ok;
    @FXML
    public Label welcomeText;
    @FXML
    public Label labelBalance;
    @FXML
    public TextField balanceField;
    @FXML
    public Button skip;
    @FXML
    public Label skipText;
    @FXML
    public Image image;
    Driver d = new Driver();

    public static Logger log = LogManager.getLogger(Login.class);



    @Override
    public void start(Stage stage) throws Exception {
        Parent par = FXMLLoader.load(getClass().getResource("/resources/FXML/firstLogin.fxml"));
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root);
        //Image image = new Image(getClass().getResourceAsStream("LoginFoto.jpg"));
        //Button skip = new Button();
        //root.setLeft(image);
        //root.setBottom(skip);
        stage.setScene(scene);
        stage.show();

    }

    @FXML private void skipBalance(ActionEvent event) throws IOException {
        d.changeScene("/FXML/übersicht.fxml");
    }

    @FXML private void checkBalance(ActionEvent event) throws IOException {
        //TODO
        d.changeScene("/FXML/übersicht.fxml");
    }
}
