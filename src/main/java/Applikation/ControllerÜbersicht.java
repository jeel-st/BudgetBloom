package Applikation;

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
import java.util.Date;
import java.util.ResourceBundle;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


public class ControllerÜbersicht implements Initializable{

    @FXML
    private TableView<User> table;

    @FXML
    private TableColumn<User, String> datum;

    @FXML
    private TableColumn<User, String> grund;

    @FXML
    private TableColumn<User, Double> betrag;

    @FXML
    private TableColumn<User, Double> kontostand;


    @FXML
    private Button logout;

    @FXML
    private Button neueEingabe;


    //Oberserver Liste weil Tabelle es als Input nutzt
    ObservableList<User> list = FXCollections.observableArrayList(
            new User("17.11.2023", "Lebensmittel", -12.3, 123.45)
    );

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        datum.setCellValueFactory(new PropertyValueFactory <User, String>("datum"));
        grund.setCellValueFactory(new PropertyValueFactory <User, String>("grund"));
        betrag.setCellValueFactory(new PropertyValueFactory <User, Double>("betrag"));
        kontostand.setCellValueFactory(new PropertyValueFactory <User, Double>("kontostand"));

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


}
