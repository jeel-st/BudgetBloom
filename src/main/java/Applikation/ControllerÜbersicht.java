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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


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
    private TableColumn<NewEntry, Integer> wichtigkeit;


    @FXML
    private Button logout;

    @FXML
    private Button neueEingabe;

    @FXML
    private Button eintragLöschen;

    private String username = Login.publicusername;
    public static Logger log = LogManager.getLogger(ControllerÜbersicht.class);

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
        wichtigkeit.setCellValueFactory(new PropertyValueFactory <NewEntry, Integer>("wichtigkeit"));
        log.debug("Started to update balance");
        Balance.updateBalance();
        log.debug("Finished update of balance");
        try{
            datenbank();
            log.info("Searching for data succeed");
        }catch(Exception e){
            log.error("Searching for data failed");
        }
        table.setItems(list);
    }

    public void userLogout(ActionEvent event)throws IOException {
        Driver d = new Driver();
        log.info("Logout button changed scene to sample.fxml");
        d.changeScene("/FXML/sample.fxml");
    }

    public void userNeueEingabe(ActionEvent event)throws IOException {
        Driver d = new Driver();
        log.info("Changed Scene to eingabe.fxml");
        d.changeScene("/FXML/eingabe.fxml");
    }

    public void datenbank() throws SQLException{

        String url = "jdbc:postgresql://foo.mi.hdm-stuttgart.de/js486";
        String pass = "(JJS)2003ab";
        String user = "js486";

        Connection con = DriverManager.getConnection(url, user, pass);

        if(username != null) {
            try {
                String sql = "SELECT edate, note, amount, bankbalance, importance FROM konto" + username + " ORDER BY edate DESC, id DESC";

                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String datum = (rs.getDate("edate").toString());
                    String grund = (rs.getString("note"));
                    double betrag = (rs.getDouble("amount"));
                    double kontostand = (rs.getDouble("bankbalance"));
                    Integer wichtigkeit = (rs.getInt("importance"));
                    list.add(new NewEntry(datum, grund, betrag, kontostand, wichtigkeit));
                }
            } catch (Exception e) {
                log.error("Failed to transfer data from database");
            }
        }else{
            log.error("Username is null");
        }
    }
    public void removeRow(ActionEvent event) throws SQLException {
        try{
            double betrag = table.getSelectionModel().getSelectedItem().getBetrag();
            String datum = table.getSelectionModel().getSelectedItem().getDatum();
            String grund = table.getSelectionModel().getSelectedItem().getGrund();
            double kontostand = table.getSelectionModel().getSelectedItem().getKontostand();
            Integer wichtigkeit = table.getSelectionModel().getSelectedItem().getWichtigkeit();
            log.info(betrag + datum + grund + kontostand + wichtigkeit);
            String url = "jdbc:postgresql://foo.mi.hdm-stuttgart.de/js486";
            String pass = "(JJS)2003ab";
            String user = "js486";

                Connection con = DriverManager.getConnection(url, user, pass);
                log.info("Connection to database succeed");

                    String sql = "DELETE FROM konto" + username + " WHERE edate = ? AND note = ? AND amount = ? AND bankbalance = ? AND importance = ?";
                    PreparedStatement stmt = con.prepareStatement(sql);
                    stmt.setDate(1, Date.valueOf(datum));
                    stmt.setString(2, grund);
                    stmt.setDouble(3, betrag);
                    stmt.setDouble(4, kontostand);
                    stmt.setInt(5, wichtigkeit);
                    int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    log.info("Deletion successful. Rows affected: " + rowsAffected);
                    table.getItems().removeAll(table.getSelectionModel().getSelectedItem());
                    //Updated die Kontostände und lädt die Tabelle neu
                    /*Balance.updateBalance();
                    try{
                        datenbank();
                        log.info("Searching for data succeed");
                    }catch(Exception e){
                        log.error("Searching for data failed");
                    }
                    table.setItems(list);*/
                    Driver d = new Driver();
                    log.info("Reload scene übersicht.fxml");
                    d.changeScene("/FXML/übersicht.fxml");
                } else {
                    log.info("No rows deleted. Check your criteria.");
                }

        }catch(Exception e){
            log.error("no row selected");
        }

    }


}
