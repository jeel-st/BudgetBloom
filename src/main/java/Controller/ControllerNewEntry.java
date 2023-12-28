package Controller;
import Interfaces.EntryInterface;
import LocalExceptions.NewEntryExceptions.AmountChangeIsNullException;
import LocalExceptions.NewEntryExceptions.NoteIsNullException;
import LocalExceptions.NewEntryExceptions.ParseDateException;
import LocalExceptions.NewEntryExceptions.ParseDoubleException;
import Logic.LogicDatabase;
import Logic.LogicFacade;
import Logic.LogicNewEntry;
import Singleton.SingletonUser;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import mainpackage.Driver;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


import javafx.scene.control.Button;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ControllerNewEntry implements Initializable, EntryInterface {

    @FXML
    private Label eingabeText;

    @FXML
    private Label errorLabel;

    @FXML
    private DatePicker eingabeDatum;

    @FXML
    private TextField eingabeGrund;

    @FXML
    private ChoiceBox<String> myChoiceBox;

    @FXML
    private TextField eingabeZahl;

    @FXML
    private Label skalaLabel;

    int mySkalaZahl;

    @FXML
    private Slider skala;

    @FXML
    private Button eingabeHinzufügen;

    @FXML
    private Button abbrechen;
    @FXML
    private ChoiceBox<String> repeatBox;
    @FXML
    private ChoiceBox<String> wiederholungshaeufigkeitBox;

    public static Logger log = LogManager.getLogger(ControllerNewEntry.class);

    private String[] wiederholungen = {"Einmalig", "Regelmäßig"};
    //kommt in die choicebox:
    private String[] eingabe = {"Einnahme", "Ausgabe"};
    private String[] wiederholungsHäufigkeit = {"täglich", "monatlich", "jährlich"};
    Driver d = new Driver();
    LogicDatabase dc = new LogicDatabase();
    SingletonUser sp = SingletonUser.getInstance();
    private String localUsername = sp.getName();
    private Boolean repeatBool;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //ChoiceBox:
        myChoiceBox.getItems().addAll(eingabe);
        myChoiceBox.setOnAction(this::getEingabe);  //this:: ist ein reverence operator (zum Label)
        myChoiceBox.setValue("Einnahme");
        repeatBox.getItems().addAll(wiederholungen);
        repeatBox.setOnAction(this::getRepeat);
        repeatBox.setValue("Einmalig");
        wiederholungshaeufigkeitBox.getItems().addAll(wiederholungsHäufigkeit);
        wiederholungshaeufigkeitBox.setVisible(false);
        //WichtigkeitsSkala (Slider + Label):
        mySkalaZahl = (int) skala.getValue();
        skalaLabel.setText(Integer.toString(mySkalaZahl));
        skala.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {

                mySkalaZahl = (int) skala.getValue();
                skalaLabel.setText(Integer.toString(mySkalaZahl));

            }
        });
    }
    public void getRepeat(ActionEvent event){
        String repetition = repeatBox.getValue();
        if ("Regelmäßig".equalsIgnoreCase(repetition)) {
            wiederholungshaeufigkeitBox.setVisible(true);
        } else {
            wiederholungshaeufigkeitBox.setVisible(false);
        }
    }
    //Label verknüpfen:
    public void getEingabe(ActionEvent event) {
        String myEingabe = myChoiceBox.getValue();
        eingabeText.setText("Neue " + myEingabe + ":");
    }


    public void userAbbruch(ActionEvent event) throws IOException {
        d.changeScene("/FXML/overview.fxml");
    }


    public void userEingabeHinzufügen(ActionEvent event) throws SQLException, IOException {
            repeatBool  = LogicFacade.getInstance().checkIsRegularBoolean(repeatBox.getValue());
            int sliderValue = (int) skala.getValue();
            Boolean rightFormat = false;
            try{
                if(LogicFacade.getInstance().checkingFormats(eingabeZahl.getText(), eingabeGrund.getText(), eingabeDatum.getValue())){
                    rightFormat = true;
                }
            } catch (NoteIsNullException e) {
                errorLabel.setText("Bitte fügen Sie einen Grund hinzu!");
            } catch (ParseDoubleException e) {
                errorLabel.setText("Bitte achten Sie bei der Einahme/Ausgabe auf das vorgegebene Format (xxx.xx)!");
            } catch (ParseDateException e) {
                errorLabel.setText("Bitte fügen Sie ein Datum hinzu!");
            } catch (AmountChangeIsNullException e){
                errorLabel.setText("Das Feld für ihre Einnahmen/Ausgaben ist ein Pflichtfeld");
            }
        if(rightFormat) {
            if (wiederholungshaeufigkeitBox.getValue() != null && repeatBool || wiederholungshaeufigkeitBox.getValue() == null && !repeatBool) {
                LogicFacade.getInstance().kontoVeränderung((Double.parseDouble(eingabeZahl.getText())), myChoiceBox.getValue(), sliderValue, eingabeGrund.getText(), Date.valueOf((eingabeDatum.getValue())), wiederholungshaeufigkeitBox.getValue(), repeatBool);
                d.changeScene("/FXML/overview.fxml");
            } else {
                log.error("Geben sie an, wie oft die Ausgabe/Einnahme wiederholt werden soll");
                errorLabel.setText("Geben sie an, wie oft die Ausgabe/Einnahme wiederholt werden soll");
            }
        }
        }


}

