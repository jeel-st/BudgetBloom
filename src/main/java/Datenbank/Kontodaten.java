package Datenbank;
import Applikation.Login;

import java.sql.*;
import java.util.Scanner;

public class Kontodaten {




        public void Kontoabfrage() throws SQLException {



            Scanner scanner = new Scanner(System.in);
            /*
            System.out.println("Geben Sie Ihren Kürzel ein: ");
            String kürzel = scanner.nextLine();


            String user = kürzel;

            String url = "jdbc:postgresql://foo.mi.hdm-stuttgart.de/"+ user;
            System.out.println("Geben Sie ihr Passwort ein: ");
            String passwort = scanner.nextLine();
            String pass = passwort;
            */

            String url = "jdbc:postgresql://foo.mi.hdm-stuttgart.de/js486";
            String pass = "(JJS)2003ab";
            String user = "js486";

            Connection con = DriverManager.getConnection(url, user, pass);
            try {
                String sql = "SELECT id, Datum, Bezeichnung, Einnahmen, Ausgaben, Kontostand FROM konto";
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    System.out.print(rs.getInt("id") + ": ");
                    System.out.print(rs.getString("Datum") + " ");
                    System.out.print(rs.getString("Bezeichnung") + " ");
                    System.out.print(rs.getDouble("Einnahmen") + "€");
                    System.out.print(rs.getDouble("Ausgaben") + "€");
                    System.out.println(rs.getInt("Kontostand") + "€");
                }
            } catch (  Exception e ){  // java.sql.SQL
                String sql = "CREATE TABLE Konto(\n" +
                        "    id         SERIAL,\n" +
                        "    Datum\t   DATE DEFAULT CURRENT_DATE ,\n" +
                        "\tBezeichnung TEXT,\n" +
                        "\tEinnahmen \t   VARCHAR(30) NOT NULL,\n" +
                        "\tAusgaben   \t  VARCHAR(30) NOT NULL, \n" +
                        "\tKontostand VARCHAR(30)\n" +
                        ")";
                PreparedStatement stmt = con.prepareStatement(sql);
                int a = stmt.executeUpdate();

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

            //rs.close();
            //stmt.close();
            con.close();
        }

}
