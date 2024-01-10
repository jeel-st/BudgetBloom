package mainpackage;


import Logic.LogicFirstLogin;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestLogicFirstLogin {

    @Test
    void testInsertInitialBalance() {
        LogicFirstLogin logicFirstLogin = new LogicFirstLogin();

        logicFirstLogin.insertInitialBalance(100.0);

    }

    @Test
    void testIsBalanceNumber() {
        LogicFirstLogin logicFirstLogin = new LogicFirstLogin();


        assertTrue(logicFirstLogin.isBalanceNumber("100.00"));
        assertTrue(logicFirstLogin.isBalanceNumber("-50.5"));

        assertFalse(logicFirstLogin.isBalanceNumber("abc"));
        assertFalse(logicFirstLogin.isBalanceNumber("10.123"));


    }
}

