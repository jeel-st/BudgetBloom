package mainpackage;

import Logic.LogicEditEntry;
import  Logic.LogicSuperClass;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class TestLogicEditEntry {

    @Test
    void testCheckFrequency() {
        LogicEditEntry logicEditEntry = new LogicEditEntry();

        assertEquals("täglich", logicEditEntry.checkFrequency("repeat", "täglich"));
        assertEquals("monatlich", logicEditEntry.checkFrequency("repeat", "monatlich"));
        assertEquals("jährlich", logicEditEntry.checkFrequency("repeat", "jährlich"));
        assertNull(logicEditEntry.checkFrequency("not repeat", "irrelevant"));

    }

    @Test
    void testIsRegularBool() {
        LogicEditEntry logicEditEntry = new LogicEditEntry();

        assertTrue(logicEditEntry.isRegularBool("repeat"));
        assertFalse(logicEditEntry.isRegularBool("Einmalig"));

    }
}
