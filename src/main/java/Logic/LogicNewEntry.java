package Logic;

import Controller.ControllerEditEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogicNewEntry {
    public static Logger log = LogManager.getLogger(ControllerEditEntry.class);

    public double kontoVeränderungsÜberprüferEdit (String eingabeZahl, String myChoiceBox) {

        double d = Double.parseDouble(eingabeZahl);
        if (myChoiceBox.equals("Einnahme")) {

            log.info(d);
            return d;
        } else {

            log.info(d);
            if (d == 0) {
                return d;
            } else if (d > 0) {
                return -d;
            } else {
                return d;
            }

        }

    }
}
