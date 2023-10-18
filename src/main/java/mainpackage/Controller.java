package mainpackage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.util.Scanner;

public class Controller {
    //@FXML
   // private void sayHello(ActionEvent event) {
    public void controller(){


        Scanner scanner = new Scanner(System.in);
        Kontoübersicht kontoübersicht = new Kontoübersicht();
        Konto konto = new Konto();


        System.out.println("Welche Aktion wollen Sie durchführen? \n" +
                "1: Kontostand anzeigen\n" +
                "2: Einnahme hinzufügen\n" +
                "3: Ausgabe hinzufügen\n");

        int aktion = scanner.nextInt();

        switch (aktion){
            case 1:
                System.out.println(konto.Kontostand);controller();break;
            case 2:
                konto.Einnahmen();break;
            case 3:
                konto.Ausgaben();break;
            default:
                System.err.println("Error");
        }


    }



}
