package Applikation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import mainpackage.Driver;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


public class ControllerÜbersicht implements Initializable{

    @FXML
    private TableView<NewEntry> table;

    @FXML
    private TableColumn<NewEntry, String> datum;

    @FXML
    private TableColumn<NewEntry, String> grund;

    @FXML
    private TableColumn<NewEntry, Double> betrag;

    @FXML
    private TableColumn<NewEntry, Double> kontostand;


    @FXML
    private Button logout;

    @FXML
    private Button neueEingabe;

    private String username = Login.publicusername;

    //Oberserver Liste weil Tabelle es als Input nutzt
    ObservableList<NewEntry> list = FXCollections.observableArrayList(
            //new User("17.11.2023", "Lebensmittel", -12.3, 123.45)
    );

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        datum.setCellValueFactory(new PropertyValueFactory <NewEntry, String>("datum"));
        grund.setCellValueFactory(new PropertyValueFactory <NewEntry, String>("grund"));
        betrag.setCellValueFactory(new PropertyValueFactory <NewEntry, Double>("betrag"));
        kontostand.setCellValueFactory(new PropertyValueFactory <NewEntry, Double>("kontostand"));
        try{
            datenbank();
        }catch(Exception e){
            System.out.println("Fehler bei Suche nach Datenbankeinträgen");
        }
        table.setItems(list);
    }

    public void userLogout(ActionEvent event)throws IOException {
        Driver d = new Driver();
        d.changeScene("/FXML/sample.fxml");
    }

    public void userNeueEingabe(ActionEvent event)throws IOException {
        Driver d = new Driver();
        d.changeScene("/FXML/eingabe.fxml");
    }

    public void datenbank() throws SQLException{

        String url = "jdbc:postgresql://foo.mi.hdm-stuttgart.de/js486";
        String pass = "(JJS)2003ab";
        String user = "js486";

        Connection con = DriverManager.getConnection(url, user, pass);

        try {
            String sql = "SELECT edate, note, amount, bankbalance FROM konto"+username;
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                String datum = (rs.getDate("edate").toString());
                String grund = (rs.getString("note"));
                double betrag = (rs.getDouble("amount"));
                double kontostand = (rs.getDouble("bankbalance"));
                list.add(new NewEntry(datum, grund, betrag, kontostand));
            }
        }catch(Exception e){
            System.out.println("Fehler! Bei der Übertragung von Tabellendaten");
        }
    }


}
