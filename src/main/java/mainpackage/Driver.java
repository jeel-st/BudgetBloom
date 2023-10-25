package mainpackage;


import Datenbank.Kontodaten;

import java.sql.SQLException;
import java.util.Scanner;

public class Driver {

    public static void main(String[] args) throws SQLException {
        Controller controller = new Controller();
        Kontodaten kontodaten = new Kontodaten();
        kontodaten.Kontoabfrage();
        //controller.controller();

    }
}
