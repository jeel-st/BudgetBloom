package Controller;

import Interfaces.EntryInterface;
import Logic.LogicDatabase;
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
                saveEdit();
            } else {
                log.error("Geben sie eine Zahl in dem vorgegebenen Format an");
                errorLabel.setText("Bitte achten Sie bei der Einahme/Ausgabe auf das vorgegebene Format (xxx.xx)!");
            }
        }
    }


    public void initialize(URL url, ResourceBundle resourceBundle) {
        repeatBox.getItems().addAll(wiederholungen);
        repeatBox.setOnAction(this::getRepeat);
        if (isRegular.equals("Einmalig")) {
            repeatBox.setValue(isRegular);
            wiederholungshaeufigkeitBox.setVisible(false);
        } else {
            repeatBox.setValue(isRegular);
            wiederholungshaeufigkeitBox.setVisible(true);
            try {
                wiederholungshaeufigkeitBox.setValue(showContentOfWiederholungshaeufigkeitBox());
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

    public String showContentOfWiederholungshaeufigkeitBox() throws Exception, SQLException {

        try (Connection con = dc.getConnection()) {
            log.info("Connection to database succeed");
            log.info(isregularBool(isRegular));
            String sql = "SELECT frequency FROM konto" + localUsername + " WHERE edate = ? AND note = ? AND amount = ? AND bankBalance = ? AND importance = ? AND isregular = ?";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {

                stmt.setDate(1, Date.valueOf(date));
                stmt.setString(2, note);
                stmt.setDouble(3, amount);
                stmt.setDouble(4, bankBalance);
                stmt.setInt(5, importance);
                stmt.setBoolean(6, isregularBool(isRegular));

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String s = rs.getString("frequency");
                        log.info(s);
                        return s;
                    } else {
                        log.error("Es wurde kein passender Datensatz gefunden");
                        throw new Exception();
                    }
                }
            } catch (Exception e) {
                log.error("Datenbank funktioniert nicht", e);
                throw e;
            }
        } catch (SQLException e) {
            log.error("Error closing connection", e);
            throw e;
        }
    }
            public boolean isregularBool (String s){
                boolean isregularBool;
                if (s.equals("Einmalig")) {
                    isregularBool = false;
                    return isregularBool;
                } else {
                    isregularBool = true;
                    return isregularBool;
                }
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
            public void saveEdit () throws SQLException, IOException {

                try (Connection con = dc.getConnection()) {
                    log.info("Connection to database succeed");

                    int sliderWert = (int) skala.getValue(); //slider Wert wird geholt

                    String sql = "UPDATE konto" + localUsername + " SET edate = ?, note = ?, amount = ?, importance = ? , isregular = ?, frequency = ? WHERE edate= ? AND note = ? AND amount = ? AND bankbalance = ? AND isregular = ? ";
                    PreparedStatement stmt = con.prepareStatement(sql);
                    try {
                        stmt.setDate(1, Date.valueOf((eingabeDatum.getValue())));
                    } catch (Exception e) {
                        log.error("Datum geht nicht");
                        errorLabel.setText("Bitte fügen Sie ein Datum hinzu!");
                        return;
                    }

                    try {
                        String grund = eingabeGrund.getText();
                        if (grund.isEmpty()) {
                            throw new IllegalArgumentException("Grund ist ein Pflichtfeld");
                        }
                        stmt.setString(2, grund);
                    } catch (IllegalArgumentException e) {
                        log.error("Grund leer");
                        errorLabel.setText("Bitte fügen Sie einen Grund hinzu!");
                        return;
                    } catch (Exception e) {
                        log.error("Grund geht nicht");
                        errorLabel.setText("Irgendetwas stimmt mit dem Grund nicht!");
                        return;
                    }

                    try {
                        stmt.setDouble(3, kontoVeränderungsÜberprüferEdit());
                        log.info("Kontoänderungseingabe erfolgreich");
                    } catch (Exception e) {
                        log.error("Kontoänderung geht nicht");
                        errorLabel.setText("Bitte achten Sie bei der Einahme/Ausgabe auf das vorgegebene Format (xxx.xx)!");
                    }


                    stmt.setInt(4, sliderWert);
                    stmt.setBoolean(5, isregularBool(repeatBox.getValue()));
                    stmt.setString(6, checkFrequency());
                    stmt.setDate(7, Date.valueOf(date));
                    stmt.setString(8, note);
                    stmt.setDouble(9, amount);
                    stmt.setDouble(10, bankBalance);
                    stmt.setBoolean(11, isregularBool(isRegular));
                    stmt.executeUpdate();

                    d.changeScene("/FXML/overview.fxml");
                } catch (SQLException e) {
                    log.error("Couldn't connect to Database");
                }
            }

        public String checkFrequency () {
            if (isregularBool(repeatBox.getValue()) && wiederholungshaeufigkeitBox.getValue().equals("täglich")) {
                return "täglich";
            } else if (isregularBool(repeatBox.getValue()) && wiederholungshaeufigkeitBox.getValue().equals("monatlich")) {
                return "monatlich";
            } else if (isregularBool(repeatBox.getValue()) && wiederholungshaeufigkeitBox.getValue().equals("jährlich")) {
                return "jährlich";
            } else {
                return null;
            }
        }

        public double kontoVeränderungsÜberprüferEdit () {

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

    }
