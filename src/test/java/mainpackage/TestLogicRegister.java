package mainpackage;
import LocalExceptions.RegisterExceptions.*;
import Logic.LogicFacade;
import Logic.LogicRegister;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class TestLogicRegister {
    LogicRegister lr = new LogicRegister();
    @Test
    void TestFillingControl() throws UsernameNullException, EmailNullException, AllNullException, PasswordNullException, SecondPasswordNullException {
        assertTrue(lr.fillingControl("abc123", "AbCdE12!", "AbCdE12!", "tester.abc@web.de"));
        assertTrue(lr.fillingControl("abc124", "AbCdE12?", "AbCdE12?", "tester.abcde@web.de"));
        assertTrue(lr.fillingControl("abc125", "AbCdE12#", "AbCdE12#", "tester.abc@hdm-stuttgart.de"));
        assertTrue(lr.fillingControl("abc128", "AbCdE12=", "AbCdE12=", "tester.abc@gmail.com"));
        assertThrows(AllNullException.class, () ->{
            lr.fillingControl("","","","");
        });
        assertThrows(UsernameNullException.class, () -> {
            lr.fillingControl("", "abc", "abc", "abcde@gf.hij");
        });
        assertThrows(PasswordNullException.class, ()->{
            lr.fillingControl("abc", "", "abcde", "abcde@fgh.ijk");
        });
        assertThrows(SecondPasswordNullException.class, () ->{
            lr.fillingControl("heyjo", "ajaj", "", "abab@cdcd.ef");
        });
        assertThrows(EmailNullException.class, () ->{
            lr.fillingControl("joel", "itsme", "itsme", "");
        });
    }

    @Test
    void TestProveRegisterTextField(){
        assertEquals("Please fill out each field", lr.proveRegisterTextFields("","","",""));
        assertEquals("Please enter your email", lr.proveRegisterTextFields("joel", "itsme", "itsme", ""));
        assertEquals("Please enter your username", lr.proveRegisterTextFields("", "abc", "abc", "abcde@gf.hij"));
        assertEquals("Please enter your password", lr.proveRegisterTextFields("abc", "", "abcde", "abcde@fgh.ijk"));
        assertEquals("Please confirm your password", lr.proveRegisterTextFields("heyjo", "ajaj", "", "abab@cdcd.ef"));
        assertEquals("The username is already taken", lr.proveRegisterTextFields("js486", "abc", "abc", "test@123.com"));
        assertEquals("Passwords do not match", lr.proveRegisterTextFields("abcdef", "abc", "abcde", "afgh@aha.de"));
        assertEquals("Password has to be between 6 and 30 characters", lr.proveRegisterTextFields("ajda123", "a!", "a!", "ajrh@web.de"));
        assertEquals("Password doesn't contain a special character", lr.proveRegisterTextFields("ajdks306", "abcdefg","abcdefg", "juj@web.de"));
    }

    @Test
    void TestCheckingUsername() throws SQLException, UsernameTakenException {
        assertTrue(lr.checkingUsername("hermann123"));
        assertTrue(lr.checkingUsername("losti954"));
        assertTrue(lr.checkingUsername("kaufhaus434"));
        assertThrows(UsernameTakenException.class, () ->{
            lr.checkingUsername("js486");
        });
        assertThrows(UsernameTakenException.class, () ->{
            lr.checkingUsername("tester");
        });
        assertThrows(UsernameTakenException.class, () ->{
            lr.checkingUsername("GalacticPhoenix11");
        });
    }

    @Test
    void TestPasswordControl() throws PasswordLengthException, PasswordsDontMatchException, PasswordSpecialCharException {
        assertThrows(PasswordsDontMatchException.class, () ->{
            lr.passwordControl("AFGBE!$$%", "KFJGK$%()");
        });
        assertThrows(PasswordsDontMatchException.class, () ->{
            lr.passwordControl("absdjkf", "jskfsak");
        });
        assertThrows(PasswordsDontMatchException.class, () ->{
            lr.passwordControl("aba", "ab");
        });
        assertThrows(PasswordLengthException.class, () ->{
            lr.passwordControl("abcd", "abcd");
        });
        assertThrows(PasswordLengthException.class, () ->{
            lr.passwordControl("aba", "aba");
        });
        assertThrows(PasswordLengthException.class, () ->{
            lr.passwordControl("qwertzuiopüasdfghjklöäyxcvbnmqw", "qwertzuiopüasdfghjklöäyxcvbnmqw");
        });
        assertThrows(PasswordSpecialCharException.class, () ->{
            lr.passwordControl("abcdefg", "abcdefg");
        });
        assertThrows(PasswordSpecialCharException.class, () ->{
            lr.passwordControl("ABCDE1234", "ABCDE1234");
        });
        assertTrue(lr.passwordControl("ABCDE!!!!", "ABCDE!!!!"));
        assertTrue(lr.passwordControl("lol!!!!", "lol!!!!"));
        assertTrue(lr.passwordControl("lol!?!!", "lol!?!!"));
    }

}
