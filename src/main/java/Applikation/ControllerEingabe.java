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

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class ControllerEingabe {


    @FXML
    private DatePicker eingabeDatum;

    @FXML
    private TextField eingabeGrund;

    @FXML
    private ChoiceBox<String> eingabeAusgabe;

    @FXML
    private TextField eingabeZahl;

    @FXML
    private Button eingabeHinzufügen;

    @FXML
    private Button logout;



    public void userLogout(ActionEvent event)throws IOException {
        Driver d = new Driver();
        d.changeScene("/FXML/sample.fxml");
    }


    public void userEingabeHinzufügen(ActionEvent event)throws IOException {
        Driver d = new Driver();
        d.changeScene("/FXML/übersicht.fxml");
    }

}

