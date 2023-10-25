package mainpackage;

import java.sql.SQLException;


public class Driver {

    public static void main(String[] args) throws SQLException {
        Controller controller = new Controller();
        //Kontodaten kontodaten = new Kontodaten();
        //kontodaten.Kontoabfrage();
        controller.controller();

    }
}
