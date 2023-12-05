package Applikation;

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
import java.util.ResourceBundle;

public class ControllerEditEntry implements Initializable {

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
    int mySkalaZahl;
    private String[] eingabe = {"Einnahme", "Ausgabe"};

    public static String date;
    public static String note;
    public static double bankBalance;
    public static double amount;
    public static int importance;
    Driver d = new Driver();
    public static Logger log = LogManager.getLogger(ControllerEditEntry.class);
    @FXML
    void userAbbruch(ActionEvent event) throws IOException {
        d.changeScene("/FXML/übersicht.fxml");
    }

    @FXML
    void userEingabeSpeichern(ActionEvent event) throws SQLException, IOException {
        ControllerEingabe c = new ControllerEingabe();
        if(c.überprüfungDatentypDouble(eingabeZahl.getText())) {
            saveEdit();
        }else{
            log.error("Geben sie eine Zahl in dem vorgegebenen Format an");
        }
    }



    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            if(amount >= 0){
                myChoiceBox.setValue("Einnahme");
            }else{
                myChoiceBox.setValue("Ausgabe");
            }
            log.info("Date was set successfully");
        }catch (Exception e){
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
    public void getEingabe(ActionEvent event){
        String myEingabe = myChoiceBox.getValue();
        eingabeText.setText("Neue " + myEingabe + ":" );
    }
    public void saveEdit() throws SQLException, IOException {

        String url = "jdbc:postgresql://foo.mi.hdm-stuttgart.de/js486";
        String pass = "(JJS)2003ab";
        String user = "js486";

        Connection con = DriverManager.getConnection(url, user, pass);
        log.info("Connection to database succeed");

        int sliderWert = (int) skala.getValue(); //slider Wert wird geholt

        String sql = "UPDATE konto" + Login.publicusername + " SET edate = ?, note = ?, amount = ?, importance = ? WHERE edate= ? AND note = ? AND amount = ? AND bankbalance = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        try {
            stmt.setDate(1, Date.valueOf((eingabeDatum.getValue())));
        } catch (Exception e) {
            log.error("Datum geht nicht");
        }
        stmt.setString(2, eingabeGrund.getText());
        try {
            stmt.setDouble(3, Double.parseDouble(eingabeZahl.getText()));
            log.info("Kontoänderungseingabe erfolgreich");
        } catch (Exception e) {
            log.error("Kontoänderung geht nicht");
        }
        stmt.setInt(4, sliderWert);
        stmt.setDate(5, Date.valueOf(date));
        stmt.setString(6, note);
        stmt.setDouble(7, amount);
        stmt.setDouble(8, bankBalance);

        stmt.executeUpdate();

        d.changeScene("/FXML/übersicht.fxml");
    }

}
