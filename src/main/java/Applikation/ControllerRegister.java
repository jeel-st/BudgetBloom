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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

    Driver d = new Driver();

    public void userToLogin(ActionEvent event) throws IOException{
        d.changeScene("/FXML/sample.fxml");
    }

    public void userCreate(ActionEvent event) throws IOException{
        if (passwordControl()) {
            newUserEntry();
            d.changeScene("/FXML/sample.fxml");
        }

    }

    public boolean passwordControl() throws IOException {
        if (!(password.getText().toString().equals(password2.getText().toString()))) {
            wrongRegister.setText("Passwords do not match");
            return false;
        } else if (password.getText().toString().length() <= 6) {
            wrongRegister.setText("Password must be longer than 6 characters");
            return false;
        } else {

            String passwordText = password.getText().toString();
            String regex = "[^a-zA-Z0-9]";

            Pattern pattern = Pattern.compile(regex);

            Matcher matcher = pattern.matcher(passwordText);

            if (matcher.find()) {
                return true;

            } else {
                wrongRegister.setText("The password contains no special characters");
                return false;
            }
        }
    }

    public void newUserEntry() {
        String url = "jdbc:postgresql://foo.mi.hdm-stuttgart.de/js486";
        String pass = "(JJS)2003ab";
        String user = "js486";


        try {
            Connection con = DriverManager.getConnection(url, user, pass);
            String sql = "INSERT INTO users VALUES (DEFAULT, ?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, username.getText());
            stmt.setString(2, password.getText());
            stmt.setString(3, email.getText());
            stmt.executeQuery();
        } catch (SQLException e) {

        }
    }

}
