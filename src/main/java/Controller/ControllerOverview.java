package Controller;

import Logic.LogicDatabase;
import Logic.LogicBalance;
import Logic.LogicTableEntry;
import Singleton.SingletonUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import mainpackage.Driver;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ControllerOverview implements Initializable{

    @FXML
    private TableView<LogicTableEntry> table;

    @FXML
    private TableColumn<LogicTableEntry, String> datum;

    @FXML
    private TableColumn<LogicTableEntry, String> grund;

    @FXML
    private TableColumn<LogicTableEntry, Double> betrag;

    @FXML
    private TableColumn<LogicTableEntry, Double> kontostand;

    @FXML
    private TableColumn<LogicTableEntry, Integer> wichtigkeit;
    @FXML
    private TableColumn<LogicTableEntry, String> regelmaeßigkeit;


    @FXML
    private Button logout;

    @FXML
    private Button neueEingabe;

    @FXML
    private Button eintragLöschen;
    @FXML
    private Button eintragBearbeiten;
    @FXML
    private Label errorLabel;
    LogicDatabase dc = new LogicDatabase();
    SingletonUser sp = SingletonUser.getInstance();
    private String localUsername = sp.getName();

    public static Logger log = LogManager.getLogger(ControllerOverview.class);

    //Oberserver Liste weil Tabelle es als Input nutzt
    ObservableList<LogicTableEntry> list = FXCollections.observableArrayList(
            //new User("17.11.2023", "Lebensmittel", -12.3, 123.45)
    );

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        datum.setCellValueFactory(new PropertyValueFactory <LogicTableEntry, String>("datum"));
        grund.setCellValueFactory(new PropertyValueFactory <LogicTableEntry, String>("grund"));
        betrag.setCellValueFactory(new PropertyValueFactory <LogicTableEntry, Double>("betrag"));
        kontostand.setCellValueFactory(new PropertyValueFactory <LogicTableEntry, Double>("kontostand"));
        wichtigkeit.setCellValueFactory(new PropertyValueFactory <LogicTableEntry, Integer>("wichtigkeit"));
        regelmaeßigkeit.setCellValueFactory(new PropertyValueFactory<LogicTableEntry, String>("regelmäßigkeit"));
        log.debug("Started to update balance");
        LogicBalance.updateBalance();
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
        log.info("Changed Scene to newEntry.fxml");
        d.changeScene("/FXML/newEntry.fxml");
    }
    public void removeRow(ActionEvent event) throws SQLException {
        if(safetyCheck()){
            deleteRow();
        }
    }

    public void datenbank() throws SQLException {


        try (Connection con = dc.getConnection()) {
            if (localUsername != null) {
                try {
                    String sql = "SELECT edate, note, amount, bankbalance, importance, isregular FROM konto" + localUsername + " ORDER BY edate DESC, id DESC";

                    PreparedStatement stmt = con.prepareStatement(sql);
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        String datum = (rs.getDate("edate").toString());
                        String grund = (rs.getString("note"));
                        double betrag = (rs.getDouble("amount"));
                        double kontostand = (rs.getDouble("bankbalance"));
                        Integer wichtigkeit = (rs.getInt("importance"));
                        Boolean regelmäßigkeitBool = (rs.getBoolean("isregular"));
                        String regelmäßigkeit;
                        if (regelmäßigkeitBool) {
                            regelmäßigkeit = "Regelmäßig";
                        } else {
                            regelmäßigkeit = "Einmalig";
                        }
                        list.add(new LogicTableEntry(datum, grund, betrag, kontostand, wichtigkeit, regelmäßigkeit));
                    }
                } catch (Exception e) {
                    log.error("Failed to transfer data from database");
                }
            } else {
                log.error("Username is null");
            }
        }catch (SQLException e) {
            log.error("Couldn't connect to Database", e);
            throw e;
        }
    }

    public boolean safetyCheck(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning");
        alert.setContentText("Are you sure you want to delete this row? The data cannot be restored afterwards.");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isEmpty()){
            log.info("Sicherheitsfenster wurde geschlossen");
            return false;
        }else if(result.get()== ButtonType.OK){
            return true;
        }else{
            return false;
        }

    }
    public void deleteRow(){
        try {
            double betrag = table.getSelectionModel().getSelectedItem().getBetrag();
            String datum = table.getSelectionModel().getSelectedItem().getDatum();
            String grund = table.getSelectionModel().getSelectedItem().getGrund();
            double kontostand = table.getSelectionModel().getSelectedItem().getKontostand();
            Integer wichtigkeit = table.getSelectionModel().getSelectedItem().getWichtigkeit();
            String regelmäßigkeit = table.getSelectionModel().getSelectedItem().getRegelmäßigkeit();
            Boolean regelmäßigkeitBool;
            if (regelmäßigkeit.equals("Regelmäßig")) {
                regelmäßigkeitBool = true;
            } else {
                regelmäßigkeitBool = false;
            }
            log.info(betrag + datum + grund + kontostand + wichtigkeit + regelmäßigkeit);
            try (Connection con = dc.getConnection()) {
                log.info("Connection to database succeed");

                String sql = "DELETE FROM konto" + localUsername + " WHERE edate = ? AND note = ? AND amount = ? AND bankbalance = ? AND importance = ? AND isregular = ?";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setDate(1, Date.valueOf(datum));
                stmt.setString(2, grund);
                stmt.setDouble(3, betrag);
                stmt.setDouble(4, kontostand);
                stmt.setInt(5, wichtigkeit);
                stmt.setBoolean(6, regelmäßigkeitBool);
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
                    log.info("Reload scene overview.fxml");
                    d.changeScene("/FXML/overview.fxml");
                } else {
                    log.info("No rows deleted.");
                    }
                }catch (SQLException e) {
                log.error("Couldn't connect to Database", e);
                throw e;
                    }
            } catch (Exception e) {
                errorLabel.setText("No row selected");
                log.error("Keine Zeile wurde ausgewählt");
            }

    }
    public void editRow(ActionEvent event) throws IOException {
        Driver d = new Driver();
        try {
            double betrag = table.getSelectionModel().getSelectedItem().getBetrag();
            String datum = table.getSelectionModel().getSelectedItem().getDatum();
            String grund = table.getSelectionModel().getSelectedItem().getGrund();
            double kontostand = table.getSelectionModel().getSelectedItem().getKontostand();
            int wichtigkeit = table.getSelectionModel().getSelectedItem().getWichtigkeit();
            String regelmäßigkeit = table.getSelectionModel().getSelectedItem().getRegelmäßigkeit();
            ControllerEditEntry.isregular = regelmäßigkeit;
            ControllerEditEntry.amount = betrag;
            ControllerEditEntry.bankBalance = kontostand;
            ControllerEditEntry.date = datum;
            ControllerEditEntry.note = grund;
            ControllerEditEntry.importance = wichtigkeit;
            d.changeScene("/FXML/editEntry.fxml");
        }catch(Exception e ){
            errorLabel.setText("No row selected");
            log.error("Keine Zeile wurde ausgewählt");
        }
    }


}
