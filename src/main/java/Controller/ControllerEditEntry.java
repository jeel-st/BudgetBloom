package Controller;

import Interfaces.EntryInterface;
import Logic.LogicDatabase;
import Logic.LogicFacade;
import Singleton.SingletonEditValues;
import Singleton.SingletonUser;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import mainpackage.Driver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

public class ControllerEditEntry implements Initializable, EntryInterface {

    @FXML
    private Label errorLabel;

    @FXML
    private Button abbrechen;

    @FXML
    private DatePicker eingabeDatum;

    @FXML
    private TextField eingabeGrund;

    @FXML
    private Button eingabeSpeichern;

    @FXML
    private Label eingabeText;

    @FXML
    private TextField eingabeZahl;

    @FXML
    private ChoiceBox<String> myChoiceBox;

    @FXML
    private Slider skala;

    @FXML
    private Label skalaLabel;
    @FXML
    private ChoiceBox<String> repeatBox;
    @FXML
    private ChoiceBox<String> wiederholungshaeufigkeitBox;
    private String[] wiederholungen = {"Einmalig", "Regelmäßig"};
    private String[] wiederholungsHäufigkeit = {"täglich", "monatlich", "jährlich"};
    int mySkalaZahl;
    private String[] eingabe = {"Einnahme", "Ausgabe"};
    SingletonEditValues sev = SingletonEditValues.getInstance();

    public String date = sev.getDate();
    public String note = sev.getNote();
    public double bankBalance = sev.getBankbalance();
    public double amount = sev.getAmount();
    public int importance = sev.getImportance();
    public String isRegular = sev.getIsregular();
    Driver d = new Driver();
    LogicDatabase dc = new LogicDatabase();
    SingletonUser sp = SingletonUser.getInstance();
    private final String localUsername = sp.getName();
    public static Logger log = LogManager.getLogger(ControllerEditEntry.class);

    @FXML
    public void userAbbruch(ActionEvent event) throws IOException {
        d.changeScene("/FXML/overview.fxml");
    }

    @FXML
    void userEingabeSpeichern(ActionEvent event) throws SQLException, IOException {
        ControllerNewEntry c = new ControllerNewEntry();
        if (repeatBox.getValue().equals("Regelmäßig") && wiederholungshaeufigkeitBox.getValue() == null) {
            log.error("Geben sie eine Frequenz an");
        } else {
            if (c.überprüfungDatentypDouble(eingabeZahl.getText())) {
                String errorLabelText = LogicFacade.getInstance().saveEdit(eingabeDatum.getValue(), eingabeGrund.getText(), (int) skala.getValue(), repeatBox.getValue(), eingabeZahl.getText(), myChoiceBox.getValue(), wiederholungshaeufigkeitBox.getValue());
                errorLabel.setText(errorLabelText);
                if (Objects.equals(errorLabelText, "Edit was saved successfully")) {
                    d.changeScene("/FXML/overview.fxml");
                }
            } else {
                log.error("Geben sie eine Zahl in dem vorgegebenen Format an");
                errorLabel.setText("Bitte achten Sie bei der Einahme/Ausgabe auf das vorgegebene Format (xxx.xx)!");
            }
        }
    }


    public void initialize(URL url, ResourceBundle resourceBundle) {
        repeatBox.getItems().addAll(wiederholungen);
        repeatBox.setOnAction(this::getRepeat);
        // bis hier stehen lassen

        if (isRegular.equals("Einmalig")) {
            repeatBox.setValue(isRegular);
            wiederholungshaeufigkeitBox.setVisible(false);
        } else {
            repeatBox.setValue(isRegular);
            wiederholungshaeufigkeitBox.setVisible(true);
            try {
                wiederholungshaeufigkeitBox.setValue(LogicFacade.getInstance().showContentOfWiederholungshaeufigkeitBox());
            } catch (Exception e) {
                log.error("Content passt nicht in WiederholungshäufigkeitsBox");
            }
        }
        wiederholungshaeufigkeitBox.getItems().addAll(wiederholungsHäufigkeit);

        try {
            if (amount >= 0) {
                myChoiceBox.setValue("Einnahme");
            } else {
                myChoiceBox.setValue("Ausgabe");
            }
            log.info("Date was set successfully");
        } catch (Exception e) {
            log.error("Date wasn't set");
        }
        eingabeDatum.setValue(LocalDate.parse(date));
        eingabeGrund.setText(note);
        eingabeZahl.setText(String.valueOf(amount));
        skala.setValue(importance);


        //ChoiceBox:
        myChoiceBox.getItems().addAll(eingabe);
        myChoiceBox.setOnAction(this::getEingabe);  //this:: ist ein reverence operator (zum Label)


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
            public void getRepeat (ActionEvent event){
                String repetition = repeatBox.getValue();
                if ("Regelmäßig".equalsIgnoreCase(repetition)) {
                    wiederholungshaeufigkeitBox.setVisible(true);
                } else {
                    wiederholungshaeufigkeitBox.setVisible(false);
                }
            }
            public void getEingabe (ActionEvent event){
                String myEingabe = myChoiceBox.getValue();
                eingabeText.setText("Neue " + myEingabe + ":");
            }

    }

