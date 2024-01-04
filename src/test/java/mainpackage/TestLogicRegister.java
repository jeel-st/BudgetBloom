package mainpackage;
import LocalExceptions.RegisterExceptions.*;
import Logic.LogicFacade;
import Logic.LogicRegister;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestLogicRegister {
    LogicRegister lr = new LogicRegister();
    @Test
    void checkFilling() throws UsernameNullException, EmailNullException, AllNullException, PasswordNullException, SecondPasswordNullException {
        assertTrue(lr.fillingControl("abc123", "AbCdE12!", "AbCdE12!", "tester.abc@web.de"));
        assertTrue(lr.fillingControl("abc124", "AbCdE12?", "AbCdE12?", "tester.abcde@web.de"));
        assertTrue(lr.fillingControl("abc125", "AbCdE12#", "AbCdE12#", "tester.abc@hdm-stuttgart.de"));
        assertTrue(lr.fillingControl("abc128", "AbCdE12=", "AbCdE12=", "tester.abc@gmail.com"));

    }
}
