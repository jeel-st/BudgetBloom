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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ControllerEingabe implements Initializable {

    @FXML
    private Label eingabeText;

    @FXML
    private DatePicker eingabeDatum;

    @FXML
    private TextField eingabeGrund;

    @FXML
    private ChoiceBox<String> myChoiceBox;

    @FXML
    private TextField eingabeZahl;

    @FXML
    private Button eingabeHinzufügen;

    @FXML
    private Button logout;
    public static Logger log = LogManager.getLogger(ControllerEingabe.class);


    //kommt in die choicebox:
    private String[] eingabe = {"Einnahme", "Ausgabe"};


    //ChoicBox:
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        myChoiceBox.getItems().addAll(eingabe);
        myChoiceBox.setOnAction(this::getEingabe);  //this:: ist ein reverence operator (zum Label)
    }

    //Label verknüpfen:
    public void getEingabe(ActionEvent event){
        String myEingabe = myChoiceBox.getValue();
        eingabeText.setText("Neue " + myEingabe + ":" );
    }


    public void userLogout(ActionEvent event)throws IOException {
        Driver d = new Driver();
        d.changeScene("/FXML/sample.fxml");
    }


    public void userEingabeHinzufügen(ActionEvent event)throws IOException {
        Driver d = new Driver();
        d.changeScene("/FXML/übersicht.fxml");
    }



}

