package Applikation;
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
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.ResourceBundle;


import javafx.scene.control.Button;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ControllerEingabe implements Initializable {

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

    public static Logger log = LogManager.getLogger(ControllerEingabe.class);

    private String[] wiederholungen = {"Einmalig", "Regelmäßig"};
    //kommt in die choicebox:
    private String[] eingabe = {"Einnahme", "Ausgabe"};
    private String[] wiederholungsHäufigkeit = {"täglich", "monatlich", "jährlich"};
    Driver d = new Driver();


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
        d.changeScene("/FXML/übersicht.fxml");
    }


    public void userEingabeHinzufügen(ActionEvent event) throws IOException, SQLException {

            if(wiederholungshaeufigkeitBox.getValue() != null && checkIsRegularBoolean()|| wiederholungshaeufigkeitBox.getValue() == null && checkIsRegularBoolean() == false) {
                kontoVeränderung();
            }else{
                log.error("Geben sie an, wie oft die Ausgabe/Einnahme wiederholt werden soll");
            }
        }


    public void kontoVeränderung() throws SQLException, IOException {
        String url = "jdbc:postgresql://foo.mi.hdm-stuttgart.de/js486";
        String pass = "(JJS)2003ab";
        String user = "js486";

        Connection con = DriverManager.getConnection(url, user, pass);
        log.info("Connection to database succeed");

        int sliderWert = (int) skala.getValue(); //slider Wert wird geholt

        String sql = "INSERT INTO konto" + Login.publicusername + " VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = con.prepareStatement(sql);
        try {
            stmt.setDouble(3, kontoVeränderungsÜberprüfer());
            log.info("Kontoänderungseingabe erfolgreich");
        } catch (Exception e) {
            log.error("Kontoänderungseingabe hat nicht geklappt");
            errorLabel.setText("Bitte achten Sie bei der Einahme/Ausgabe auf das vorgegebene Format (xxx.xx)!");
        }

        try {
            String grund = eingabeGrund.getText();
            if (grund.isEmpty()) {
                throw new IllegalArgumentException("Grund ist ein Pflichtfeld");
            }
            stmt.setString(2, grund);
        } catch (Exception e) {
            log.error("Grund geht nicht");
            errorLabel.setText("Bitte fügen Sie einen Grund hinzu!" );
        }

        try {
            stmt.setDate(1, Date.valueOf((eingabeDatum.getValue())));
        }catch (Exception e){
            log.error("Datum geht nicht");
            errorLabel.setText("Bitte fügen Sie ein Datum hinzu!");
            return;
        }


        try {
            double neuerKontostand = aktuellerKontostand();
            log.info("Neuer Kontostand: " + neuerKontostand);
            stmt.setDouble(4, neuerKontostand);
        } catch (Exception e) {
            log.error("Aktueller Kontostand  konnte nicht aufgerufen werden");
        }



        stmt.setInt(5, sliderWert);

        try {
            stmt.setBoolean(6, checkIsRegularBoolean());
            stmt.setString(7, checkFrequency());
            stmt.executeUpdate();
            d.changeScene("/FXML/übersicht.fxml");

        }catch (Exception e){
            log.info("Eingabe konnte nicht hinzugefügt werden");
        }

    }

    public boolean checkIsRegularBoolean(){
        if(repeatBox.getValue().equals("Einmalig")){
            return false;
        }else{
            return true;
        }
    }

    public String checkFrequency(){
        if(checkIsRegularBoolean()==true && wiederholungshaeufigkeitBox.getValue().equals("täglich")){
            return "täglich";
        }else if(checkIsRegularBoolean()==true && wiederholungshaeufigkeitBox.getValue().equals("monatlich")){
            return "monatlich";
        }else if(checkIsRegularBoolean()==true && wiederholungshaeufigkeitBox.getValue().equals("jährlich")){
            return "jährlich";
        }else {
            return null;
        }
    }


    public double aktuellerKontostand() throws Exception,SQLException{
        String url = "jdbc:postgresql://foo.mi.hdm-stuttgart.de/js486";
        String pass = "(JJS)2003ab";
        String user = "js486";

        Connection con = DriverManager.getConnection(url, user, pass);
        log.info("Connection to database succeed");

        String sql = "SELECT bankBalance FROM konto" + Login.publicusername + " ORDER BY edate DESC, id DESC LIMIT 1";
        PreparedStatement stmt = con.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        try {
            while (rs.next()) {
                double Kontostand = rs.getDouble("bankBalance");

                log.info(Kontostand);
                double neuerKontostand = Kontostand + kontoVeränderungsÜberprüfer();
                neuerKontostand = Math.round(neuerKontostand * 100.0) / 100.0;
                return neuerKontostand;
            }
        } catch (Exception e) {
            log.error("kein Kontostand gefunden");
        }

        throw new Exception();
    }

    public double kontoVeränderungsÜberprüfer() {

            double d = Double.parseDouble(eingabeZahl.getText());
            if (myChoiceBox.getValue().equals("Einnahme")) {

                log.info(d);
                return d;
            } else {

                log.info(d);
                if (d == 0) {
                    return d;
                } else if (d > 0) {
                    return -d;
                } else {
                    return d;
                }

        }

    }
    public boolean überprüfungDatentypDouble (String s){
        try {
            Double.parseDouble(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}

