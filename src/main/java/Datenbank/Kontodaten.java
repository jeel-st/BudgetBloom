package Datenbank;
import java.sql.*;
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

                String einfügen = "INSERT INTO konto VALUES (?,?,?,?,?)";
                PreparedStatement stm = con.prepareStatement(sql);
                System.out.println("Geben Sie ein Datum an: ");
                int datum = scanner.nextInt();
                stm.setInt(2, datum);

                System.out.println("Geben Sie eine Bezeichnung ein: ");
                String bezeichnung = scanner.nextLine();
                stm.setString(3,bezeichnung);

                System.out.println("Geben Sie einen Betrag an: ");
                double betrag = scanner.nextDouble();
                stm.setDouble(4, betrag);

                System.out.println("Geben Sie einen Kontostand an: ");
                double kontostand = scanner.nextDouble();
                stm.setDouble(5,kontostand);

                ResultSet rs = stm.executeQuery();

            }

            //rs.close();
            //stmt.close();
            con.close();
        }

}
