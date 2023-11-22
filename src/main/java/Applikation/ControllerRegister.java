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

    Driver d = new Driver();

    public void userToLogin(ActionEvent event) throws IOException{
        d.changeScene("/FXML/sample.fxml");
    }

    public void userCreate(ActionEvent event) throws IOException, SQLException {
        if(fillingControl()) {
            if (passwordControl()) {
                if(checkingUsername()) {
                    if (newUserEntry()) {
                            newTable();
                        d.changeScene("/FXML/sample.fxml");
                    }
                }
            }
        }
    }
    public boolean fillingControl(){
        String checkingUsername = username.getText();
        String checkingPassword1 = password.getText();
        String checkingPassword2 = password2.getText();
        String checkingEmail = email.getText();
        if(checkingEmail.isEmpty() && checkingPassword1.isEmpty() && checkingPassword2.isEmpty() && checkingUsername.isEmpty()){
            wrongRegister.setText("Please enter your data");
            return false;
        }
        else if(checkingEmail.isEmpty()){
            wrongRegister.setText("Please enter your email.");
            return false;
        }else if(checkingUsername.isEmpty()){
            wrongRegister.setText("Please enter your username");
            return false;
        }else if(checkingPassword1.isEmpty()){
            wrongRegister.setText("Please enter your password");
            return false;
        }else if(checkingPassword2.isEmpty()){
            wrongRegister.setText("Please confirm your password");
            return false;
        }else{
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
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Es ist ein Fehler passiert");
            return false;
        }
    }
    public void newTable(){
        String url = "jdbc:postgresql://foo.mi.hdm-stuttgart.de/js486";
        String pass = "(JJS)2003ab";
        String user = "js486";
        try {
                Connection con = DriverManager.getConnection(url, user, pass);
                String sqlTable = "CREATE TABLE konto"+ username.getText() +"( \n"+
                "id SERIAL PRIMARY KEY,\n"+
                "edate DATE DEFAULT CURRENT_DATE,\n"+
                "note TEXT,\n"+
                "amount NUMERIC,\n"+
                "bankBalance NUMERIC,\n"+
                "importance INTEGER CHECK(importance >= 0 AND importance <=10) \n"+
                ")";
            PreparedStatement stm = con.prepareStatement(sqlTable);

            stm.execute();
            con.close();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public boolean checkingUsername() throws SQLException{
        String url = "jdbc:postgresql://foo.mi.hdm-stuttgart.de/js486";
        String pass = "(JJS)2003ab";
        String user = "js486";

        String checkUsername = username.getText();
        Connection con = DriverManager.getConnection(url, user, pass);
        String query = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, checkUsername);
            try (ResultSet resultSet = stmt.executeQuery()) {

                if(resultSet.next()){
                    wrongRegister.setText("Der Benutzername ist bereits vergeben.");
                    return false;
                }else{
                    return true;
                }
            }
        }

    }

}
