package mainpackage;

import LocalExceptions.NewEntryExceptions.*;
import Logic.LogicFacade;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TestLogicNewEntry {



    @Test
    void testAccountChangeChecker() {
        double positiveNum = 418.12;
        double negativeNum = -614.92;
        assertEquals(positiveNum, LogicFacade.getInstance().accountChangeChecker(positiveNum, "Einnahme"));
        assertEquals(-positiveNum, LogicFacade.getInstance().accountChangeChecker(positiveNum, "Ausgabe"));
        assertEquals(negativeNum, LogicFacade.getInstance().accountChangeChecker(negativeNum, "Einnahme"));         //es wird davon ausgegangen, dass der Nutzer mit einer negativen Einnahme eigentlich eine Ausgabe darstellen möchte
        assertEquals(negativeNum, LogicFacade.getInstance().accountChangeChecker(negativeNum, "Ausgabe"));
    }

    @Test
    void testCheckingFormats() {
        LocalDate thisMoment = LocalDate.now();
        String amountChange = "412.43";
        String invalidAmountChange = "Hello World!";
        String note = "Einkauf";

        assertThrows(NoteIsNullException.class, () -> {
            LogicFacade.getInstance().checkingFormats(amountChange, "", thisMoment);
        });
        assertThrows(ParseDoubleException.class, () -> {
            LogicFacade.getInstance().checkingFormats(invalidAmountChange, note, thisMoment);
        });
        assertThrows(AmountChangeIsNullException.class, () -> {
            LogicFacade.getInstance().checkingFormats("", note, thisMoment);
        });

    }
}
