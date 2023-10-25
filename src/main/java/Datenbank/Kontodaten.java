package Datenbank;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Kontodaten {



        public void Kontoabfrage() throws SQLException {

            Scanner scanner = new Scanner(System.in);
            System.out.println("Geben Sie Ihren Kürzel ein: ");
            String kürzel = scanner.nextLine();
            String url = "jdbc:postgresql://foo.mi.hdm-stuttgart.de/"+ kürzel;

            String user = kürzel;
            System.out.println("Geben Sie ihr Passwort ein: ");
            String passwort = scanner.nextLine();
            String pass = passwort;

            Connection con = DriverManager.getConnection(url, user, pass);
            try {
                String sql = "SELECT id, datum, bezeichnung, betrag, kontostand FROM konto";
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    System.out.print(rs.getInt("id") + ": ");
                    System.out.print(rs.getString("datum") + " ");
                    System.out.print(rs.getString("bezeichnung") + " ");
                    System.out.print(rs.getDouble("betrag") + "€");
                    System.out.println(rs.getInt("kontostand") + " ");
                }
            } catch ( final java.sql.SQLException e ){
                String sql = "CREATE TABLE Konto(\n" +
                        "    id         SERIAL,\n" +
                        "    Datum\t   DATE DEFAULT CURRENT_DATE ,\n" +
                        "\tBezeichnung TEXT,\n" +
                        "\tBetrag \t   VARCHAR(30) NOT NULL,\n" +
                        "\tKontostand VARCHAR(30)\n" +
                        ")";
                PreparedStatement stmt = con.prepareStatement(sql);
                int a = stmt.executeUpdate();

                String einfügen = "INSERT INTO konto (Datum , Bezeichnung, Betrag, Kontostand) VALUES ( TO_DATE(?, 'DD.MM.YYYY'), ? , ? , ?)";
                PreparedStatement stm = con.prepareStatement(einfügen);

                /*System.out.println("Geben Sie die ID ein: ");
                int scan = scanner.nextInt();
                stm.setInt(1,scan);
                */
                System.out.println("Geben Sie ein Datum im Format 'DD.MM.YYYY' an: ");
                String dateString = scanner.nextLine();
                stm.setString(1,dateString);

                System.out.println("Geben Sie eine Bezeichnung ein: ");
                String bezeichnung = scanner.nextLine();
                stm.setString(2,bezeichnung);

                System.out.println("Geben Sie einen Betrag an: ");
                double betrag = scanner.nextDouble();
                stm.setDouble(3, betrag);

                System.out.println("Geben Sie einen Kontostand an: ");
                double kontostand = scanner.nextDouble();
                stm.setDouble(4,kontostand);

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
