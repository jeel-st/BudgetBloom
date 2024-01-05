package Controller;

import Logic.*;
import Singleton.SingletonUser;
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
    private TableColumn<LogicTableEntry, String> date;

    @FXML
    private TableColumn<LogicTableEntry, String> reason;

    @FXML
    private TableColumn<LogicTableEntry, Double> amount;

    @FXML
    private TableColumn<LogicTableEntry, Double> accountBalance;

    @FXML
    private TableColumn<LogicTableEntry, Integer> importance;
    @FXML
    private TableColumn<LogicTableEntry, String> regularity;


    @FXML
    private Button logout;

    @FXML
    private Button newInput;

    @FXML
    private Button inputDelete;
    @FXML
    private Button inputEdit;
    @FXML
    private Label errorLabel;
    LogicOverview lo = new LogicOverview();
    LogicFacade lf = LogicFacade.getInstance();
    public static Logger log = LogManager.getLogger(ControllerOverview.class);


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        log.info("started to initialize");
        date.setCellValueFactory(new PropertyValueFactory <LogicTableEntry, String>("date"));
        reason.setCellValueFactory(new PropertyValueFactory <LogicTableEntry, String>("reason"));
        amount.setCellValueFactory(new PropertyValueFactory <LogicTableEntry, Double>("amount"));
        accountBalance.setCellValueFactory(new PropertyValueFactory <LogicTableEntry, Double>("accountBalance"));
        importance.setCellValueFactory(new PropertyValueFactory <LogicTableEntry, Integer>("importance"));
        regularity.setCellValueFactory(new PropertyValueFactory<LogicTableEntry, String>("regularity"));
        log.debug("Started to update balance");
        LogicFacade.updateBalance();
        log.debug("Finished update of balance");
        try{
            table.setItems(lo.database());
            log.info("Searching for data succeed");
        }catch(Exception e){
            log.error("Searching for data failed");
        }

    }

    public void userLogout(ActionEvent event)throws IOException {
        Driver d = new Driver();
        log.info("Logout button changed scene to sample.fxml");
        d.changeScene("/FXML/sample.fxml");
    }

    public void userNewInput(ActionEvent event)throws IOException {
        Driver d = new Driver();
        log.info("Changed Scene to newEntry.fxml");
        d.changeScene("/FXML/newEntry.fxml");
    }
    public void removeRow(ActionEvent event) throws SQLException {
        if(safetyCheck()){
            deleteRow();
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
            double amount = table.getSelectionModel().getSelectedItem().getAmount();
            String date = table.getSelectionModel().getSelectedItem().getDate();
            String reason = table.getSelectionModel().getSelectedItem().getReason();
            double accountBalance = table.getSelectionModel().getSelectedItem().getAccountBalance();
            Integer importance = table.getSelectionModel().getSelectedItem().getImportance();
            String regularity = table.getSelectionModel().getSelectedItem().getRegularity();
            boolean regularityBool;
            if (regularity.equals("Regelmäßig")) {
                regularityBool = true;
            } else {
                regularityBool = false;
            }
            log.info(amount + date + reason + accountBalance + importance + regularity);

            int rowsAffected = lf.deleteRowInDatabase(amount, date, reason, accountBalance, importance, regularityBool);
                if (rowsAffected > 0) {
                    log.info("Deletion successful. Rows affected: " + rowsAffected);
                    table.getItems().removeAll(table.getSelectionModel().getSelectedItem());
                    mainpackage.Driver d = new Driver();
                    log.info("Reload scene overview.fxml");
                    d.changeScene("/FXML/overview.fxml");
                } else {
                    log.info("No rows deleted.");
                }

            } catch (Exception e) {
                errorLabel.setText("No row selected");
                log.error("Keine Zeile wurde ausgewählt");
            }

    }
    public void editRow(ActionEvent event) throws IOException {
        Driver d = new Driver();
        LogicOverview lo = new LogicOverview();
        try {

            double amount = table.getSelectionModel().getSelectedItem().getAmount();
            String date = table.getSelectionModel().getSelectedItem().getDate();
            String reason = table.getSelectionModel().getSelectedItem().getReason();
            double accountBalance = table.getSelectionModel().getSelectedItem().getAccountBalance();
            int importance = table.getSelectionModel().getSelectedItem().getImportance();
            String regularity = table.getSelectionModel().getSelectedItem().getRegularity();
            log.info(amount);
            lo.saveValues(amount, date, reason, accountBalance, importance, regularity);
            d.changeScene("/FXML/editEntry.fxml");
        }catch(Exception e ){
            errorLabel.setText("No row selected");
            log.error("Keine Zeile wurde ausgewählt");
        }
    }

}
