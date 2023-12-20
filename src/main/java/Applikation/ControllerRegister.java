package Applikation;

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
    DatenbankConnector dc = new DatenbankConnector();

    public void userToLogin(ActionEvent event) throws IOException {
        d.changeScene("/FXML/sample.fxml");
    }

    public void userCreate(ActionEvent event) throws IOException, SQLException {
        log.info("User creation started");
        if (fillingControl()) {
            if (passwordControl()) {
                if (checkingUsername()) {
                    if (newUserEntry()) {
                        newTable();
                        d.changeScene("/FXML/sample.fxml");
                    } else {
                        log.error("NewUserEntry failed");
                    }
                } else {
                    log.error("checkingUsername failed");
                }
            } else {
                log.error("passwordControl failed");
            }
        } else {
            log.error("fillingControl failed");
        }
    }

    public boolean fillingControl() {
        String checkingUsername = username.getText();
        String checkingPassword1 = password.getText();
        String checkingPassword2 = password2.getText();
        String checkingEmail = email.getText();

        if (checkingEmail.isEmpty() && checkingPassword1.isEmpty() && checkingPassword2.isEmpty() && checkingUsername.isEmpty()) {
            wrongRegister.setText("Please enter your data");
            return false;
        } else if (checkingEmail.isEmpty()) {
            wrongRegister.setText("Please enter your email.");
            return false;
        } else if (checkingUsername.isEmpty()) {
            wrongRegister.setText("Please enter your username");
            return false;
        } else if (checkingPassword1.isEmpty()) {
            wrongRegister.setText("Please enter your password");
            return false;
        } else if (checkingPassword2.isEmpty()) {
            wrongRegister.setText("Please confirm your password");
            return false;
        } else {

            return true;
        }

    }

    public boolean passwordControl() {
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

    public boolean newUserEntry() {
        try (Connection con = dc.getConnection()) {
            String sql = "INSERT INTO users VALUES (DEFAULT, ?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, username.getText());
            stmt.setString(2, password.getText());
            stmt.setString(3, email.getText());
            stmt.executeUpdate();
            log.info("New user created successfully");
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't connect to Database");
            return false;
        }
    }

    public void newTable() {
        try (Connection con = dc.getConnection()) {
            String sqlTable = "CREATE TABLE konto" + username.getText() + "( \n" +
                    "id SERIAL PRIMARY KEY,\n" +
                    "edate DATE DEFAULT CURRENT_DATE NOT NULL,\n" +
                    "note TEXT,\n" +
                    "amount NUMERIC NOT NULL,\n" +
                    "bankBalance NUMERIC NOT NULL,\n" +
                    "importance INTEGER CHECK(importance >= 0 AND importance <=10) NOT NULL,\n" +
                    "isregular BOOLEAN DEFAULT false NOT NULL,\n" +
                    "frequency VARCHAR(10) CHECK(frequency IN ('täglich', 'monatlich', 'jährlich'))\n" +
                    ")";
            PreparedStatement stm = con.prepareStatement(sqlTable);

            stm.execute();
            log.info("Table build successfully");
            con.close();
        } catch (SQLException ex) {
            log.error("table build failed");
            ex.printStackTrace();
        }
    }

    public boolean checkingUsername() throws SQLException {
        try (Connection con = dc.getConnection()) {

            String checkUsername = username.getText();

            String query = "SELECT * FROM users WHERE username = ?";
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setString(1, checkUsername);
                try (ResultSet resultSet = stmt.executeQuery()) {

                    if (resultSet.next()) {
                        wrongRegister.setText("Der Benutzername ist bereits vergeben.");
                        return false;
                    } else {
                        return true;
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Couldn't connect to Database");
            return false;
        }

    }
}
