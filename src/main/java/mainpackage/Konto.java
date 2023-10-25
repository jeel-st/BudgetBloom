package mainpackage;

import java.sql.SQLException;
import java.util.Scanner;

public class Konto {
    public double Kontostand;
    public double Einnahmen;
    public double Ausgaben;
    Scanner scanner = new Scanner(System.in);
    Controller controller = new Controller();

    public void Kontostand() throws SQLException {

        double Kontostand = this.Kontostand;
        this.Kontostand = Kontostand + Einnahmen + Ausgaben;
        controller.controller();

    }
    public void Einnahmen() throws SQLException{
        System.out.println("Geben Sie ihre Einnahmen an:");
        double Einnahmen = scanner.nextDouble();
        System.out.println("Ihre Einnahmen: "+ Einnahmen);
        this.Einnahmen = Einnahmen;
        Kontostand();
        controller.controller();
    }
    public void EinnahmenLöschen(){

    }
    public void Ausgaben()throws SQLException{
        System.out.println("Geben Sie ihre Ausgaben ein:");
        double Ausgaben = scanner.nextDouble();
        if(Ausgaben > 0){
            Ausgaben = -Ausgaben;
        }
        System.out.println("Ihre Ausgaben: "+ Ausgaben);
        this.Ausgaben = Ausgaben;
        Kontostand();
        controller.controller();

    }
    public void AusgabenLöschen(){

    }

}
