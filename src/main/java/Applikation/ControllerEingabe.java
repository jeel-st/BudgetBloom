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
    public static Logger log = LogManager.getLogger(ControllerEingabe.class);


    //kommt in die choicebox:
    private String[] eingabe = {"Einnahme", "Ausgabe"};
    Driver d = new Driver();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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

    //Label verknüpfen:
    public void getEingabe(ActionEvent event) {
        String myEingabe = myChoiceBox.getValue();
        eingabeText.setText("Neue " + myEingabe + ":");
    }


    public void userAbbruch(ActionEvent event) throws IOException {

        d.changeScene("/FXML/übersicht.fxml");
    }


    public void userEingabeHinzufügen(ActionEvent event) throws IOException, SQLException {
        if(überprüfungDatentypDouble(eingabeZahl.getText())) {
            log.info(eingabeDatum.getValue());
            kontoVeränderung();
        }else{
            log.error("Geben sie eine Zahl in dem vorgegebenen Format an");
        }
    }


    public void kontoVeränderung() throws SQLException, IOException {
        String url = "jdbc:postgresql://foo.mi.hdm-stuttgart.de/js486";
        String pass = "(JJS)2003ab";
        String user = "js486";

        Connection con = DriverManager.getConnection(url, user, pass);
        log.info("Connection to database succeed");

        int sliderWert = (int) skala.getValue(); //slider Wert wird geholt

        String sql = "INSERT INTO konto" + Login.publicusername + " VALUES (DEFAULT, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = con.prepareStatement(sql);
        try {
            stmt.setDate(1, Date.valueOf((eingabeDatum.getValue())));
        } catch (Exception e) {
            log.error("Datum geht nicht");
        }
        stmt.setString(2, eingabeGrund.getText());
        try {
            stmt.setDouble(3, kontoVeränderungsÜberprüfer());
            log.info("Kontoänderungseingabe erfolgreich");
        } catch (Exception e) {
            log.error("Kontoänderungseingabe hat nicht geklappt");

        }
        try {
            double neuerKontostand = aktuellerKontostand();
            log.info(neuerKontostand);
            stmt.setDouble(4, neuerKontostand);
        } catch (Exception e) {
            log.error("Aktueller Kontostand  konnte nicht aufgerufen werden");
        }

        stmt.setInt(5, sliderWert);

        stmt.executeUpdate();

        d.changeScene("/FXML/übersicht.fxml");
    }


    public double aktuellerKontostand() throws SQLException {
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
                return neuerKontostand;
            }
        } catch (Exception e) {
            log.error("kein Kontostand gefunden");
        }


        return 0;
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

