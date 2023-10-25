package Datenbank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class EingabeHinzufügen {
    public void Eingabe() throws SQLException{
        Scanner scanner = new Scanner(System.in);
        System.out.println("Geben Sie Ihren Kürzel ein: ");
        String kürzel = scanner.nextLine();
        String url = "jdbc:postgresql://foo.mi.hdm-stuttgart.de/"+ kürzel;

        String user = kürzel;
        System.out.println("Geben Sie ihr Passwort ein: ");
        String passwort = scanner.nextLine();
        String pass = passwort;

        Connection con = DriverManager.getConnection(url, user, pass);

        String einfügen = "INSERT INTO konto (Datum , Bezeichnung, Einnahmen, Ausgaben, Kontostand) VALUES ( TO_DATE(?, 'DD.MM.YYYY'), ? ,?, ? , ?)";
        PreparedStatement stm = con.prepareStatement(einfügen);


        System.out.println("Geben Sie ein Datum im Format 'DD.MM.YYYY' an: ");
        String dateString = scanner.nextLine();
        stm.setString(1,dateString);

        System.out.println("Geben Sie eine Bezeichnung ein: ");
        String bezeichnung = scanner.nextLine();
        stm.setString(2,bezeichnung);

        System.out.println("Geben Sie einen Betrag als Einnahme an: ");
        double einnahme = scanner.nextDouble();
        stm.setDouble(3, einnahme);

        System.out.println("Geben Sie einen Betrag als Ausgabe an: ");
        double ausgabe = scanner.nextDouble();
        stm.setDouble(4, ausgabe);

        System.out.println("Geben Sie einen Kontostand an: ");
        double kontostand = scanner.nextDouble();
        stm.setDouble(5,kontostand);

        int i = stm.executeUpdate();
        if(i == 1){
            System.out.println("Es wurde eine Zeile geändert");
        }else {
            System.out.println("Es wurden " + i + " Zeilen geändert.");
        }

    }
}
