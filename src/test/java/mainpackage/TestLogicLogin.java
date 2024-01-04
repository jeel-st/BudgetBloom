package mainpackage;

import Logic.LogicFacade;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TestLogicLogin {

    @Test
    public void testIsValidUser() throws IOException {
        assertTrue(LogicFacade.getInstance().isValidUser("tester", "123456789!"));
        assertFalse(LogicFacade.getInstance().isValidUser("tester", "wrongpw"));
        assertFalse(LogicFacade.getInstance().isValidUser("wronguser", "123456789!"));
        assertFalse(LogicFacade.getInstance().isValidUser("wronguser", "wrongpw"));
    }
}
