package mainpackage;

import Applikation.Login;
import Datenbank.EingabeHinzufügen;
import Datenbank.Kontodaten;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.sql.SQLException;
import java.util.Scanner;

public class Controller {
    //@FXML
   // private void sayHello(ActionEvent event) {
    public void controller() throws SQLException {

    Login l = new Login();

        Scanner scanner = new Scanner(System.in);
        Kontoübersicht kontoübersicht = new Kontoübersicht();
        Kontodaten kontodaten = new Kontodaten();
        Konto konto = new Konto();
        EingabeHinzufügen eingabeHinzufügen = new EingabeHinzufügen();


        System.out.println("Welche Aktion wollen Sie durchführen? \n" +
                "1: Kontostand anzeigen\n" +
                "2: Änderung hinzufügen\n" +
                "3: Ausgabe hinzufügen\n");

        int aktion = scanner.nextInt();

        switch (aktion){
            case 1:
                kontodaten.Kontoabfrage();controller();break;
            case 2:
                eingabeHinzufügen.Eingabe();break;
            default:
                System.err.println("Error");
        }


    }



}
