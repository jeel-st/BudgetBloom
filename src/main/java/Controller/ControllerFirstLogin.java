package Controller;


import Logic.LogicDatabase;
import Singleton.SingletonUser;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    Driver d = new Driver();
    LogicDatabase dc = new LogicDatabase();
    SingletonUser sp = SingletonUser.getInstance();
    private String localUsername = sp.getName();
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
        insertInitialBalance(0);
        d.changeScene("/FXML/overview.fxml");
    }

    @FXML private void checkBalance(ActionEvent event) throws IOException {
        String balanceString = balanceField.getText();
        if (isBalanceNumber(balanceString)) {
            double balance = Double.parseDouble(balanceString);
            log.debug("Found Double is " + balance);
            insertInitialBalance(balance);
            d.changeScene("/FXML/overview.fxml");

        } else {
            wrongBalance.setText("Please enter a number in one of the following formats: xxxxx.yy or xxx.y or xxx");
        }





    }

    private void insertInitialBalance(double balance) {

        try(Connection con = dc.getConnection()) {
            String sql = "INSERT INTO konto" + localUsername + " VALUES (DEFAULT, DEFAULT, 'initial konto balance', ?, ?, 10)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setDouble(1, balance);
            stmt.setDouble(2, balance);
            stmt.execute();
            log.info("Initial balance inserted successfully");
        } catch (SQLException e) {
            log.error("Couldn't connect to Database", e);
        }
    }

    private boolean isBalanceNumber(String balance) {
        String regex = "^[-]?[0-9]+([.][0-9][0-9]?)?$";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(balance);

        if (matcher.find()) {
            return true;
        } else {
            return false;
        }
    }
}
