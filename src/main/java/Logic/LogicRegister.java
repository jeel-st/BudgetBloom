package Logic;

import Exceptions.RegisterExceptions.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogicRegister {
    LogicDatabase ld = new LogicDatabase();
    public static Logger log = LogManager.getLogger(LogicRegister.class);
    String proveRegisterTextFields(String username, String password, String password2, String email){
        try {
            if(fillingControl(username, password, password2, email)){
                if(checkingUsername(username)){
                    if(usernameLength(username)) {
                        if(emailLength(email)) {
                            if (passwordControl(password, password2)) {
                                if (newUserEntry(username, password, email)) {
                                    newTable(username);
                                }
                            }
                        }
                    }
                }
            }
            return "completed";
        }catch (AllNullException ane){
            return "Please fill out each field";
        }catch (EmailNullException ene){
            return "Please enter your email";
        }catch (UsernameNullException une){
            return "Please enter your username";
        }catch (PasswordNullException pne){
            return "Please enter your password";
        }catch (SecondPasswordNullException spne){
            return "Please confirm your password";
        }catch (UsernameTakenException ute){
            return "The username is already taken";
        }catch (PasswordsDontMatchException pdme){
            return "Passwords do not match";
        }catch (PasswordLengthException ple){
            return "Password has to be between 6 and 30 characters";
        }catch (PasswordSpecialCharException psce){
            return "Password doesn't contain a special character";
        }catch (UsernameLengthException ule){
            return "Username has to be between 6 and 19 characters";
        } catch (SQLException e) {
            return "Couldn't get a database connection. Please check your internet connection";
        } catch (EmailLengthException e) {
            return "Email has to be between 6 and 69 characters";
        }
    }
    public boolean fillingControl(String username, String password, String password2, String email) throws AllNullException, EmailNullException, PasswordNullException, SecondPasswordNullException, UsernameNullException {
        String emptyString = "";
        if (email.equals(emptyString) && password.equals(emptyString) && password2.equals(emptyString) && username.equals(emptyString)) {
            throw new AllNullException();
        } else if (email.equals(emptyString)) {
            throw new EmailNullException();
        } else if (username.equals(emptyString)) {
            throw new UsernameNullException();
        } else if (password.equals(emptyString)) {
            throw new PasswordNullException();
        } else if (password2.equals(emptyString)) {
            throw new SecondPasswordNullException();
        }else{
            return true;
        }

    }

    public boolean checkingUsername(String username) throws SQLException, UsernameTakenException {
        Connection con = ld.getConnection();

            String query = "SELECT * FROM users WHERE username = ?";
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setString(1, username);
                try (ResultSet resultSet = stmt.executeQuery()) {

                    if (resultSet.next()) {
                        throw new UsernameTakenException();
                    } else {
                        return true;
                    }
                }
            }

    }
    public boolean usernameLength(String username) throws UsernameLengthException{
        if(username.length() < 5 || username.length() > 20){
            throw new UsernameLengthException();
        }else{
            return true;
        }
    }
    public boolean emailLength(String email) throws EmailLengthException{
        if(email.length() < 5 || email.length() > 70){
            throw new EmailLengthException();
        }else{
            return true;
        }
    }
    public boolean passwordControl(String password, String password2) throws PasswordsDontMatchException, PasswordLengthException, PasswordSpecialCharException {
        if (!(password.equals(password2))) {
            throw new PasswordsDontMatchException();
        } else if (password.length() <= 6 || password.length() >= 30) {
            throw new PasswordLengthException();
        } else {

            String regex = "[^a-zA-Z0-9]";

            Pattern pattern = Pattern.compile(regex);

            Matcher matcher = pattern.matcher(password);

            if (matcher.find()) {
                return true;

            } else {
                throw new PasswordSpecialCharException();
            }
        }
    }
    public boolean newUserEntry(String username, String password, String email) throws SQLException {
        Connection con = ld.getConnection();
            String sql = "INSERT INTO users VALUES (DEFAULT, ?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, email);
            stmt.executeUpdate();
            log.info("New user created successfully");
            return true;
    }
    public void newTable(String username) {
        try (Connection con = ld.getConnection()) {
            String sqlTable = "CREATE TABLE konto" + username + "( \n" +
                    "id SERIAL PRIMARY KEY,\n" +
                    "edate DATE DEFAULT CURRENT_DATE NOT NULL,\n" +
                    "note TEXT,\n" +
                    "amount NUMERIC NOT NULL,\n" +
                    "bankBalance NUMERIC NOT NULL,\n" +
                    "importance INTEGER CHECK(importance >= 0 AND importance <=10) NOT NULL,\n" +
                    "isregular BOOLEAN DEFAULT false NOT NULL,\n" +
                    "frequency VARCHAR(10) CHECK(frequency IN ('täglich', 'monatlich', 'jährlich'))\n" +
                    "payment VARCHAR(20) DEFAULT NULL\n" +
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
}
